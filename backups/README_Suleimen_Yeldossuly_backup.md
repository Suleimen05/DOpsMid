# Suleimen Yeldossuly Student API

DevOps midterm project for **Suleimen Yeldossuly**, group **IT2-2310**, student ID **38320**.

This repository contains a Spring Boot CRUD application, Linux administration script, and the files needed for the Git, Docker, PostgreSQL, and Jenkins demonstrations. Its GitHub repository is `Suleimen05/DOpsMid`; the project resources use the personalized name `suleimen-yeldossuly-student-api`.

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
tail -n 5 logs/Suleimen_Yeldossuly_system_report.log
grep 'Suleimen' logs/Suleimen_Yeldossuly_system_report.log > logs/Suleimen_Yeldossuly_log_analysis.txt
ls -l scripts/Suleimen_Yeldossuly_system_report.sh backups/README_Suleimen_Yeldossuly_backup.md
```

`>` replaces a file's contents; `>>` appends to the end. The requested permission mode is `744`: owner can read, write, and execute; group and others can only read.

## Build and test

Install JDK 17 or newer, then run `./gradlew test` on Linux or `gradlew.bat test` on Windows. Tests use an in-memory H2 database; the running application uses PostgreSQL.

## Docker Compose

Set a local database password in the shell, then start the services:

```bash
export POSTGRES_PASSWORD='<choose-a-local-password>'
docker compose up --build -d
docker compose ps
```

In PowerShell, use `$env:POSTGRES_PASSWORD = '<choose-a-local-password>'` instead of `export`. Do not commit the password or a `.env` file. The app is at `http://localhost:8082/api/students`. Inside Docker it connects to PostgreSQL at `db:5432`, where `db` is the Compose service name.

Example requests:

```bash
curl -i -X POST http://localhost:8082/api/students -H 'Content-Type: application/json' -d '{"fullName":"Suleimen Yeldossuly","email":"suleimen38320@example.com","groupName":"IT2-2310"}'
curl -i http://localhost:8082/api/students
curl -i http://localhost:8082/api/students/1
curl -i -X PUT http://localhost:8082/api/students/1 -H 'Content-Type: application/json' -d '{"fullName":"Suleimen Yeldossuly","email":"updated38320@example.com","groupName":"IT2-2310"}'
curl -i -X DELETE http://localhost:8082/api/students/1
curl -i http://localhost:8082/api/students/999999
```

Use the ID returned by POST if it is not `1`. To demonstrate persistence, create a student, run `docker compose restart`, and request the student again. `docker compose down` keeps the named database volume; `docker compose down -v` deletes it.

## Jenkins

Create a Pipeline job named `Suleimen_Yeldossuly_Student_API` using the repository URL `https://github.com/Suleimen05/DOpsMid.git` and script path `Jenkinsfile`. The Jenkins agent needs Java, Git, and Docker CLI access. The pipeline compiles, runs tests, builds the image, and verifies it. SCM polling checks for new commits about every five minutes. Automatic deployment is not part of this pipeline.

The image was first built as `suleimen-yeldossuly-student-api:v1.0-38320`. After the application version changed from `1.0.0` to `1.0.1`, it is rebuilt as `suleimen-yeldossuly-student-api:v1.0.1-38320`.

The `Suleimen_Yeldossuly_Student_API_Test` job is reserved for a controlled failing build on a personal test branch. The main job continues to use `main`.
