# Suleimen Yeldossuly Student API

DevOps midterm project for **Suleimen Yeldossuly**, group **IT2-2310**, student ID **38320**.

This repository contains a Spring Boot CRUD application, Linux administration script, and the files needed for the Git, Docker, PostgreSQL, and Jenkins demonstrations. Its GitHub repository is `Suleimen05/DOpsMid`; the project resources use the personalized name `suleimen-yeldossuly-student-api`.
This README also serves as Suleimen Yeldossuly's defense checklist. Demonstrate the commands below on this project.

## Project structure

- `scripts/` — personal Bash system report.
- `logs/` — generated report and analysis output.
- `backups/` — README backup for the Linux task.
- `src/` — Spring Boot source code.
- `Dockerfile`, `docker-compose.yml`, `Jenkinsfile` — Docker and Jenkins configuration.

## Linux report

On Linux, run:

```bash
chmod 744 scripts/Suleimen_Yeldossuly_system_report.sh
./scripts/Suleimen_Yeldossuly_system_report.sh
cp README.md backups/README_Suleimen_Yeldossuly_backup.md
ps aux | head
ps aux | grep '[j]ava' || true
tail -n 5 logs/Suleimen_Yeldossuly_system_report.log
grep 'Yeldossuly' logs/Suleimen_Yeldossuly_system_report.log > logs/Suleimen_Yeldossuly_log_analysis.txt
ls -l scripts/Suleimen_Yeldossuly_system_report.sh backups/README_Suleimen_Yeldossuly_backup.md
stat -c '%a %n' scripts/Suleimen_Yeldossuly_system_report.sh
cmp README.md backups/README_Suleimen_Yeldossuly_backup.md
```

`>` replaces a file's contents; `>>` appends to the end. The requested permission mode is `744`: owner can read, write, and execute; group and others can only read. Run `chmod` and `stat` on a Linux filesystem before the defense: Git records only whether a file is executable, so a clone may show `755` until `chmod 744` is run. `cmp` has no output when the backup matches the original.

## Build and test

Install JDK 17 or newer, then run `./gradlew test` on Linux or `gradlew.bat test` on Windows. Tests use an in-memory H2 database; the running application uses PostgreSQL.

For a local API check while Docker is unavailable, run `gradlew.bat bootTestRun --args="--spring.profiles.active=test --server.port=8082"` in one PowerShell window, then `powershell -ExecutionPolicy Bypass -File scripts/verify_api.ps1` in another. This temporary test profile uses H2 and does not demonstrate PostgreSQL persistence.

## Docker Compose

Set a local database password in the shell, then start the services:

```bash
export POSTGRES_PASSWORD='<choose-a-local-password>'
docker compose up --build -d
docker compose ps
docker image ls suleimen-yeldossuly-student-api
docker compose logs app --tail=40
docker inspect suleimen-yeldossuly-student-api --format '{{.Name}} {{.State.Status}} {{.Config.Image}}'
docker compose exec app sh -c 'java -version'
docker compose stop app
docker compose start app
```

In PowerShell, use `$env:POSTGRES_PASSWORD = '<choose-a-local-password>'` instead of `export`. Do not commit the password or a `.env` file. The app is at `http://localhost:8082/api/students`. Inside Docker it connects to PostgreSQL at `db:5432`, where `db` is the Compose service name.

Example requests for a Linux shell:

```bash
curl -i -X POST http://localhost:8082/api/students -H 'Content-Type: application/json' -d '{"fullName":"Suleimen Yeldossuly","email":"suleimen38320@example.com","groupName":"IT2-2310"}'
curl -i http://localhost:8082/api/students
curl -i http://localhost:8082/api/students/1
curl -i -X PUT http://localhost:8082/api/students/1 -H 'Content-Type: application/json' -d '{"fullName":"Suleimen Yeldossuly","email":"updated38320@example.com","groupName":"IT2-2310"}'
curl -i -X DELETE http://localhost:8082/api/students/1
curl -i http://localhost:8082/api/students/999999
```

Use the ID returned by POST if it is not `1`. To demonstrate persistence, create a student, run `docker compose restart`, and request the student again. `docker compose down` keeps the named database volume; `docker compose down -v` deletes it.

On Windows PowerShell, run `powershell -ExecutionPolicy Bypass -File scripts/verify_api.ps1 -VerifyPersistence` after `docker compose up --build -d`. The script uses the ID returned by POST, tests create/list/get/update/delete/404, restarts both Compose services, and verifies that the record still exists afterward. Use `powershell -ExecutionPolicy Bypass -File scripts/verify_api.ps1` without the switch to test an already running API without restarting Docker. Save the displayed HTTP results, `docker compose ps`, logs, image inspection, and container commands as defense evidence.

## Jenkins

Create a Pipeline job named `Suleimen_Yeldossuly_Student_API` using the repository URL `https://github.com/Suleimen05/DOpsMid.git` and script path `Jenkinsfile`. The Jenkins agent needs Java, Git, and Docker CLI access. The pipeline compiles, runs tests, builds the image, and verifies it. SCM polling checks for new commits about every five minutes. Automatic deployment is not part of this pipeline.

The image was first built as `suleimen-yeldossuly-student-api:v1.0-38320`. After the application version changed from `1.0.0` to `1.0.1`, it is rebuilt as `suleimen-yeldossuly-student-api:v1.0.1-38320`.

The `Suleimen_Yeldossuly_Student_API_Test` job is reserved for a controlled failing build on a personal test branch. The main job continues to use `main`.

For Jenkins evidence, temporarily set the test job's Git branch specification to the full commit SHA `b2783124e73a86e15d94381b15df26c1c96583a1` and turn off **Lightweight checkout**. Build the job and show the failed Test stage; Docker Build and Verify must be skipped. Then restore `*/feature/suleimen-yeldossuly-ci-failure` and turn Lightweight checkout back on. The branch contains the fix at `3a1f1ef`; build it and show success. To demonstrate automation, make a new commit on that monitored branch and show the polling-triggered build in Build History and Console Output. The two Groovy files in `jenkins/` define the jobs, but their presence alone does not prove that the jobs were created or run. Continuous Integration automatically builds and tests changes; Continuous Deployment also releases passing changes automatically. This project implements CI, not automatic deployment.

## Git evidence for the defense

Use `git log --graph --oneline --all --decorate` to show commits and merges, `git branch -a` to show the personal branches, `git tag` to show the version, and `git remote -v` to show the GitHub repository. Explain that `.gitignore` keeps build output and local `.env` files out of the repository. Passwords and database credentials belong in local environment variables or Jenkins Credentials, not in Git commits.
