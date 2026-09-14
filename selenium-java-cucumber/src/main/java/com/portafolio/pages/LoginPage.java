package com.portafolio.pages;

import org.openqa.selenium.By;

/**
 * Page Object para la página de Login de SauceDemo.
 *
 * <p><b>Refactorizado:</b> ahora hereda de {@link BasePage}, lo que elimina
 * la duplicación de operaciones básicas (click, type, getText) y centraliza
 * las esperas en {@link com.portafolio.utils.WaitUtils}.</p>
 */
public class LoginPage extends BasePage {

    // ═══════════════════ LOCATORS ═══════════════════
    private static final By USERNAME_FIELD = By.id("user-name");
    private static final By PASSWORD_FIELD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ERROR_MESSAGE = By.className("error-message-container");
    private static final By LOGO = By.className("app_logo");

    private static final String URL = "https://www.saucedemo.com/";

    public LoginPage() {
        super();
        log.debug("LoginPage instanciada");
    }

    // ═══════════════════ ACCIONES ═══════════════════

    public void navigateTo() {
        navigateTo(URL);
        waitForLoad();
    }

    public void enterUsername(String username) {
        type(USERNAME_FIELD, username);
    }

    public void enterPassword(String password) {
        type(PASSWORD_FIELD, password);
    }

    public void clickLogin() {
        click(LOGIN_BUTTON);
    }

    /**
     * Método de conveniencia: agrupa las acciones del login en una sola llamada.
     * Ideal para mantener los Steps legibles.
     */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    // ═══════════════════ CONSULTAS ═══════════════════

    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }

    public boolean isLogoDisplayed() {
        return isDisplayed(LOGO);
    }

    public boolean isLoginButtonDisplayed() {
        return isDisplayed(LOGIN_BUTTON);
    }

    // ═══════════════════ IMPLEMENTACIÓN DE BasePage ═══════════════════

    @Override
    public boolean isPageLoaded() {
        return isDisplayed(LOGO) && isDisplayed(LOGIN_BUTTON);
    }

    /**
     * Espera a que la página de login esté cargada.
     * Se usa internamente en {@link #navigateTo()}.
     */
    private void waitForLoad() {
        if (!isPageLoaded()) {
            throw new IllegalStateException("La página de login no se cargó correctamente");
        }
        log.info("✅ Página de login cargada");
    }
}