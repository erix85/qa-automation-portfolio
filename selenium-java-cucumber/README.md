# Java + Selenium + Cucumber - Framework BDD

## 📋 Descripción

Framework de automatización con **Java, Selenium y Cucumber** para pruebas funcionales en portales bancarios. Implementa el patrón **Page Object Model (POM)** y **Behavior Driven Development (BDD)**.

## 🎯 Funcionalidades de Ejemplo

- ✅ Login con diferentes tipos de usuarios
- ✅ Transferencias entre cuentas
- ✅ Consulta de saldos y productos
- ✅ Validación de mensajes de error
- ✅ Pruebas cross-browser

## 🛠️ Tecnologías

| Tecnología | Versión |
|------------|---------|
| Java | 17 |
| Selenium WebDriver | 4.20.0 |
| Cucumber | 7.15.0 |
| TestNG | 7.10.2 |
| Maven | 3.9.x |

## 📁 Estructura del Proyecto

selenium-java-cucumber/
├── pom.xml
├── src/
│ ├── main/
│ │ └── java/com/portafolio/
│ │ ├── pages/ # Page Object Model
│ │ └── utils/ # Helpers
│ └── test/
│ ├── java/com/portafolio/
│ │ ├── runner/ # Test Runner
│ │ └── steps/ # Step Definitions
│ └── resources/
│ └── features/ # Archivos .feature
└── testng.xml

text

## 🚀 Cómo Ejecutar

```bash
# Instalar dependencias
mvn clean install

# Ejecutar todas las pruebas
mvn test

# Ejecutar pruebas por tags
mvn test -Dcucumber.filter.tags="@SmokeTest"

# Ejecutar pruebas en modo headless
mvn test -Dheadless=true
📊 Reportes
Los reportes se generan en target/cucumber-reports/

🔗 Casos de Uso Relacionados
Proyecto Falabella - Migración Cloud

Proyecto Scotiabank

text

### 5. testng.xml

```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Portafolio QA - Banca y Finanzas">
    <test name="Pruebas Bancarias">
        <classes>
            <class name="com.portafolio.runner.TestRunner"/>
        </classes>
    </test>
</suite>