package com.portafolio.utils;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilidad centralizada para esperas explícitas.
 *
 * <p><b>Filosofía:</b> las esperas explícitas son más confiables que las
 * implícitas porque esperan una <i>condición específica</i> en lugar de un
 * tiempo fijo. Esta clase encapsula toda la lógica de {@link WebDriverWait}
 * y {@link ExpectedConditions}.</p>
 *
 * <p><b>Principio de diseño:</b> agnóstica al framework de testing. No conoce
 * a Cucumber ni a JUnit. Solo depende de Selenium.</p>
 *
 * <p>El timeout se configura con la propiedad del sistema
 * {@code -DexplicitWait=15} (default: 15 segundos).</p>
 *
 * @author Erick
 */
public final class WaitUtils {

    private static final Logger log = LoggerFactory.getLogger(WaitUtils.class);

    private static final int DEFAULT_TIMEOUT = Integer.parseInt(
            System.getProperty("explicitWait", "15"));
    private static final int POLLING_INTERVAL_MS = 300;

    private WaitUtils() {
        // Clase de utilidad, no instanciable
    }

    /**
     * Crea una instancia de {@link WebDriverWait} con el timeout configurado.
     * Se crea uno nuevo por llamada para evitar problemas de estado compartido
     * entre hilos en ejecución paralela.
     */
    private static WebDriverWait newWait() {
        WebDriverWait wait = new WebDriverWait(
                DriverManager.getDriver(),
                Duration.ofSeconds(DEFAULT_TIMEOUT));
        wait.pollingEvery(Duration.ofMillis(POLLING_INTERVAL_MS));
        wait.ignoring(NoSuchElementException.class);
        wait.ignoring(StaleElementReferenceException.class);
        return wait;
    }

    // ═══════════════════ ESPERAS POR VISIBILIDAD ═══════════════════

    /**
     * Espera hasta que un elemento sea visible en el DOM.
     */
    public static WebElement waitForVisibility(By locator) {
        log.debug("⏳ Esperando visibilidad de: {}", locator);
        long start = System.currentTimeMillis();
        try {
            WebElement element = newWait().until(
                    ExpectedConditions.visibilityOfElementLocated(locator));
            log.debug("✅ Elemento visible en {} ms: {}",
                    System.currentTimeMillis() - start, locator);
            return element;
        } catch (Exception e) {
            log.error("❌ Timeout esperando visibilidad de: {} ({} ms)",
                    locator, System.currentTimeMillis() - start);
            throw e;
        }
    }

    /**
     * Espera hasta que un elemento sea clickeable (visible + habilitado).
     *
     * <p><b>Uso recomendado:</b> para escribir en inputs de modales que pueden
     * estar animándose. Es más robusto que {@link #waitForVisibility(By)}.</p>
     */
    public static WebElement waitForClickability(By locator) {
        log.debug("⏳ Esperando que sea clickeable: {}", locator);
        long start = System.currentTimeMillis();
        try {
            WebElement element = newWait().until(
                    ExpectedConditions.elementToBeClickable(locator));
            log.debug("✅ Elemento clickeable en {} ms: {}",
                    System.currentTimeMillis() - start, locator);
            return element;
        } catch (Exception e) {
            log.error("❌ Timeout esperando clickabilidad de: {} ({} ms)",
                    locator, System.currentTimeMillis() - start);
            throw e;
        }
    }

    /**
     * Alias semántico de {@link #waitForClickability(By)}.
     */
    public static WebElement waitForInteractability(By locator) {
        return waitForClickability(locator);
    }

    // ═══════════════════ ESPERAS POR INVISIBILIDAD ═══════════════════

    /**
     * Espera hasta que un elemento sea invisible o desaparezca del DOM.
     */
    public static boolean waitForInvisibility(By locator) {
        log.debug("⏳ Esperando invisibilidad de: {}", locator);
        try {
            return newWait().until(
                    ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (Exception e) {
            log.error("❌ Timeout esperando invisibilidad de: {}", locator);
            throw e;
        }
    }

    // ═══════════════════ ESPERAS POR TEXTO ═══════════════════

    /**
     * Espera hasta que el texto de un elemento sea exactamente el esperado.
     */
    public static boolean waitForTextToBe(By locator, String expectedText) {
        log.debug("⏳ Esperando texto '{}' en: {}", expectedText, locator);
        return newWait().until(
                ExpectedConditions.textToBe(locator, expectedText));
    }

    /**
     * Espera hasta que el texto de un elemento contenga el valor esperado.
     */
    public static boolean waitForTextToContain(By locator, String expectedPartialText) {
        log.debug("⏳ Esperando que el texto contenga '{}' en: {}",
                expectedPartialText, locator);
        return newWait().until(
                ExpectedConditions.textToBePresentInElementLocated(locator, expectedPartialText));
    }

    // ═══════════════════ ESPERAS POR URL ═══════════════════

    /**
     * Espera hasta que la URL actual contenga un fragmento específico.
     */
    public static boolean waitForUrlContains(String urlFragment) {
        log.debug("⏳ Esperando que la URL contenga: '{}'", urlFragment);
        long start = System.currentTimeMillis();
        try {
            boolean result = newWait().until(
                    ExpectedConditions.urlContains(urlFragment));
            log.debug("✅ URL contiene '{}' en {} ms",
                    urlFragment, System.currentTimeMillis() - start);
            return result;
        } catch (Exception e) {
            log.error("❌ Timeout esperando URL con '{}'. URL actual: {}",
                    urlFragment, DriverManager.getDriver().getCurrentUrl());
            throw e;
        }
    }

    // ═══════════════════ ESPERAS POR ESTADO ═══════════════════

    /**
     * Espera hasta que un elemento tenga el atributo con el valor especificado.
     */
    public static boolean waitForAttributeToBe(By locator, String attribute, String value) {
        log.debug("⏳ Esperando atributo '{}' = '{}' en: {}", attribute, value, locator);
        return newWait().until(
                ExpectedConditions.attributeToBe(locator, attribute, value));
    }

    /**
     * Espera hasta que un input tenga un valor no vacío en el atributo
     * {@code value}.
     */
    public static boolean waitForInputToHaveValue(By locator) {
        log.debug("⏳ Esperando que el input tenga valor: {}", locator);
        return newWait().until(driver -> {
            try {
                WebElement element = driver.findElement(locator);
                String value = element.getAttribute("value");
                return value != null && !value.trim().isEmpty();
            } catch (Exception e) {
                return false;
            }
        });
    }

    /**
     * Espera hasta que un elemento sea visible y esté habilitado.
     */
    public static WebElement waitForEnabled(By locator) {
        log.debug("⏳ Esperando que el elemento esté habilitado: {}", locator);
        return newWait().until(driver -> {
            WebElement element = driver.findElement(locator);
            return (element.isDisplayed() && element.isEnabled()) ? element : null;
        });
    }

    // ═══════════════════ ESPERAS PERSONALIZADAS ═══════════════════

    /**
     * Espera hasta que una condición personalizada se cumpla.
     */
    public static <T> T waitForCondition(ExpectedCondition<T> condition) {
        return newWait().until(condition);
    }

    /**
     * Pausa breve para casos específicos donde las esperas explícitas no son
     * suficientes (por ejemplo, animaciones CSS que no cambian el DOM).
     *
     * <p><b>Advertencia:</b> usar con moderación. Solo para casos justificados.
     * Preferir siempre {@link #waitForCondition} cuando sea posible.</p>
     *
     * @param millis milisegundos a esperar
     */
    public static void pause(long millis) {
        try {
            log.debug("⏸️ Pausa controlada de {} ms", millis);
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Pausa interrumpida: {}", e.getMessage());
        }
    }
}