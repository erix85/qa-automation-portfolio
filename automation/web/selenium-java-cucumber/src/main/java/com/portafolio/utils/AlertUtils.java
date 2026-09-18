package com.portafolio.utils;

import java.time.Duration;

import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilidad centralizada para manejar alerts de JavaScript.
 *
 * <p><b>Responsabilidad única:</b> gestionar diálogos nativos del navegador.
 * No conoce a Cucumber ni a los Page Objects.</p>
 *
 * @author Erick
 */
public final class AlertUtils {

    private static final Logger log = LoggerFactory.getLogger(AlertUtils.class);
    private static final int DEFAULT_TIMEOUT = Integer.parseInt(
            System.getProperty("alertTimeout", "10"));

    private AlertUtils() {
        // Clase de utilidad, no instanciable
    }

    /**
     * Espera hasta que aparezca un alert y devuelve su texto.
     */
    public static String getAlertText() {
        log.debug("⏳ Esperando alert...");
        try {
            Alert alert = new WebDriverWait(DriverManager.getDriver(),
                    Duration.ofSeconds(DEFAULT_TIMEOUT))
                    .until(ExpectedConditions.alertIsPresent());
            String text = alert.getText();
            log.info("📢 Alert detectado: '{}'", text);
            return text;
        } catch (Exception e) {
            log.error("❌ No se detectó ningún alert en {} segundos", DEFAULT_TIMEOUT);
            throw e;
        }
    }

    /**
     * Acepta el alert (equivalente a hacer click en "OK").
     */
    public static void acceptAlert() {
        log.debug("🖱️ Aceptando alert...");
        Alert alert = DriverManager.getDriver().switchTo().alert();
        alert.accept();
        log.info("✅ Alert aceptado");
    }

    /**
     * Cancela el alert (equivalente a hacer click en "Cancel").
     */
    public static void dismissAlert() {
        log.debug("🖱️ Cancelando alert...");
        Alert alert = DriverManager.getDriver().switchTo().alert();
        alert.dismiss();
        log.info("✅ Alert cancelado");
    }

    /**
     * Verifica si hay un alert presente (sin lanzar excepción).
     */
    public static boolean isAlertPresent() {
        try {
            DriverManager.getDriver().switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }
}