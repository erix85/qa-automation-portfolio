# 🚀 QA Automation Framework - Selenium + Cucumber

[![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)](https://openjdk.org/)
[![Selenium](https://img.shields.io/badge/Selenium-4.20-green?logo=selenium)](https://www.selenium.dev/)
[![Cucumber](https://img.shields.io/badge/Cucumber-7.15-brightgreen?logo=cucumber)](https://cucumber.io/)
[![TestNG](https://img.shields.io/badge/TestNG-7.10-red)](https://testng.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9-blue?logo=apachemaven)](https://maven.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-Selenium_Grid-2496ED?logo=docker)](https://www.docker.com/)

Framework de automatización de pruebas end-to-end con **Java 17**, **Selenium 4**, **Cucumber 7** y **TestNG**, aplicando arquitectura por capas, patrones de diseño profesionales y soporte dual para ejecución **local** y **distribuida (Selenium Grid + Docker)**.

---

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Tecnologías](#-tecnologías)
- [Arquitectura](#-arquitectura)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Instalación](#-instalación)
- [Ejecución de Tests](#-ejecución-de-tests)
- [Selenium Grid](#-selenium-grid-ejecución-distribuida)
- [Reportes](#-reportes)
- [Convenciones](#-convenciones)
- [Roadmap](#-roadmap)
- [Autor](#-autor)

---

## ✨ Características

- ✅ **BDD con Cucumber 7** — Escenarios en Gherkin legibles por negocio
- ✅ **Page Object Model** — Separación entre locators y comportamiento
- ✅ **DTOs tipados** — Datos de prueba tipados con Jackson
- ✅ **Configuración centralizada** — Entornos via `qa.properties`
- ✅ **Esperas explícitas** — `WaitUtils` sin esperas implícitas
- ✅ **Ejecución paralela** — `ThreadLocal` + `@DataProvider(parallel = true)`
- ✅ **Modo dual** — Local (WebDriverManager) y Grid (Docker)
- ✅ **Persistencia runtime** — Usuarios generados entre features
- ✅ **Reportes profesionales** — Allure + Cucumber HTML/JSON/JUnit
- ✅ **Logging estructurado** — SLF4J + Logback
- ✅ **Cross-browser** — Chrome, Firefox, Edge
- ✅ **CI/CD ready** — Preparado para GitHub Actions

---

## 🛠️ Tecnologías

| Tecnología | Versión | Uso |
|------------|---------|-----|
| Java | 17 | Lenguaje base |
| Selenium WebDriver | 4.20.0 | Automatización de navegador |
| Cucumber | 7.15.0 | BDD framework |
| TestNG | 7.10.2 | Runner de tests |
| Maven | 3.9.x | Gestión de dependencias |
| WebDriverManager | 5.8.0 | Auto-descarga de drivers |
| Allure | 2.29.1 | Reportes interactivos |
| Logback | 1.5.6 | Logging |
| Jackson | 2.17.1 | Serialización JSON |
| Docker | 29.x | Contenedores |
| Selenium Grid | 4.20.0 | Ejecución distribuida |

---

## 🏗️ Arquitectura

### Capas del Framework

```
┌──────────────────────────────────────────────────────────────┐
│  🎬 CAPA DE ORQUESTACIÓN (src/test)                          │
│  ├─ features/       → Escenarios Gherkin                     │
│  ├─ steps/          → Step Definitions                       │
│  ├─ hooks/          → @Before / @After                       │
│  └─ runner/         → TestRunner (Cucumber + TestNG)         │
└──────────────────────────────────────────────────────────────┘
                            ▼
┌──────────────────────────────────────────────────────────────┐
│  🎭 CAPA DE MODELO DE NEGOCIO (src/main)                     │
│  ├─ pages/          → Page Objects (BasePage, HomePage, ...) │
│  └─ locators/       → Locators desacoplados por página       │
└──────────────────────────────────────────────────────────────┘
                            ▼
┌──────────────────────────────────────────────────────────────┐
│  📦 CAPA DE DATOS                                            │
│  ├─ models/         → DTOs (User, Product)                   │
│  └─ testdata/       → JSON, CSV                              │
└──────────────────────────────────────────────────────────────┘
                            ▼
┌──────────────────────────────────────────────────────────────┐
│  🔧 CAPA DE INFRAESTRUCTURA                                  │
│  ├─ DriverManager        → ThreadLocal + Local/Grid          │
│  ├─ WaitUtils            → Esperas explícitas                │
│  ├─ AlertUtils           → Manejo de alerts                  │
│  ├─ ScreenshotUtils      → Capturas de pantalla              │
│  ├─ ConfigReader         → Lectura de configuración          │
│  ├─ GeneratedUserStore   → Persistencia runtime              │
│  └─ TestDataReader       → Lectura de datos de prueba        │
└──────────────────────────────────────────────────────────────┘
                            ▼
┌──────────────────────────────────────────────────────────────┐
│  🌐 CAPA DE EJECUCIÓN                                        │
│  ├─ Local: WebDriverManager → Chrome/Firefox/Edge            │
│  └─ Grid:  RemoteWebDriver  → Hub + Nodos (Docker)           │
└──────────────────────────────────────────────────────────────┘
                            ▼
┌──────────────────────────────────────────────────────────────┐
│  📊 CAPA DE REPORTES                                         │
│  ├─ Allure Report                                            │
│  ├─ Cucumber HTML/JSON/JUnit                                 │
│  └─ Logback Logs                                             │
└──────────────────────────────────────────────────────────────┘
```

---

## 📁 Estructura del Proyecto

```
selenium-java-cucumber/
│
├── docker-compose.yml                  → Selenium Grid 4 (Hub + 3 Nodos)
├── pom.xml                             → Dependencias Maven
├── testng.xml                          → Suite TestNG
├── README.md                           → Este archivo
│
├── src/
│   ├── main/
│   │   └── java/com/portafolio/
│   │       ├── locators/               → Locators por página
│   │       │   ├── HomeLocators.java
│   │       │   ├── LoginLocators.java
│   │       │   └── SignupLocators.java
│   │       │
│   │       ├── models/                 → DTOs
│   │       │   └── User.java
│   │       │
│   │       ├── pages/                  → Page Objects
│   │       │   ├── BasePage.java
│   │       │   ├── HomePage.java
│   │       │   ├── LoginPage.java
│   │       │   └── SignupPage.java
│   │       │
│   │       └── utils/                  → Utilidades transversales
│   │           ├── AlertUtils.java
│   │           ├── ConfigReader.java
│   │           ├── DriverManager.java
│   │           ├── GeneratedUserStore.java
│   │           ├── ScreenshotUtils.java
│   │           ├── TestDataReader.java
│   │           └── WaitUtils.java
│   │
│   └── test/
│       ├── java/com/portafolio/
│       │   ├── hooks/                  → Hooks de Cucumber
│       │   │   └── Hooks.java
│       │   │
│       │   ├── runner/                 → TestRunner
│       │   │   └── TestRunner.java
│       │   │
│       │   └── steps/                  → Step Definitions
│       │       ├── CommonSteps.java
│       │       ├── LoginSteps.java
│       │       └── SignupSteps.java
│       │
│       └── resources/
│           ├── config/                 → Configuración por entorno
│           │   └── qa.properties
│           │
│           ├── features/               → Escenarios Gherkin
│           │   └── auth/
│           │       ├── login.feature
│           │       └── signup.feature
│           │
│           ├── testdata/               → Datos de prueba estáticos
│           │   └── users.json
│           │
│           ├── allure.properties
│           └── logback.xml
│
└── target/                             → Generado por Maven (gitignored)
    ├── generated-users.json            → Usuarios creados en runtime
    ├── cucumber-reports/
    ├── allure-results/
    └── screenshots/
```

---

## 🚀 Instalación

### Pre-requisitos

- **Java 17** o superior → `java -version`
- **Maven 3.9+** → `mvn -version`
- **Docker Desktop** (opcional, para Selenium Grid) → `docker --version`

### Clonar e instalar

```bash
git clone https://github.com/erix85/qa-automation-portfolio.git
cd qa-automation-portfolio/selenium-java-cucumber
mvn clean install -DskipTests
```

---

## ▶️ Ejecución de Tests

### Modo Local (por defecto)

```bash
# Todos los tests
mvn clean test

# Solo smoke tests
mvn clean test "-Dcucumber.filter.tags=@smoke"

# Solo login
mvn clean test "-Dcucumber.filter.tags=@login"

# Chrome en modo headless
mvn clean test "-Dheadless=true"

# Firefox
mvn clean test "-Dbrowser=firefox"

# Edge
mvn clean test "-Dbrowser=edge"
```

> ⚠️ **PowerShell**: usar comillas alrededor de todo el argumento `-D`.
> ```powershell
> mvn clean test "-Dcucumber.filter.tags=@smoke"
> ```

### Modo Grid (ver sección siguiente)

```bash
mvn clean test "-Dgrid.url=http://localhost:4444"
```

---

## 🌐 Selenium Grid (Ejecución Distribuida)

El framework soporta **dos modos de ejecución** con la misma base de código:

| Modo | Cuándo usarlo | Cómo activarlo |
|------|---------------|----------------|
| 🖥️ **Local** | Desarrollo, debugging | Por defecto (sin `-Dgrid.url`) |
| 🌐 **Grid** | Paralelismo, CI/CD, cross-browser | `-Dgrid.url=http://localhost:4444` |

### Arquitectura del Grid

```
┌─────────────────────────────────────┐
│       Selenium Hub (4444)           │
└──────────┬──────────┬───────────────┘
           │          │
     ┌─────▼───┐  ┌───▼─────┐  ┌────────┐
     │ Chrome  │  │ Firefox │  │  Edge  │
     │  Node   │  │  Node   │  │  Node  │
     │ (2 ses) │  │ (2 ses) │  │ (2 ses)│
     └─────────┘  └─────────┘  └────────┘
```

**Total**: hasta **6 sesiones concurrentes**.

### 🔧 Comandos del Grid

```bash
# Levantar el Grid (primera vez descarga ~4 GB de imágenes)
docker-compose up -d

# Ver estado de los contenedores
docker-compose ps

# Ver logs en tiempo real
docker-compose logs -f

# Abrir la UI del Grid
start http://localhost:4444/ui

# Apagar el Grid
docker-compose down

# Apagar y limpiar todo
docker-compose down -v

# Actualizar imágenes a la última versión
docker-compose pull
```

### 🎯 Ejecutar tests contra el Grid

```bash
# Chrome
mvn clean test "-Dgrid.url=http://localhost:4444" "-Dbrowser=chrome"

# Firefox
mvn clean test "-Dgrid.url=http://localhost:4444" "-Dbrowser=firefox"

# Edge
mvn clean test "-Dgrid.url=http://localhost:4444" "-Dbrowser=edge"

# Todos los smoke tests en Chrome headless
mvn clean test "-Dgrid.url=http://localhost:4444" "-Dheadless=true" "-Dcucumber.filter.tags=@smoke"
```

### 🖥️ Ver los navegadores en vivo (VNC)

Cada nodo expone VNC. Para ver el navegador en acción:

```bash
# Chrome Node (puerto 7900)
start vnc://localhost:7900

# El password está vacío (configurado con SE_VNC_NO_PASSWORD=1)
```

O usando cualquier cliente VNC (RealVNC, TigerVNC, etc.).

---

## 📊 Reportes

### Allure Report (recomendado)

```bash
# Ejecutar tests y generar reporte
mvn clean test
mvn allure:serve
```

Se abrirá automáticamente en el navegador con:
- 📊 Vista general con gráficos
- 🎯 Escenarios agrupados por Epic/Feature
- 📸 Screenshots adjuntos en fallos
- 📎 Datos de prueba adjuntos

### Cucumber Reports

- **HTML**: `target/cucumber-reports/cucumber.html`
- **JSON**: `target/cucumber-reports/cucumber.json`
- **JUnit XML**: `target/cucumber-reports/cucumber.xml`
- **Enriquecido** (tras `mvn verify`): `target/cucumber-reports/overview-features.html`

---

## 📐 Convenciones

### Nomenclatura de Steps

Para evitar `Undefined step`, seguir estas convenciones:

| ✅ Correcto | ❌ Incorrecto |
|------------|--------------|
| `que el usuario está en la página principal` | `el usuario está en la página principal` |
| `hace click en el enlace {string}` | `click en {string}` |
| `debería ver el mensaje de bienvenida {string}` | `ver mensaje {string}` |

**Regla**: usar siempre la misma frase para el mismo step. Los textos deben coincidir **exactamente** entre `.feature` y `@Dado/@Cuando/@Entonces`.

### Mensajes de DemoBlaze (confirmados)

| Escenario | Mensaje exacto |
|-----------|----------------|
| Signup exitoso | `Sign up successful.` |
| Signup duplicado | `This user already exist.` |
| Login usuario inexistente | `User does not exist.` |
| Login contraseña incorrecta | `Wrong password.` |

### Estructura de un Step

```java
@Cuando("el usuario recién creado inicia sesión")
@Story("Ingreso con usuario generado")
@Severity(SeverityLevel.CRITICAL)
public void elUsuarioRecienCreadoIniciaSesion() {
    log.info("🎬 When: login con el usuario recién creado");
    
    User lastUser = GeneratedUserStore.getLastCreated();
    Assert.assertNotNull(lastUser, "No hay usuarios generados");
    
    Allure.step("Ingresar usuario: " + lastUser.getUsername(), () -> {
        loginPage.enterUsername(lastUser.getUsername());
    });
    
    Allure.step("Ingresar contraseña", () -> {
        loginPage.enterPassword(lastUser.getPassword());
    });
    
    Allure.step("Hacer click en el botón Log in", () -> {
        loginPage.clickLogin();
    });
}
```

### Comandos en PowerShell

En PowerShell, los `-D` con puntos deben ir entre comillas:

```powershell
# ✅ Correcto
mvn clean test "-Dcucumber.filter.tags=@smoke"

# ❌ Incorrecto (PowerShell interpreta mal)
mvn clean test -Dcucumber.filter.tags=@smoke
```

Alternativa: usar el stop-parsing symbol:

```powershell
mvn --% clean test -Dcucumber.filter.tags=@smoke
```

---

## 🗺️ Roadmap

### ✅ Completado

- [x] Arquitectura por capas (main/test)
- [x] Page Object Model con BasePage
- [x] Locators desacoplados
- [x] Steps separados (Common/Login/Signup)
- [x] DTOs tipados con Jackson
- [x] Configuración centralizada (`ConfigReader`)
- [x] Esperas explícitas (`WaitUtils`)
- [x] Ciclo de vida con `@Before`/`@After`
- [x] Ejecución paralela con `ThreadLocal`
- [x] Reportes Allure + Cucumber
- [x] Persistencia runtime de usuarios
- [x] Feature de signup (positivo + negativo)
- [x] Feature de login (positivo + 2 negativos)
- [x] **Selenium Grid + Docker Compose**
- [x] **Modo dual: local + Grid**
- [x] **Cross-browser (Chrome, Firefox, Edge)**

### 🚧 En progreso

- [ ] Feature de multi-ventana
- [ ] Feature de alerts extendidos
- [ ] Feature de multi-navegador parametrizado

### 📅 Futuro

- [ ] CI/CD con GitHub Actions
- [ ] Visual Regression Testing
- [ ] Ejecución con Selenium Grid en CI
- [ ] Dockerización del propio framework
- [ ] Reporte consolidado multi-navegador

---

## 🎯 Casos de Uso Demostrados

| Feature | Técnica demostrada |
|---------|-------------------|
| `signup.feature` | BDD, POM, DTOs, Alerts, Persistencia runtime |
| `login.feature` | Background, reutilización entre features, paralelismo |
| `docker-compose.yml` | Selenium Grid 4, Docker, cross-browser |
| Ejecución `-Dgrid.url` | Modo dual (local/remoto) |

---

## 👤 Autor

**Erick** — QA Automation Engineer

- 🐙 GitHub: [@erix85](https://github.com/erix85)
- 📁 Portfolio: [qa-automation-portfolio](https://github.com/erix85/qa-automation-portfolio)

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver `LICENSE` para más información.

---

## 🙏 Agradecimientos

- [Selenium](https://www.selenium.dev/) por el framework de automatización
- [Cucumber](https://cucumber.io/) por el framework BDD
- [Allure](https://qameta.io/allure/) por los reportes
- [WebDriverManager](https://github.com/bonigarcia/webdrivermanager) por la gestión de drivers
- [DemoBlaze](https://www.demoblaze.com/) por el sitio de pruebas

---

**⭐ Si este proyecto te resultó útil, considera darle una estrella en GitHub.**