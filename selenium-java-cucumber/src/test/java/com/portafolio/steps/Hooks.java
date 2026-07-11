package com.portafolio.steps;

import com.portafolio.utils.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Hooks {

    @Before
    public void setUp() {
        // El driver se inicializa automáticamente en DriverManager
        System.out.println("=== Iniciando prueba ===");
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            try {
                // Tomar screenshot si falla
                byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver())
                        .getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "screenshot_failure");
            } catch (Exception e) {
                System.err.println("Error al tomar screenshot: " + e.getMessage());
            }
        }
        
        // Cerrar el navegador después de cada escenario
        DriverManager.quitDriver();
        System.out.println("=== Prueba finalizada ===");
    }
}