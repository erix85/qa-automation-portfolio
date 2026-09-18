package com.portafolio.pages;

import com.portafolio.locators.LoginLocators;
import com.portafolio.utils.WaitUtils;

/**
 * Page Object para el modal de Login de DemoBlaze.
 *
 * <p>El login no es una página separada, sino un <b>modal</b> que aparece
 * sobre la home. Este Page Object se encarga de sus acciones y garantiza
 * que el modal esté completamente listo antes de interactuar con él.</p>
 *
 * <p><b>Solución de timing:</b> el modal de DemoBlaze tiene una animación
 * de apertura. Antes de escribir en los campos, se espera a que el modal
 * completo sea visible y a que el campo de usuario sea clickeable.
 * Esto evita el problema de "elemento visible pero no interactuable".</p>
 *
 * @author Erick
 */
public class LoginPage extends BasePage {

    public LoginPage() {
        super();
        log.debug("LoginPage instanciada");
    }

    // ═══════════════════ SINCRONIZACIÓN ═══════════════════

    /**
     * Espera a que el modal de login esté completamente listo para interactuar.
     *
     * <p>Verifica que:</p>
     * <ol>
     *   <li>El modal sea visible.</li>
     *   <li>El campo de usuario sea clickeable (no solo visible).</li>
     * </ol>
     */
    public void waitForModalToBeReady() {
        log.debug("⏳ Esperando a que el modal de login esté listo");
        WaitUtils.waitForVisibility(LoginLocators.MODAL);
        WaitUtils.waitForClickability(LoginLocators.USERNAME_FIELD);
        log.debug("✅ Modal de login listo");
    }

    // ═══════════════════ ACCIONES ═══════════════════

    public void enterUsername(String username) {
        waitForModalToBeReady();
        log.info("👤 Ingresando username: '{}'", username);
        type(LoginLocators.USERNAME_FIELD, username);
    }

    public void enterPassword(String password) {
        log.info("🔒 Ingresando contraseña");
        type(LoginLocators.PASSWORD_FIELD, password);
    }

    public void clickLogin() {
        log.info("🖱️ Click en botón Log in");
        click(LoginLocators.LOGIN_BUTTON);
    }

    /**
     * Método de conveniencia: completa el formulario completo y confirma el login.
     *
     * <p>Recomendado para casos donde no se necesita granularidad.</p>
     */
    public void login(String username, String password) {
        log.info("🔐 Iniciando sesión con usuario '{}'", username);
        waitForModalToBeReady();
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    // ═══════════════════ CONSULTAS ═══════════════════

    /**
     * Verifica si el modal de login está visible.
     */
    public boolean isModalVisible() {
        return isDisplayed(LoginLocators.MODAL);
    }

    @Override
    public boolean isPageLoaded() {
        return isDisplayed(LoginLocators.MODAL)
            && isDisplayed(LoginLocators.USERNAME_FIELD);
    }
}