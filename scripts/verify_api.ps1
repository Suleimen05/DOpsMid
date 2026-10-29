param(
    [string]$BaseUrl = 'http://localhost:8082',
    [switch]$VerifyPersistence
)

$ErrorActionPreference = 'Stop'
$api = "$($BaseUrl.TrimEnd('/'))/api/students"
$email = 'suleimen38320+' + [DateTimeOffset]::UtcNow.ToUnixTimeMilliseconds() + '@example.com'
$body = @{
    fullName = 'Suleimen Yeldossuly'
    email = $email
    groupName = 'IT2-2310'
} | ConvertTo-Json -Compress

$created = Invoke-RestMethod -Uri $api -Method Post -ContentType 'application/json' -Body $body
if (-not $created.id -or $created.fullName -ne 'Suleimen Yeldossuly') {
    throw 'POST did not create the expected student.'
}
$id = $created.id
Write-Output "POST $api -> 201 (id=$id)"

$all = Invoke-RestMethod -Uri $api -Method Get
if (-not (@($all.id) -contains $id)) { throw 'GET all did not return the new student.' }
Write-Output "GET $api -> 200 (id=$id present)"

$one = Invoke-RestMethod -Uri "$api/$id" -Method Get
if ($one.email -ne $email) { throw 'GET by ID returned unexpected data.' }
Write-Output "GET $api/$id -> 200"

$updatedEmail = 'updated-' + $email
$updateBody = @{
    fullName = 'Suleimen Yeldossuly'
    email = $updatedEmail
    groupName = 'IT2-2310'
} | ConvertTo-Json -Compress
$updated = Invoke-RestMethod -Uri "$api/$id" -Method Put -ContentType 'application/json' -Body $updateBody
if ($updated.email -ne $updatedEmail) { throw 'PUT did not update the email.' }
Write-Output "PUT $api/$id -> 200"

if ($VerifyPersistence) {
    docker restart suleimen-yeldossuly-postgres | Out-Null
    if ($LASTEXITCODE -ne 0) { throw 'PostgreSQL container restart failed.' }
    $healthy = $false
    for ($attempt = 0; $attempt -lt 40; $attempt++) {
        if ((docker inspect --format '{{.State.Health.Status}}' suleimen-yeldossuly-postgres) -eq 'healthy') {
            $healthy = $true
            break
        }
        Start-Sleep -Seconds 2
    }
    if (-not $healthy) { throw 'PostgreSQL did not become healthy after restart.' }
    docker restart suleimen-yeldossuly-student-api | Out-Null
    if ($LASTEXITCODE -ne 0) { throw 'Application container restart failed.' }
    $persisted = $null
    for ($attempt = 0; $attempt -lt 40; $attempt++) {
        try {
            $persisted = Invoke-RestMethod -Uri "$api/$id" -Method Get -TimeoutSec 3
            break
        } catch {
            Start-Sleep -Seconds 3
        }
    }
    if (-not $persisted -or $persisted.email -ne $updatedEmail) {
        throw 'The updated record was not available after restarting Compose services.'
    }
    Write-Output "GET $api/$id after restarting both containers -> 200 (record persisted)"
}

$deleted = Invoke-WebRequest -Uri "$api/$id" -Method Delete -UseBasicParsing
if ([int]$deleted.StatusCode -ne 204) { throw 'DELETE did not return 204.' }
Write-Output "DELETE $api/$id -> 204"

try {
    Invoke-RestMethod -Uri "$api/$id" -Method Get | Out-Null
    throw 'GET of a deleted student unexpectedly succeeded.'
} catch {
    if (-not $_.Exception.Response -or [int]$_.Exception.Response.StatusCode -ne 404) { throw }
}
Write-Output "GET $api/$id after DELETE -> 404"

try {
    Invoke-RestMethod -Uri "$api/999999" -Method Get | Out-Null
    throw 'GET of a nonexistent student unexpectedly succeeded.'
} catch {
    if (-not $_.Exception.Response -or [int]$_.Exception.Response.StatusCode -ne 404) { throw }
}
Write-Output "GET $api/999999 -> 404"
