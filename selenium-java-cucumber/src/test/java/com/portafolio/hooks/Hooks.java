package com.portafolio.hooks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.portafolio.utils.DriverManager;
import com.portafolio.utils.ScreenshotUtils;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;

/**
 * Hooks de Cucumber: se ejecutan antes y después de cada escenario.
 *
 * <p><b>Responsabilidad única:</b> orquestar el ciclo de vida del escenario.
 * Es el único punto de contacto entre el framework de testing (Cucumber)
 * y las utilidades agnósticas ({@link DriverManager}, {@link ScreenshotUtils}).</p>
 *
 * <p><b>Integración con Allure:</b> en caso de fallo, se adjunta el screenshot
 * tanto a Allure como al reporte HTML de Cucumber, y se guarda en disco.</p>
 *
 * @author Erick
 */
public class Hooks {

    private static final Logger log = LoggerFactory.getLogger(Hooks.class);

    // ═══════════════════ BEFORE ═══════════════════

    @Before(order = 0)
    public void setUp(Scenario scenario) {
        log.info("════════════════════════════════════════════════");
        log.info("▶ INICIANDO ESCENARIO: {}", scenario.getName());
        log.info("🏷  Tags: {}", scenario.getSourceTagNames());
        log.info("════════════════════════════════════════════════");

        // Inicializa el driver bajo demanda para este hilo
        DriverManager.getDriver();
    }

    // ═══════════════════ AFTER ═══════════════════

    @After(order = 0)
    public void tearDown(Scenario scenario) {
        try {
            if (scenario.isFailed()) {
                handleFailure(scenario);
            } else {
                log.info("✅ ESCENARIO COMPLETADO: {}", scenario.getName());
            }
        } finally {
            // Siempre cerramos el driver, aunque falle algo arriba
            DriverManager.quitDriver();
            log.info("🔚 Driver cerrado. Fin del escenario.\n");
        }
    }

    // ═══════════════════ MÉTODOS PRIVADOS ═══════════════════

    /**
     * Maneja un escenario fallido:
     * <ul>
     *   <li>Adjunta el screenshot a Allure.</li>
     *   <li>Adjunta el screenshot al reporte HTML/JSON de Cucumber.</li>
     *   <li>Guarda una copia en disco para CI/CD.</li>
     * </ul>
     */
    private void handleFailure(Scenario scenario) {
        log.error("❌ ESCENARIO FALLIDO: {}", scenario.getName());

        // Captura única de la pantalla
        byte[] screenshotBytes = ScreenshotUtils.captureAsBytes();

        if (screenshotBytes != null && screenshotBytes.length > 0) {
            // 1) Adjuntar a Allure
            Allure.getLifecycle().addAttachment(
                    "Screenshot en fallo",
                    "image/png",
                    "png",
                    screenshotBytes);

            // 2) Adjuntar al reporte de Cucumber
            scenario.attach(screenshotBytes, "image/png", "Screenshot en fallo");

            log.info("📸 Screenshot adjuntado a Allure y Cucumber ({} bytes)", screenshotBytes.length);
        } else {
            log.warn("⚠️ No se pudo capturar screenshot en el fallo.");
        }

        // 3) Guardar copia en disco como artefacto persistente
        ScreenshotUtils.captureToFile(scenario.getName());
    }
}