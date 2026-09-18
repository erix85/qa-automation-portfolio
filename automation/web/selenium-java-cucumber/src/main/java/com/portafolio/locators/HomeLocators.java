package com.portafolio.locators;

import org.openqa.selenium.By;

/**
 * Locators de la página principal de DemoBlaze.
 *
 * <p>Incluye la barra de navegación, el catálogo de productos y los
 * elementos comunes de la home.</p>
 *
 * @author Erick
 */
public final class HomeLocators {

    private HomeLocators() {
        // Clase de constantes, no instanciable
    }

    // ═══════════════════ NAVBAR ═══════════════════
    public static final By LOGO         = By.id("nava");
    public static final By SIGNUP_LINK  = By.id("signin2");
    public static final By LOGIN_LINK   = By.id("login2");
    public static final By CART_LINK    = By.id("cartur");
    public static final By LOGOUT_LINK  = By.id("logout2");
    public static final By WELCOME_USER = By.id("nameofuser");

    // ═══════════════════ CATÁLOGO ═══════════════════
    public static final By PRODUCT_TITLES  = By.className("card-title");
    public static final By PRODUCT_PRICES  = By.cssSelector(".card-block h5");
    public static final By PRODUCT_CARDS   = By.className("card");

    // ═══════════════════ CATEGORÍAS ═══════════════════
    public static final By CATEGORY_PHONES   = By.linkText("Phones");
    public static final By CATEGORY_LAPTOPS  = By.linkText("Laptops");
    public static final By CATEGORY_MONITORS = By.linkText("Monitors");

    // ═══════════════════ MODALES ═══════════════════
    public static final By SIGNUP_MODAL = By.id("signInModal");
    public static final By LOGIN_MODAL  = By.id("logInModal");
}