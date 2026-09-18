# QA Automation Portfolio - Erick Cuevas

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-blue)](https://linkedin.com/in/tu-perfil)
[![GitHub](https://img.shields.io/badge/GitHub-Follow-black)](https://github.com/erix85)
[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.java.com/)
[![Python](https://img.shields.io/badge/Python-3.12-blue)](https://www.python.org/)
[![Selenium](https://img.shields.io/badge/Selenium-4.20.0-green)](https://www.selenium.dev/)
[![Cucumber](https://img.shields.io/badge/Cucumber-7.15.0-brightgreen)](https://cucumber.io/)
[![Appium](https://img.shields.io/badge/Appium-2.x-purple)](https://appium.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-lightgrey)](LICENSE)

## Sobre Mi

Ingeniero QA con 7 anos de experiencia en aseguramiento de calidad de software, especializado en el sector bancario y financiero. He trabajado en proyectos criticos para BancoEstado, Banco Chile, Scotiabank, BCI y Falabella.

### Logros Destacados

- +30% de eficiencia en pruebas de regresion (Falabella)
- Certificacion del portal CasaVerso (BancoEstado)
- Migracion Cloud On-premises a Cloud (Falabella Colombia)
- Implementacion de frameworks en Java, Python, Robot Framework

### Stack Tecnologico

| Categoria | Tecnologias |
|-----------|-------------|
| Lenguajes | Java 17, Python, SQL, Kotlin (proximo), Swift (proximo) |
| Automatizacion Web | Selenium WebDriver, Cucumber, Behave, Robot Framework |
| Automatizacion Mobile | Appium (Android + iOS) |
| Pruebas de API | Postman, Newman, REST Assured (proximo), SOAP UI |
| Pruebas de Performance | JMeter, Locust |
| Pruebas de Seguridad | Fortify, BlackDuck |
| Bases de Datos | PostgreSQL 16, MySQL, H2 |
| CI/CD | Jenkins, GitHub Actions |
| Contenedores | Docker, Docker Compose |
| IA aplicada a QA | Test Generation, Self-Healing, Visual Diff (proximo) |
| Gestion | Jira, Confluence, ALM |

## Estructura del Portafolio

Este portfolio agrupa multiples proyectos especializados organizados por dominio funcional. Cada subproyecto es autonomo: tiene su propio README, dependencias, CI/CD y documentacion.
qa-automation-portfolio/
|
|-- automation/ # Frameworks de automatizacion
| |-- web/
| | |-- selenium-java-cucumber/ # BDD con Selenium + Java + Cucumber
| |-- mobile/
| | |-- appium/ # Mobile testing con Appium
| |-- api/
| | |-- postman/ # Colecciones Postman + Newman
| | |-- rest-assured/ # API testing con REST Assured
| |-- bdd/
| | |-- python-behave/ # BDD con Python + Behave
| | |-- robot-framework/ # BDD con Robot Framework
| |-- performance/
| |-- jmeter/ # Performance testing con JMeter
|
|-- apps/ # Aplicaciones propias (SUT)
| |-- demo-api/ # API REST con Spring Boot
| |-- demo-android/ # App Android con Kotlin
| |-- demo-ios/ # App iOS con Swift
|
|-- database/ # Base de datos del sistema
| |-- migrations/ # Migraciones versionadas
| |-- seed-data/ # Datos iniciales para testing
| |-- scripts/ # Utilitarios (init, reset, backup)
| |-- docs/ # Diagrama ER y esquema
|
|-- ai/ # IA aplicada a QA
| |-- test-generator/ # Generacion de tests con LLM
| |-- self-healing/ # Locators auto-reparables
| |-- visual-diff/ # Deteccion de regresiones visuales
|
|-- shared/ # Recursos compartidos
| |-- assets/ # Imagenes y logos
| |-- configs/ # Configuraciones comunes
| |-- docs/ # Documentacion transversal
|
|-- docs/ # Documentacion del portfolio
| |-- bug-reports/ # Ejemplos de bug reports
| |-- test-cases/ # Ejemplos de casos de prueba
| |-- test-plans/ # Ejemplos de planes de prueba
|
|-- assets/ # Recursos visuales del portfolio
| |-- screenshots/ # Capturas de reportes
|
|-- .devcontainer/ # Configuracion de GitHub Codespaces
|-- .github/ # CI/CD workflows
|-- .gitignore
|-- README.md # Este archivo

text

## Proyectos Destacados

### automation/web/selenium-java-cucumber

Framework BDD end-to-end para testing de aplicaciones web.

| Aspecto | Detalle |
|---------|---------|
| Stack | Java 17, Selenium 4.20, Cucumber 7.15, TestNG, Maven |
| Patrones | Page Object Model, DTOs, Config Centralizada |
| Modos de ejecucion | Local, Selenium Grid, GitHub Codespaces |
| Reportes | Allure + Cucumber HTML/JSON/JUnit |
| CI/CD | GitHub Actions |
| Features | Signup, Login, Multi-ventana, Multi-navegador |

[Ver README completo](automation/web/selenium-java-cucumber/README.md)

### automation/mobile/appium

Framework cross-platform para testing mobile (Android + iOS).

| Aspecto | Detalle |
|---------|---------|
| Stack | Appium 2.x, Java 17, Cucumber, TestNG |
| Plataformas | Android, iOS |
| Cloud | BrowserStack, Sauce Labs |
| Reportes | Allure |

[Ver README completo](automation/mobile/appium/README.md)

### automation/api/rest-assured

Framework de testing de APIs con validacion de contratos.

| Aspecto | Detalle |
|---------|---------|
| Stack | REST Assured 5.x, Java 17, TestNG |
| Validaciones | JSON Schema, Status Codes, Headers |
| Auth | JWT, OAuth2 |
| Reportes | Allure |

[Ver README completo](automation/api/rest-assured/README.md)

### automation/performance/jmeter

Planes de performance testing para APIs y servicios.

| Aspecto | Detalle |
|---------|---------|
| Stack | JMeter 5.x |
| Escenarios | Load, Stress, Spike |
| Reportes | HTML Dashboard |

[Ver README completo](automation/performance/jmeter/README.md)

### automation/bdd/python-behave

Framework BDD en Python con Behave + Selenium.

[Ver README completo](automation/bdd/python-behave/README.md)

### automation/bdd/robot-framework

Framework BDD con Robot Framework.

[Ver README completo](automation/bdd/robot-framework/README.md)

## Aplicaciones Propias (SUT)

Las aplicaciones propias sirven como System Under Test (SUT) para los frameworks de automatizacion.

### apps/demo-api

API REST construida con Spring Boot 3 + PostgreSQL.

Endpoints:
- POST /api/v1/users - Crear usuario
- POST /api/v1/auth/login - Login
- GET /api/v1/products - Listar productos
- POST /api/v1/transactions - Crear transaccion

[Ver README completo](apps/demo-api/README.md)

### apps/demo-android

App Android nativa con Kotlin + Jetpack Compose.

Pantallas:
- Login
- Lista de productos
- Detalle de producto
- Simulacion de transaccion

[Ver README completo](apps/demo-android/README.md)

### apps/demo-ios

App iOS nativa con Swift + SwiftUI.

[Ver README completo](apps/demo-ios/README.md)

## Base de Datos

### database/

Base de datos PostgreSQL 16 que alimenta al sistema completo.

| Componente | Descripcion |
|------------|-------------|
| Migrations | Scripts SQL versionados |
| Seed Data | Datos iniciales para testing |
| Scripts | Utilitarios (init, reset, backup) |
| Docs | Diagrama ER y esquema detallado |

Levantar la BD:
```bash
cd database
docker-compose up -d
Ver README completo

IA Aplicada a QA
Proyectos que integran inteligencia artificial en el ciclo de testing.

Proyecto	Descripcion
test-generator	Generacion de casos de prueba con LLMs
self-healing	Locators auto-reparables con IA
visual-diff	Deteccion de regresiones visuales con IA
Ver README completo

Inicio Rapido
Requisitos
Java 17+

Maven 3.9+

Docker Desktop (para bases de datos y Selenium Grid)

Node.js 20+ (para algunos frameworks)

Ejecucion con GitHub Codespaces (sin instalacion)
Abre este repositorio en GitHub.

Click en Code, luego Codespaces, luego Create codespace on main.

Espera a que se configure el entorno (~2 minutos).

Ejecuta:

bash
cd automation/web/selenium-java-cucumber
mvn clean test "-Dcucumber.filter.tags=@smoke"
Ejecucion local
bash
git clone https://github.com/erix85/qa-automation-portfolio.git
cd qa-automation-portfolio

cd automation/web/selenium-java-cucumber
mvn clean test "-Dcucumber.filter.tags=@smoke"
Reportes
Cada proyecto genera reportes Allure interactivos:

Proyecto	Comando	Reporte
Selenium	mvn allure:serve	target/allure-results/
Appium	mvn allure:serve	target/allure-results/
REST Assured	mvn allure:serve	target/allure-results/
Roadmap
Completado
☑ Estructura base del portfolio por dominios
☑ Framework Selenium + Java + Cucumber
☑ Configuracion de Selenium Grid (Docker + Standalone)
☑ Integracion con GitHub Codespaces
☑ Features: signup, login, multi-modo
☑ Estructura de directorios: database, shared, ai, apps
En Progreso
□ Base de datos PostgreSQL (database/)
□ API propia con Spring Boot (apps/demo-api)
□ Framework REST Assured (automation/api/rest-assured)
□ App Android con Kotlin (apps/demo-android)
□ Framework Appium (automation/mobile/appium)
Futuro
□ App iOS con Swift (apps/demo-ios)
□ Performance testing con JMeter
□ IA aplicada a QA (test-generator, self-healing, visual-diff)
□ CI/CD completo para todos los subproyectos
□ Narrativa publica en Google Sites
Metricas del Portfolio
Metrica	Valor
Subproyectos	15+
Lenguajes	Java, Python, Kotlin, Swift, SQL
Frameworks de automatizacion	6
Aplicaciones propias	3
Bases de datos	PostgreSQL, MySQL, H2
Modos de ejecucion	Local, Docker, Cloud, Codespaces
Contacto
LinkedIn: Erick Cuevas

GitHub: @erix85

Email: tu-email@ejemplo.com

Licencia
Este proyecto esta bajo la Licencia MIT. Ver LICENSE para mas informacion.

Agradecimientos
Selenium - Web automation

Cucumber - BDD framework

Appium - Mobile automation

REST Assured - API testing

Allure - Reporting

PostgreSQL - Database

Docker - Containerization

Si este portfolio te resulto util, considera darle una estrella en GitHub.