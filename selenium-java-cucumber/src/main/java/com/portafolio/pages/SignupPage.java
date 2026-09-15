package com.portafolio.pages;

import com.portafolio.locators.SignupLocators;

/**
 * Page Object para el modal de registro de DemoBlaze.
 *
 * <p>Modela las acciones del formulario de registro. Los locators viven en
 * {@link SignupLocators}.</p>
 *
 * @author Erick
 */
public class SignupPage extends BasePage {

    public SignupPage() {
        super();
        log.debug("SignupPage instanciada");
    }

    // ═══════════════════ ACCIONES ═══════════════════

    public void enterUsername(String username) {
        type(SignupLocators.USERNAME_FIELD, username);
    }

    public void enterPassword(String password) {
        type(SignupLocators.PASSWORD_FIELD, password);
    }

    public void clickSignupButton() {
        click(SignupLocators.SIGNUP_BUTTON);
    }

    /**
     * Método de conveniencia: completa el formulario y confirma el registro.
     */
    public void signup(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickSignupButton();
    }

    // ═══════════════════ CONSULTAS ═══════════════════

    @Override
    public boolean isPageLoaded() {
        return isDisplayed(SignupLocators.MODAL)
            && isDisplayed(SignupLocators.USERNAME_FIELD);
    }
}