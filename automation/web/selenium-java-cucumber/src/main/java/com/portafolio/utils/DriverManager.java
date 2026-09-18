package com.portafolio.utils;

import java.net.URI;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Gestor del WebDriver basado en ThreadLocal con soporte para <b>tres modos</b>:
 * <ul>
 *   <li><b>LOCAL</b> — WebDriverManager en la máquina del desarrollador
 *       (Windows/Mac/Linux con GUI).</li>
 *   <li><b>CODESPACES</b> — Chromium headless en GitHub Codespaces (Linux sin GUI).
 *       Se detecta automáticamente vía la variable {@code CODESPACES=true}.</li>
 *   <li><b>GRID</b> — {@link RemoteWebDriver} conectado a un Hub de Selenium Grid.
 *       Se activa con {@code -Dgrid.url=http://localhost:4444}.</li>
 * </ul>
 *
 * <p><b>Prioridad de selección:</b></p>
 * <ol>
 *   <li>Si {@code -Dgrid.url} está definida → <b>GRID</b>.</li>
 *   <li>Si {@code CODESPACES=true} → <b>CODESPACES</b>.</li>
 *   <li>Si no → <b>LOCAL</b> (default).</li>
 * </ol>
 *
 * <p><b>Principio de diseño:</b> esta clase es agnóstica al framework de testing.
 * No conoce a Cucumber, JUnit, TestNG ni a ningún runner. Solo gestiona el ciclo
 * de vida de {@link WebDriver}.</p>
 *
 * <p><b>Configuración por propiedades del sistema:</b></p>
 * <ul>
 *   <li>{@code -Dbrowser=chrome|firefox|edge} (default: chrome)</li>
 *   <li>{@code -Dheadless=true|false} (default: false en local, true en Codespaces)</li>
 *   <li>{@code -Dgrid.url=http://localhost:4444} (default: vacío → modo local/Codespaces)</li>
 *   <li>{@code -DpageLoadTimeout=30} (segundos)</li>
 * </ul>
 *
 * <p><b>Ejemplos de ejecución:</b></p>
 * <pre>
 * // Modo LOCAL (Windows con GUI)
 * mvn test -Dbrowser=chrome
 *
 * // Modo CODESPACES (headless, sin configuración adicional)
 * mvn test -Dcucumber.filter.tags="@smoke"
 *
 * // Modo GRID (Grid corriendo en localhost:4444)
 * mvn test -Dgrid.url=http://localhost:4444 -Dbrowser=firefox
 * </pre>
 *
 * @author Erick
 */
public final class DriverManager {

    private static final Logger log = LoggerFactory.getLogger(DriverManager.class);

    private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    // ═══════════════════ CONFIGURACIÓN ═══════════════════

    private static final String BROWSER = System.getProperty("browser", "chrome").toLowerCase();
    private static final int PAGE_LOAD_TIMEOUT = Integer.parseInt(System.getProperty("pageLoadTimeout", "30"));

    private static final String GRID_URL = System.getProperty("grid.url", "").trim();
    private static final boolean USE_GRID = !GRID_URL.isEmpty();

    private static final boolean IS_CODESPACES = "true".equalsIgnoreCase(System.getenv("CODESPACES"));

    /**
     * Headless se activa automáticamente en Codespaces. En local, se lee de la
     * propiedad del sistema {@code -Dheadless} (default: false).
     */
    private static final boolean HEADLESS =
            IS_CODESPACES || Boolean.parseBoolean(System.getProperty("headless", "false"));

    private DriverManager() {
        // Clase de utilidad, no instanciable
    }

    // ═══════════════════ API PÚBLICA ═══════════════════

    /**
     * Devuelve el driver del hilo actual. Lo crea si no existe.
     *
     * <p>Selecciona automáticamente el modo:</p>
     * <ol>
     *   <li>GRID si {@code -Dgrid.url} está definida.</li>
     *   <li>CODESPACES si {@code CODESPACES=true} en las variables de entorno.</li>
     *   <li>LOCAL en cualquier otro caso.</li>
     * </ol>
     *
     * @return instancia de {@link WebDriver} única por hilo
     */
    public static WebDriver getDriver() {
        if (DRIVER_THREAD_LOCAL.get() == null) {
            WebDriver driver = createDriver();
            DRIVER_THREAD_LOCAL.set(driver);
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
     *
     * @return {@code true} si hay un driver activo en el hilo actual
     */
    public static boolean hasDriver() {
        return DRIVER_THREAD_LOCAL.get() != null;
    }

    /**
     * Indica si el framework está configurado para usar Selenium Grid.
     *
     * @return {@code true} si {@code -Dgrid.url} está definida y no vacía
     */
    public static boolean isGridMode() {
        return USE_GRID;
    }

    /**
     * Indica si el framework está corriendo en GitHub Codespaces.
     *
     * @return {@code true} si {@code CODESPACES=true}
     */
    public static boolean isCodespacesMode() {
        return IS_CODESPACES;
    }

    /**
     * Devuelve la URL del Grid configurada, o cadena vacía si no está en modo Grid.
     */
    public static String getGridUrl() {
        return GRID_URL;
    }

    // ═══════════════════ FACTORY PRIVADO ═══════════════════

    private static WebDriver createDriver() {
        if (USE_GRID) {
            log.info("🎯 Modo seleccionado: GRID (por -Dgrid.url)");
            return createRemoteDriver();
        }
        if (IS_CODESPACES) {
            log.info("🎯 Modo seleccionado: CODESPACES (entorno detectado)");
            return createCodespacesDriver();
        }
        log.info("🎯 Modo seleccionado: LOCAL");
        return createLocalDriver();
    }

    // ═══════════════════ MODO LOCAL ═══════════════════

    private static WebDriver createLocalDriver() {
        WebDriver driver;
        log.info("🖥️  Modo LOCAL: inicializando driver: {} (headless={})", BROWSER, HEADLESS);

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

        configureTimeouts(driver);
        return driver;
    }

    // ═══════════════════ MODO CODESPACES ═══════════════════

    /**
     * Crea un driver específicamente configurado para GitHub Codespaces.
     *
     * <p>Codespaces es un contenedor Linux sin GUI. Se configura Chromium en modo
     * headless, apuntando al binario instalado por el {@code devcontainer.json}.</p>
     *
     * <p>Detalles críticos:</p>
     * <ul>
     *   <li>Binario de Chromium en {@code /usr/bin/chromium-browser}.</li>
     *   <li>Flags obligatorios: {@code --no-sandbox} y {@code --disable-dev-shm-usage}.</li>
     *   <li>Modo headless por defecto.</li>
     * </ul>
     */
    private static WebDriver createCodespacesDriver() {
        log.info("☁️  Modo CODESPACES: configurando Chromium headless en Linux");

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-notifications");

        // Ruta del binario instalado por el devcontainer
        if (System.getenv("CHROMIUM_BINARY") != null) {
            options.setBinary(System.getenv("CHROMIUM_BINARY"));
        } else {
            // Ruta estándar en devcontainers de Microsoft
            options.setBinary("/usr/bin/chromium");
        }

        WebDriver driver = new ChromeDriver(options);
        configureTimeouts(driver);
        log.info("✅ Chromium headless inicializado para Codespaces");
        return driver;
    }

    // ═══════════════════ MODO GRID (REMOTO) ═══════════════════

    private static WebDriver createRemoteDriver() {
        log.info("🌐 Modo GRID: conectando a {} con navegador {}", GRID_URL, BROWSER);

        try {
            URL hubUrl = URI.create(GRID_URL).toURL();

            RemoteWebDriver driver;
            switch (BROWSER) {
                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (HEADLESS) firefoxOptions.addArguments("--headless");
                    driver = new RemoteWebDriver(hubUrl, firefoxOptions);
                    break;

                case "edge":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    if (HEADLESS) edgeOptions.addArguments("--headless");
                    driver = new RemoteWebDriver(hubUrl, edgeOptions);
                    break;

                case "chrome":
                default:
                    ChromeOptions chromeOptions = new ChromeOptions();
                    if (HEADLESS) chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments(
                        "--window-size=1920,1080",
                        "--disable-notifications",
                        "--no-sandbox",
                        "--disable-dev-shm-usage"
                    );
                    driver = new RemoteWebDriver(hubUrl, chromeOptions);
                    break;
            }

            configureTimeouts(driver);
            log.info("✅ Sesión remota establecida. Session ID: {}", driver.getSessionId());
            return driver;

        } catch (Exception e) {
            throw new RuntimeException(
                "❌ No se pudo conectar al Selenium Grid en: " + GRID_URL +
                "\n   Verifica que el Grid esté corriendo:" +
                "\n     - Docker:     docker-compose ps" +
                "\n     - Standalone: java -jar selenium-server-4.49.0.jar standalone" +
                "\n   Y que la URL sea accesible desde el navegador: " + GRID_URL, e);
        }
    }

    // ═══════════════════ HELPERS ═══════════════════

    private static void configureTimeouts(WebDriver driver) {
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
        try {
            driver.manage().window().maximize();
        } catch (Exception e) {
            // En headless algunos navegadores no soportan maximize; se ignora.
            log.debug("No se pudo maximizar la ventana (probablemente headless): {}", e.getMessage());
        }
    }
}