
# QA Automation Portfolio - Erick Cuevas

## Sobre Mí

Ingeniero QA con 8 años de experiencia en el sector bancario y financiero. Especialista en automatización de pruebas con Java, Python, Selenium, Cucumber y Robot Framework.


## Estructura del Portafolio

| Proyecto | Tecnologías | Descripción |
|----------|-------------|-------------|
| Java + Selenium + Cucumber | Java, Selenium, Cucumber, Maven | Framework BDD para pruebas web bancarias |
| Python + Behave | Python, Behave, Selenium | Pruebas BDD para portales financieros |
| Robot Framework | Robot Framework, Selenium | Keyword-driven testing |
| JMeter Performance | JMeter | Pruebas de carga y estrés |
| Postman API Tests | Postman, Newman | Pruebas de API REST |



Requisitos técnicos:

Maven para gestión de dependencias (pom.xml)

Page Object Model - Estructura de carpetas con pages/

Cucumber con archivos .feature en Gherkin

TestNG o JUnit como test runner

WebDriverManager o drivers configurados

Reporting (Allure, Extent Reports o HTML)

Estructura específica para Java + Cucumber:

text
selenium-java-cucumber/
├── pom.xml
├── src/
│   ├── main/java/com/portafolio/
│   │   ├── pages/
│   │   │   ├── LoginPage.java
│   │   │   ├── DashboardPage.java
│   │   │   └── TransferPage.java
│   │   ├── utils/
│   │   │   ├── DriverManager.java
│   │   │   └── TestDataReader.java
│   │   └── config/
│   │       └── ConfigReader.java
│   └── test/
│       ├── java/com/portafolio/
│       │   ├── runner/
│       │   │   └── TestRunner.java
│       │   ├── steps/
│       │   │   ├── LoginSteps.java
│       │   │   ├── TransferSteps.java
│       │   │   └── Hooks.java
│       │   └── utils/
│       └── resources/
│           ├── features/
│           │   ├── login.feature
│           │   └── transfer.feature
│           └── config.properties
└── README.md
La aplicación a testear:

Puedes usar SauceDemo (saucedemo.com) o The Internet (the-internet.herokuapp.com) como aplicaciones públicas de práctica, ya que son reconocidas por reclutadores en el mundo QA