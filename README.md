# Suleimen Yeldossuly Student API

DevOps midterm project for **Suleimen Yeldossuly**, group **IT2-2310**, student ID **38320**.

This repository contains a Spring Boot application, Linux administration script, and the files needed for the Git, Docker, PostgreSQL, and Jenkins demonstrations. The application code will be developed in the personal CRUD feature branch.

## Project structure

- `scripts/` — personal Bash system report.
- `logs/` — generated report and analysis output.
- `backups/` — README backup for the Linux task.
- `src/` — Spring Boot source code.
- `Dockerfile`, `docker-compose.yml`, `Jenkinsfile` — added in their respective stages.

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

