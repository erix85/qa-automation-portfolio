package com.portafolio.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilidad para la captura de pantalla del navegador.
 *
 * <p><b>Principio de diseño:</b> esta clase NO conoce a Cucumber ni a ningún
 * framework de testing. Devuelve los bytes de la imagen y, opcionalmente,
 * permite guardarla en disco. El llamador decide qué hacer con ellos
 * (adjuntarlos a un reporte, guardarlos como artefacto, enviarlos por correo, etc.).</p>
 *
 * @author Erick
 */
public final class ScreenshotUtils {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtils.class);
    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtils() {
        // Clase de utilidad, no instanciable
    }

    /**
     * Captura la pantalla actual del navegador en formato PNG.
     *
     * @return bytes de la imagen PNG, o {@code null} si no se pudo capturar
     */
    public static byte[] captureAsBytes() {
        if (!DriverManager.hasDriver()) {
            log.warn("No hay driver inicializado para el hilo actual; no se puede capturar pantalla.");
            return null;
        }

        WebDriver driver = DriverManager.getDriver();
        if (driver instanceof TakesScreenshot takesScreenshot) {
            try {
                byte[] screenshot = takesScreenshot.getScreenshotAs(OutputType.BYTES);
                log.debug("📸 Screenshot capturado ({} bytes)", screenshot.length);
                return screenshot;
            } catch (Exception e) {
                log.error("Error al capturar la pantalla: {}", e.getMessage());
            }
        }
        return null;
    }

    /**
     * Captura la pantalla y la guarda en disco dentro de {@code target/screenshots/}.
     *
     * @param prefix prefijo del nombre del archivo (ej. nombre del escenario)
     * @return la ruta del archivo guardado, o {@code null} si falló
     */
    public static Path captureToFile(String prefix) {
        byte[] screenshot = captureAsBytes();
        if (screenshot == null) {
            return null;
        }

        String sanitizedPrefix = sanitize(prefix);
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String fileName = sanitizedPrefix + "_" + timestamp + ".png";

        Path screenshotsDir = Paths.get("target", "screenshots");
        Path filePath = screenshotsDir.resolve(fileName);

        try {
            Files.createDirectories(screenshotsDir);
            Files.write(filePath, screenshot);
            log.info("💾 Screenshot guardado en: {}", filePath.toAbsolutePath());
            return filePath;
        } catch (IOException e) {
            log.error("No se pudo guardar el screenshot: {}", e.getMessage());
            return null;
        }
    }

    private static String sanitize(String input) {
        if (input == null || input.isBlank()) {
            return "screenshot";
        }
        return input.replaceAll("[^a-zA-Z0-9-_]", "_");
    }
}