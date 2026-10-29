# Suleimen Yeldossuly Student API

DevOps midterm project for **Suleimen Yeldossuly**, group **IT2-2310**, student ID **38320**.

This README is Suleimen Yeldossuly's defense checklist for the Linux, Git, Spring Boot, Docker, PostgreSQL, and Jenkins parts of the DevOps midterm. Each command below is intended to be demonstrated on the student's own project.

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

## Git evidence for the defense

Use `git log --graph --oneline --all --decorate` to show commits and merges, `git branch -a` to show the personal branches, `git tag` to show the version, and `git remote -v` to show the GitHub repository. Explain that `.gitignore` keeps build output and local `.env` files out of the repository. Passwords and database credentials belong in local environment variables or Jenkins Credentials, not in Git commits.

