# Дипломный проект по профессии "Тестировщик"

Дипломный проект — автоматизация тестирования комплексного сервиса, взаимодействующего с СУБД и API Банка.

## Начало работы

C помощью команды "git clone" осуществить клонирование репозитория по ссылке `https://github.com/SuslovQA/DiplomaProject.git`.

### Предустановки:

- Intellij IDEA
- Google Chrome
- GitHub, Git

### Установка и запуск
1. Открыть склонированный проект в Intellij IDEA.
2. Для запуска контейнеров ввести в терминале Intellij IDEA команду "docker-compose up".
3. Запустить jar-файл командой для mysql: `java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar artifacts/aqa-shop.jar`,
  
    или для postgresql: `java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar`.

Запуск считается успешным при наличии в логах сообщений: `Tomcat started on port(s): 8080 (http) with context path ''` или `Started ShopApplication in ... seconds (JVM running for ...)`.

4. Запуск SUT:
- для MySQL ввести в терминале команду `./gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"`.

- для PostgreSQL ввести в терминале команду `./gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"`.

5. Для того, чтобы ознакомиться с формой, нужно войти в браузер и ввести ссылку `http://localhost:8080`.
6. При необходимости отключения SUT, находясь в терминале Intellij IDEA, нажать клавиши CTRL+C.
7. При необходимости отключения контейнеров ввести в терминале Intellij IDEA команду docker-compose down.

### Инструкция для получения отчетов с помощью Allure

1. Выполнить шаги 1-4 инструкции подключения БД и включения SUT.
2. Ввести команду `./gradlew allureServe`.
