package com.portafolio.pages;

import com.portafolio.locators.LoginLocators;

/**
 * Page Object para el modal de Login de DemoBlaze.
 *
 * <p>El login no es una página separada, sino un modal que aparece
 * sobre la home. Este Page Object se encarga de sus acciones.</p>
 *
 * @author Erick
 */
public class LoginPage extends BasePage {

    public LoginPage() {
        super();
        log.debug("LoginPage instanciada");
    }

    // ═══════════════════ ACCIONES ═══════════════════

    public void enterUsername(String username) {
        type(LoginLocators.USERNAME_FIELD, username);
    }

    public void enterPassword(String password) {
        type(LoginLocators.PASSWORD_FIELD, password);
    }

    public void clickLogin() {
        click(LoginLocators.LOGIN_BUTTON);
    }

    /**
     * Método de conveniencia: completa el formulario y confirma el login.
     */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    // ═══════════════════ CONSULTAS ═══════════════════

    @Override
    public boolean isPageLoaded() {
        return isDisplayed(LoginLocators.MODAL)
            && isDisplayed(LoginLocators.USERNAME_FIELD);
    }
}