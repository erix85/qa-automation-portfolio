package com.portafolio.pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.portafolio.locators.HomeLocators;
import com.portafolio.utils.ConfigReader;
import com.portafolio.utils.WaitUtils;

/**
 * Page Object para la página principal de DemoBlaze.
 *
 * <p>Modela la navbar, el catálogo y las categorías. Es la base de
 * prácticamente todos los flujos de la tienda.</p>
 *
 * <p><b>Nota sobre login:</b> el método {@link #getWelcomeMessage()}
 * espera a que el mensaje de bienvenida sea visible antes de leerlo,
 * evitando race conditions después del login.</p>
 *
 * @author Erick
 */
public class HomePage extends BasePage {

    public HomePage() {
        super();
        log.debug("HomePage instanciada");
    }

    // ═══════════════════ NAVEGACIÓN ═══════════════════

    public void navigateTo() {
        String url = ConfigReader.getFullUrl("home.path");
        navigateTo(url);
        waitForLoad();
    }

    /**
     * Hace click en un enlace de la barra de navegación.
     *
     * @param linkText "Sign up", "Log in", "Cart" o "Log out"
     */
    public void clickOnLink(String linkText) {
        log.info("🖱️ Click en enlace: '{}'", linkText);
        By locator = switch (linkText.toLowerCase()) {
            case "sign up" -> HomeLocators.SIGNUP_LINK;
            case "log in"  -> HomeLocators.LOGIN_LINK;
            case "cart"    -> HomeLocators.CART_LINK;
            case "log out" -> HomeLocators.LOGOUT_LINK;
            default -> throw new IllegalArgumentException("Enlace no soportado: " + linkText);
        };
        click(locator);
    }

    /**
     * Selecciona una categoría del menú lateral.
     *
     * @param category "Phones", "Laptops" o "Monitors"
     */
    public void selectCategory(String category) {
        log.info("📂 Seleccionando categoría: '{}'", category);
        By locator = switch (category.toLowerCase()) {
            case "phones"   -> HomeLocators.CATEGORY_PHONES;
            case "laptops"  -> HomeLocators.CATEGORY_LAPTOPS;
            case "monitors" -> HomeLocators.CATEGORY_MONITORS;
            default -> throw new IllegalArgumentException("Categoría no soportada: " + category);
        };
        click(locator);
    }

    // ═══════════════════ CONSULTAS ═══════════════════

    /**
     * Devuelve el nombre del usuario logueado que muestra la navbar,
     * o cadena vacía si no hay sesión activa.
     *
     * <p>Espera a que el mensaje sea visible tras el login.</p>
     */
    public String getWelcomeMessage() {
        if (!isDisplayed(HomeLocators.WELCOME_USER)) {
            return "";
        }
        WaitUtils.waitForVisibility(HomeLocators.WELCOME_USER);
        return getText(HomeLocators.WELCOME_USER);
    }

    /**
     * Verifica si hay un usuario logueado consultando el mensaje de bienvenida.
     */
    public boolean isUserLoggedIn() {
        return isDisplayed(HomeLocators.WELCOME_USER)
            && getWelcomeMessage().contains("Welcome");
    }

    /**
     * Espera a que el mensaje de bienvenida aparezca en la navbar.
     *
     * <p>Útil después del login para asegurar que la sesión se estableció.</p>
     */
    public boolean waitForUserToBeLoggedIn() {
        try {
            WaitUtils.waitForVisibility(HomeLocators.WELCOME_USER);
            return true;
        } catch (Exception e) {
            log.warn("No se detectó el mensaje de bienvenida");
            return false;
        }
    }

    public List<String> getProductTitles() {
        List<WebElement> elements = driver.findElements(HomeLocators.PRODUCT_TITLES);
        return elements.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public int getProductCount() {
        return countElements(HomeLocators.PRODUCT_CARDS);
    }

    public boolean isProductListed(String productName) {
        return getProductTitles().contains(productName);
    }

    // ═══════════════════ IMPLEMENTACIÓN DE BasePage ═══════════════════

    @Override
    public boolean isPageLoaded() {
        return isDisplayed(HomeLocators.LOGO)
            && isDisplayed(HomeLocators.PRODUCT_TITLES);
    }

    private void waitForLoad() {
        if (!isPageLoaded()) {
            throw new IllegalStateException("La página principal no cargó correctamente");
        }
        log.info("✅ Página principal cargada");
    }
}