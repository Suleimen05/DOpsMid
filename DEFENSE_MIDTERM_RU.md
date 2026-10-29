# Защита DevOps Midterm — Suleimen Yeldossuly

Студент: **Suleimen Yeldossuly**, группа **IT2-2310**, ID **38320**. Проект: Spring Boot API для студентов. Инструкция проверена на этом компьютере **30.10.2026**. Она запускает уже подготовленные контейнеры и Jenkins jobs; пароль базы данных вводить и показывать не нужно.

## 1. Перед показом: запустить всё

Открой **один PowerShell** в папке проекта и выполни команды по порядку:

```powershell
Set-Location 'C:\Users\PC\Documents\ChatGPT\dopsMID'
docker desktop start --timeout 120
docker start suleimen-yeldossuly-postgres
while ((docker inspect --format '{{.State.Health.Status}}' suleimen-yeldossuly-postgres) -ne 'healthy') { Start-Sleep -Seconds 2 }
docker start suleimen-yeldossuly-student-api
docker start suleimen-yeldossuly-jenkins
docker start suleimen-yeldossuly-linux
docker ps --filter name=suleimen-yeldossuly
Invoke-RestMethod 'http://localhost:8082/api/students'
```

**Ожидаемый результат:** четыре контейнера имеют статус `Up`, PostgreSQL — `healthy`. Последняя команда показывает JSON со студентом Suleimen Yeldossuly. Если Jenkins только запустился, подожди примерно полминуты перед открытием его страницы.

Открой в браузере две вкладки:

- API: [http://localhost:8082/api/students](http://localhost:8082/api/students)
- Jenkins: [http://localhost:8080](http://localhost:8080)

Это локальные адреса **этого компьютера**. Jenkins открыт только на `127.0.0.1:8080` для демонстрации. Не удаляй контейнеры или Docker volumes перед защитой: там находятся база и история сборок Jenkins.

**Вступление, которое можно сказать:** «Я сделал индивидуальный DevOps-проект: Linux-скрипт, историю Git, Spring Boot CRUD API, Docker Compose с PostgreSQL и Jenkins pipeline. Сейчас покажу каждый пункт задания и результат запуска».

## 2. Linux: структура, скрипт, права и лог

В том же PowerShell войди в подготовленный Linux-контейнер:

```powershell
docker exec -it suleimen-yeldossuly-linux bash
```

Теперь приглашение командной строки — **Linux**. Выполни всё одним проходом:

```bash
cd /workspace
pwd
find . -maxdepth 2 -print
ls -ld scripts logs backups src
ls -l Dockerfile docker-compose.yml Jenkinsfile build.gradle README.md
stat -c '%a %n' scripts/Suleimen_Yeldossuly_system_report.sh
./scripts/Suleimen_Yeldossuly_system_report.sh
tail -n 5 logs/Suleimen_Yeldossuly_system_report.log
grep 'Yeldossuly' logs/Suleimen_Yeldossuly_system_report.log > logs/Suleimen_Yeldossuly_log_analysis.txt
cat logs/Suleimen_Yeldossuly_log_analysis.txt
cmp README.md backups/README_Suleimen_Yeldossuly_backup.md && echo 'Backup matches README'
ls -l scripts/Suleimen_Yeldossuly_system_report.sh backups/README_Suleimen_Yeldossuly_backup.md
ps aux | head
exit
```

**Ожидаемый результат:** `stat` показывает `744`, а `ls -l` — `-rwxr--r--` для скрипта. Отчёт содержит имя, дату, пользователя, ОС, ядро, CPU, память и диски. `cat` показывает строку с фамилией; `cmp` печатает `Backup matches README`.

Чтобы показать именно работающий Java-процесс, уже снова в PowerShell выполни:

```powershell
docker exec suleimen-yeldossuly-jenkins sh -c 'ps aux | grep [j]ava'
```

**Что требовали и зачем:** создать личный Bash-скрипт и пустой лог, собрать сведения о системе, выставить права, скопировать README, показать процессы и проанализировать лог. Это проверяет базовые навыки Linux-администрирования. Права `744` означают: владелец читает, изменяет и запускает файл; остальные только читают. `>` перезаписывает файл, `>>` добавляет строки в конец. `#!/usr/bin/env bash` выбирает Bash для запуска скрипта.

**Что сказать:** «Скрипт собирает системную информацию и сохраняет её через `tee` одновременно на экран и в личный лог. Отдельной командой я отфильтровал лог по фамилии и сохранил результат. Резервная копия README совпадает с оригиналом».

## 3. Git и GitHub: личная история работы

Вернись в тот же PowerShell в папке проекта и покажи:

```powershell
git status --short --branch
git remote -v
git branch -a
git tag
git log --graph --oneline --all --decorate -20
git show --summary b80f7e2
```

**Что смотреть:** в истории есть начальный коммит с именем студента, два CRUD-коммита в личной ветке, ветка документации, merge-коммит `b80f7e2` с разрешённым конфликтом README, личный тег `v1.0-suleimen-yeldossuly`, отдельная ветка с намеренно сломанным и исправленным тестом. `origin` ведёт на `Suleimen05/DOpsMid`.

**Что требовали и зачем:** отдельные ветки и осмысленные коммиты показывают индивидуальную работу; merge показывает объединение изменений; конфликт README показывает, как вручную согласовать правки одной строки; тег отмечает версию. `.gitignore` исключает сборочные файлы и `.env`. Пароли не коммитят, потому что история Git хранит даже старые версии файлов.

**Что сказать:** «Я работал в личных feature-ветках, сделал несколько содержательных коммитов, объединил их с `main` и вручную разрешил конфликт README. Тег фиксирует версию. Git хранит историю локально, GitHub хранит удалённую копию и позволяет делиться ею».

Примечание для честного ответа: в задании предлагается имя репозитория вида `surname-name-student-api`. Фактическое имя GitHub-репозитория — `DOpsMid`; оно оставлено по вашему решению. Имена проекта, веток, образа, контейнеров и Jenkins jobs персонализированы.

## 4. Gradle и приложение

Покажи исходники в VS Code: `src/main/java/kz/suleimen/studentapi/Student.java`, `StudentRepository.java`, `StudentController.java`, а также тест `src/test/java/kz/suleimen/studentapi/StudentApiIntegrationTest.java`. Затем в PowerShell:

```powershell
.\gradlew.bat clean test --no-daemon
```

**Ожидаемый результат:** четыре строки `PASSED` и `BUILD SUCCESSFUL`.

**Что требовали и зачем:** CRUD означает создать, прочитать, изменить и удалить студента. `Student` описывает поля и ограничения, `StudentRepository` работает с базой через JPA, `StudentController` обрабатывает HTTP-запросы. Тесты проверяют основные операции, ошибочный ввод и `404` для отсутствующей записи. Автоматические тесты нужны, чтобы ошибка остановила CI до сборки Docker-образа.

## 5. Docker, PostgreSQL и API

Покажи файлы `Dockerfile` и `docker-compose.yml`, затем выполни:

```powershell
docker ps --filter name=suleimen-yeldossuly
docker inspect suleimen-yeldossuly-postgres --format '{{.State.Health.Status}}'
docker image ls suleimen-yeldossuly-student-api
docker image inspect suleimen-yeldossuly-student-api:v1.0.1-38320 --format 'Image size: {{.Size}} bytes'
docker volume ls --filter name=suleimen-yeldossuly-student-api_postgres-data
docker network ls --filter name=student-api
docker logs --tail 15 suleimen-yeldossuly-student-api
docker exec suleimen-yeldossuly-student-api java -version
```

**Ожидаемый результат:** приложение и PostgreSQL работают, база `healthy`, есть личный Docker-образ, постоянный том и отдельная сеть. В логах нет ошибки соединения с базой.

Теперь одной командой покажи весь CRUD и сохранность данных после перезапуска **обоих** контейнеров:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\verify_api.ps1 -VerifyPersistence
```

**Ожидаемый результат:** `POST -> 201`, `GET -> 200`, `PUT -> 200`, после перезапуска запись снова найдена (`record persisted`), `DELETE -> 204`, запросы отсутствующих студентов дают `404`. Скрипт удаляет только созданную им тестовую запись. Личная запись, оставленная для просмотра в браузере, сохраняется.

**Что требовали и зачем:** `Dockerfile` состоит из этапа сборки на JDK и этапа запуска на JRE; в конечный образ переносится готовый JAR. Compose запускает приложение и PostgreSQL вместе. Приложение обращается к `db:5432`, потому что `db` — имя сервиса в Docker-сети; `localhost` внутри контейнера указывал бы на сам контейнер. Health check не даёт приложению стартовать до готовности базы. Именованный volume сохраняет записи при перезапуске контейнеров.

**Что сказать:** «Образ — шаблон приложения, контейнер — запущенный экземпляр образа. Я собрал персональный образ `suleimen-yeldossuly-student-api:v1.0.1-38320`, запустил его с PostgreSQL и проверил API. После перезапуска обоих контейнеров запись осталась в volume».

## 6. Jenkins: успешная, неуспешная и автоматическая сборки

Открой [Jenkins](http://localhost:8080). В интерфейсе уже созданы два личных задания:

| Что показать | Ссылка | Что видно |
| --- | --- | --- |
| Основной pipeline | [Suleimen_Yeldossuly_Student_API №1](http://localhost:8080/job/Suleimen_Yeldossuly_Student_API/1/console) | `SUCCESS`, пять этапов, имя студента, четыре теста `PASSED`, образ найден |
| Контролируемая ошибка | [Тестовый job №2](http://localhost:8080/job/Suleimen_Yeldossuly_Student_API_Test/2/console) | `createAndReadStudent() FAILED`, Docker Build и Verify пропущены |
| Исправление | [Тестовый job №3](http://localhost:8080/job/Suleimen_Yeldossuly_Student_API_Test/3/console) | `SUCCESS`, четыре теста `PASSED` |
| Автоматический запуск | [Тестовый job №4](http://localhost:8080/job/Suleimen_Yeldossuly_Student_API_Test/4/console) | `Started by an SCM change`, коммит `07ae1ec`, `SUCCESS` |

Для защиты показывай **№2–4** у тестового job. №1 этого job был неудачной настройкой checkout до запуска тестов, поэтому он не является примером контролируемой ошибки.

Покажи `Jenkinsfile`: этапы `Checkout → Build → Test → Docker Build → Verify` и строку `pollSCM('H/5 * * * *')`. Jenkins берёт код из GitHub, собирает и тестирует его, создаёт Docker-образ и проверяет, что образ существует. Сломанный тест был только в личной тестовой ветке; `main` не ломали. После исправления новый коммит `07ae1ec` запустил job автоматически через SCM polling.

**Что требовали и зачем:** Jenkins автоматизирует повторяемые проверки. Если Build или Test падает, дальнейшие этапы не выполняются. CI — автоматическая сборка и тестирование изменений. Continuous Delivery готовит релиз, но оставляет ручное решение о выпуске; Continuous Deployment выпускает автоматически. В этом задании автоматическое развёртывание не требовалось и не настроено. Секреты для публичного GitHub-репозитория не понадобились; если бы репозиторий был приватным, доступ хранился бы в Jenkins Credentials.

**Что сказать:** «Вот успешный основной pipeline. Здесь я специально сломал ожидание теста в отдельной ветке: Jenkins остановился на Test. Здесь я исправил тест, и сборка прошла. Последняя сборка началась сама после коммита, что видно по `Started by an SCM change`».

## 7. Короткие ответы на частые вопросы

| Вопрос | Короткий ответ |
| --- | --- |
| Что делает `git commit`? | Сохраняет снимок подготовленных изменений в истории Git. |
| Что такое branch? | Отдельная линия работы, чтобы развивать функцию без изменения `main`. |
| Что такое merge conflict? | Две ветки изменили один участок файла; человек выбирает итоговый текст. |
| Что делает `chmod`? | Меняет права доступа к файлу. |
| `RUN` и `CMD` в Dockerfile? | `RUN` выполняется при сборке образа, `CMD` задаёт команду при запуске контейнера. Здесь для запуска используется `ENTRYPOINT`. |
| Image и container? | Образ — шаблон; контейнер — работающий экземпляр. |
| Network и volume? | Сеть соединяет контейнеры; volume хранит данные вне жизненного цикла контейнера. |
| Что такое Jenkinsfile? | Кодовое описание этапов Jenkins pipeline в репозитории. |
| Почему тест перед Docker Build? | Ошибочный код не должен переходить к упаковке образа. |
| Почему не хранить пароль в Git? | Коммиты и старые версии файлов могут быть доступны другим людям. |

## После защиты

Если хочешь остановить **только эти** контейнеры, выполни в PowerShell:

```powershell
docker stop suleimen-yeldossuly-linux suleimen-yeldossuly-jenkins suleimen-yeldossuly-student-api suleimen-yeldossuly-postgres
```

Для следующего показа снова выполни раздел 1. Не используй `docker compose down -v`: параметр `-v` удалит том PostgreSQL с данными.
