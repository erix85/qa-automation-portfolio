package com.portafolio.locators;

import org.openqa.selenium.By;

/**
 * Locators del modal de Login de DemoBlaze.
 *
 * <p><b>Nota:</b> DemoBlaze usa un modal (no una página dedicada).
 * Los elementos solo son visibles tras hacer click en el link "Log in".</p>
 *
 * @author Erick
 */
public final class LoginLocators {

    private LoginLocators() {
        // Clase de constantes, no instanciable
    }

    // ═══════════════════ MODAL LOGIN ═══════════════════
    public static final By MODAL          = By.id("logInModal");
    public static final By USERNAME_FIELD = By.id("loginusername");
    public static final By PASSWORD_FIELD = By.id("loginpassword");
    public static final By LOGIN_BUTTON   = By.xpath("//button[text()='Log in']");

    // ═══════════════════ POST-LOGIN (navbar) ═══════════════════
    public static final By WELCOME_USER = By.id("nameofuser");
    public static final By LOGOUT_LINK  = By.id("logout2");
}