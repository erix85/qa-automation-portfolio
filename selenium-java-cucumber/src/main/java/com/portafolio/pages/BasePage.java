package com.portafolio.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.portafolio.utils.DriverManager;
import com.portafolio.utils.WaitUtils;

/**
 * Clase base para todos los Page Objects.
 *
 * <p><b>Responsabilidad única:</b> proporcionar operaciones comunes de interacción
 * con la UI (click, type, getText, select, etc.) delegando las esperas a
 * {@link WaitUtils}. Cada Page Object concreto hereda de aquí y solo declara
 * sus locators y acciones específicas.</p>
 *
 * <p><b>Principios aplicados:</b></p>
 * <ul>
 *   <li><b>DRY:</b> evita duplicar operaciones básicas en cada Page Object.</li>
 *   <li><b>SRP:</b> los Page Objects concretos solo se preocupan por su página.</li>
 *   <li><b>OCP:</b> añadir nuevos métodos comunes no rompe las subclases.</li>
 * </ul>
 *
 * <p><b>Uso:</b></p>
 * <pre>
 * public class LoginPage extends BasePage {
 *     private static final By USERNAME = By.id("user-name");
 *
 *     public void enterUsername(String username) {
 *         type(USERNAME, username);   // método heredado
 *     }
 * }
 * </pre>
 *
 * @author Erick
 */
public abstract class BasePage {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final WebDriver driver;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
    }

    // ═══════════════════ OPERACIONES BÁSICAS ═══════════════════

    /**
     * Hace click en un elemento, esperando a que sea clickeable.
     */
    protected void click(By locator) {
        log.debug("🖱️ Click en: {}", locator);
        WaitUtils.waitForClickability(locator).click();
    }

    /**
     * Limpia un campo de texto y escribe el valor indicado.
     */
    protected void type(By locator, String text) {
        log.debug("✏️ Escribiendo '{}' en: {}", text, locator);
        WebElement element = WaitUtils.waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Escribe en un campo sin limpiarlo previamente.
     */
    protected void appendText(By locator, String text) {
        log.debug("✏️ Añadiendo '{}' en: {}", text, locator);
        WaitUtils.waitForVisibility(locator).sendKeys(text);
    }

    /**
     * Obtiene el texto visible de un elemento.
     */
    protected String getText(By locator) {
        String text = WaitUtils.waitForVisibility(locator).getText();
        log.debug("📖 Texto obtenido de {}: '{}'", locator, text);
        return text;
    }

    /**
     * Obtiene el valor del atributo {@code value} de un input (útil para validar
     * el contenido de un campo rellenado).
     */
    protected String getValue(By locator) {
        return WaitUtils.waitForVisibility(locator).getAttribute("value");
    }

    /**
     * Verifica si un elemento está visible en la página, sin lanzar excepción.
     *
     * @return {@code true} si el elemento es visible; {@code false} en caso contrario
     */
    protected boolean isDisplayed(By locator) {
        try {
            return WaitUtils.waitForVisibility(locator).isDisplayed();
        } catch (Exception e) {
            log.warn("Elemento no visible: {}", locator);
            return false;
        }
    }

    /**
     * Verifica si un elemento está presente en el DOM (aunque no sea visible).
     */
    protected boolean isPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    /**
     * Cuenta cuántos elementos coinciden con el locator.
     */
    protected int countElements(By locator) {
        return driver.findElements(locator).size();
    }

    // ═══════════════════ OPERACIONES AVANZADAS ═══════════════════

    /**
     * Selecciona una opción de un dropdown por su texto visible.
     */
    protected void selectByVisibleText(By locator, String visibleText) {
        log.debug("🔽 Seleccionando '{}' en: {}", visibleText, locator);
        Select select = new Select(WaitUtils.waitForVisibility(locator));
        select.selectByVisibleText(visibleText);
    }

    /**
     * Selecciona una opción de un dropdown por su valor.
     */
    protected void selectByValue(By locator, String value) {
        log.debug("🔽 Seleccionando value='{}' en: {}", value, locator);
        Select select = new Select(WaitUtils.waitForVisibility(locator));
        select.selectByValue(value);
    }

    /**
     * Hace scroll hasta un elemento.
     */
    protected void scrollTo(By locator) {
        WebElement element = WaitUtils.waitForVisibility(locator);
        org.openqa.selenium.JavascriptExecutor js = 
                (org.openqa.selenium.JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        log.debug("📜 Scroll realizado a: {}", locator);
    }

    // ═══════════════════ NAVEGACIÓN ═══════════════════

    /**
     * Navega a una URL.
     */
    protected void navigateTo(String url) {
        log.info("🌐 Navegando a: {}", url);
        driver.get(url);
    }

    /**
     * Devuelve la URL actual.
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Devuelve el título de la página actual.
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }

    // ═══════════════════ MÉTODO ABSTRACTO ═══════════════════

    /**
     * Cada Page Object concreto debe implementar este método para verificar
     * que la página se ha cargado correctamente. Es la primera aserción que
     * debería hacerse al entrar a una página.
     *
     * @return {@code true} si la página está cargada y lista para interactuar
     */
    public abstract boolean isPageLoaded();
}