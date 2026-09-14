package com.portafolio.utils;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Gestor del WebDriver basado en ThreadLocal.
 * Permite ejecución paralela segura: cada hilo tiene su propia instancia de driver.
 *
 * <p><b>Principio de diseño:</b> esta clase es agnóstica al framework de testing.
 * No conoce a Cucumber, JUnit, TestNG ni a ningún runner. Solo gestiona el ciclo
 * de vida de {@link WebDriver}. Otras utilidades (ej. {@code ScreenshotUtils})
 * se encargan de la captura de pantalla.</p>
 *
 * <p>Configuración por propiedades del sistema:</p>
 * <ul>
 *   <li>{@code -Dbrowser=chrome|firefox|edge} (default: chrome)</li>
 *   <li>{@code -Dheadless=true|false} (default: false)</li>
 *   <li>{@code -DimplicitWait=10} (segundos)</li>
 *   <li>{@code -DpageLoadTimeout=30} (segundos)</li>
 * </ul>
 *
 * @author Erick
 */
public final class DriverManager {

    private static final Logger log = LoggerFactory.getLogger(DriverManager.class);

    private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    private static final String BROWSER = System.getProperty("browser", "chrome").toLowerCase();
    private static final boolean HEADLESS = Boolean.parseBoolean(System.getProperty("headless", "false"));
    private static final int IMPLICIT_WAIT = Integer.parseInt(System.getProperty("implicitWait", "10"));
    private static final int PAGE_LOAD_TIMEOUT = Integer.parseInt(System.getProperty("pageLoadTimeout", "30"));

    private DriverManager() {
        // Clase de utilidad, no instanciable
    }

    /**
     * Devuelve el driver del hilo actual. Lo crea si no existe.
     *
     * @return instancia de {@link WebDriver} única por hilo
     */
    public static WebDriver getDriver() {
        if (DRIVER_THREAD_LOCAL.get() == null) {
            DRIVER_THREAD_LOCAL.set(createDriver());
        }
        return DRIVER_THREAD_LOCAL.get();
    }

    /**
     * Cierra el driver del hilo actual y limpia el ThreadLocal.
     * Es idempotente: puede llamarse varias veces sin efectos secundarios.
     */
    public static void quitDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver != null) {
            try {
                driver.quit();
                log.info("Driver cerrado correctamente para el hilo: {}", Thread.currentThread().getId());
            } catch (Exception e) {
                log.warn("Error al cerrar el driver: {}", e.getMessage());
            } finally {
                DRIVER_THREAD_LOCAL.remove();
            }
        }
    }

    /**
     * Indica si el hilo actual ya tiene un driver inicializado.
     * Útil para chequeos defensivos antes de tomar capturas, por ejemplo.
     *
     * @return {@code true} si hay un driver activo en el hilo actual
     */
    public static boolean hasDriver() {
        return DRIVER_THREAD_LOCAL.get() != null;
    }

    // ═══════════════════ MÉTODOS PRIVADOS ═══════════════════

    private static WebDriver createDriver() {
        WebDriver driver;
        log.info("🌐 Inicializando driver: {} (headless={})", BROWSER, HEADLESS);

        switch (BROWSER) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (HEADLESS) firefoxOptions.addArguments("--headless");
                firefoxOptions.addArguments("--width=1920", "--height=1080");
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (HEADLESS) edgeOptions.addArguments("--headless");
                edgeOptions.addArguments("--window-size=1920,1080", "--disable-notifications");
                driver = new EdgeDriver(edgeOptions);
                break;

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (HEADLESS) chromeOptions.addArguments("--headless=new");
                chromeOptions.addArguments(
                    "--window-size=1920,1080",
                    "--disable-notifications",
                    "--remote-allow-origins=*",
                    "--no-sandbox",
                    "--disable-dev-shm-usage"
                );
                driver = new ChromeDriver(chromeOptions);
                break;
        }

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
        driver.manage().window().maximize();

        return driver;
    }
}