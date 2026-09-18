package com.portafolio.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Runner principal de Cucumber con TestNG.
 * Ejecuta los escenarios definidos en src/test/resources/features.
 *
 * <p><b>Reportes generados:</b></p>
 * <ul>
 *   <li>{@code target/cucumber-reports/cucumber.html} — Reporte HTML legible</li>
 *   <li>{@code target/cucumber-reports/cucumber.json} — Para integraciones CI/CD</li>
 *   <li>{@code target/cucumber-reports/cucumber.xml} — JUnit XML (Jenkins, Azure DevOps)</li>
 *   <li>{@code target/cucumber-reports/rerun.txt} — Re-ejecución de fallidos</li>
 *   <li>{@code target/allure-results/} — Resultados crudos para Allure</li>
 * </ul>
 *
 * @author Erick
 */
@CucumberOptions(
    features = "classpath:features",
    glue = {"com.portafolio.steps", "com.portafolio.hooks"},
    tags = "not @ignore",
    plugin = {
        "pretty",
        "html:target/cucumber-reports/cucumber.html",
        "json:target/cucumber-reports/cucumber.json",
        "junit:target/cucumber-reports/cucumber.xml",
        "rerun:target/cucumber-reports/rerun.txt",
        "timeline:target/cucumber-reports/timeline",
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"   // ⭐ Plugin Allure
    },
    monochrome = true,
    publish = false
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}