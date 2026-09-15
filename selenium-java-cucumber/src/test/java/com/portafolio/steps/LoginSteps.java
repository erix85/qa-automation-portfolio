package com.portafolio.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.portafolio.models.User;
import com.portafolio.pages.HomePage;
import com.portafolio.pages.LoginPage;
import com.portafolio.utils.GeneratedUserStore;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

/**
 * Step Definitions específicos del feature de login.
 *
 * <p><b>Nota:</b> los steps compartidos (navegación, validación de alerts,
 * etc.) viven en {@link CommonSteps}. Aquí solo los específicos del login.</p>
 *
 * <p><b>Reutilización de datos:</b> el escenario positivo usa el usuario
 * generado por el feature de signup, recuperado a través de
 * {@link GeneratedUserStore#getLastCreated()}.</p>
 *
 * @author Erick
 */
@Epic("Autenticación")
@Feature("Login de usuarios")
public class LoginSteps {

    private static final Logger log = LoggerFactory.getLogger(LoginSteps.class);

    private final HomePage homePage = new HomePage();
    private final LoginPage loginPage = new LoginPage();

    // ═══════════════════ WHEN ═══════════════════

    /**
     * Inicia sesión con el último usuario generado por el feature de signup.
     *
     * <p><b>Prerequisito:</b> debe haberse ejecutado antes el escenario
     * {@code @signup @positive}, que deja el usuario persistido en
     * {@code target/generated-users.json}.</p>
     */
    @Cuando("el usuario recién creado inicia sesión")
    @Story("Ingreso con usuario generado")
    @Severity(SeverityLevel.CRITICAL)
    public void elUsuarioRecienCreadoIniciaSesion() {
        log.info("🎬 When: login con el usuario recién creado");

        User lastUser = GeneratedUserStore.getLastCreated();
        Assert.assertNotNull(lastUser,
                "No hay usuarios generados. Ejecuta primero el feature @signup");

        log.info("   Usuario recuperado: {}", lastUser.getUsername());

        Allure.addAttachment(
                "Usuario reutilizado",
                "application/json",
                lastUser.toString(),
                ".txt");

        Allure.step("Ingresar usuario: " + lastUser.getUsername(), () -> {
            loginPage.enterUsername(lastUser.getUsername());
        });

        Allure.step("Ingresar contraseña", () -> {
            loginPage.enterPassword(lastUser.getPassword());
        });

        Allure.step("Hacer click en el botón Log in", () -> {
            loginPage.clickLogin();
        });
    }

    /**
     * Completa el formulario de login con un usuario específico (para negativos).
     */
    @Cuando("completa el formulario de login con usuario {string} y contraseña {string}")
    @Story("Ingreso de credenciales")
    @Severity(SeverityLevel.CRITICAL)
    public void completarFormularioLogin(String username, String password) {
        log.info("🎬 When: completando formulario de login con usuario '{}'", username);

        Allure.step("Ingresar usuario: " + username, () -> {
            loginPage.enterUsername(username);
        });

        Allure.step("Ingresar contraseña", () -> {
            loginPage.enterPassword(password);
        });
    }

    /**
     * Completa el formulario de login usando el usuario generado pero con
     * una contraseña incorrecta (para escenarios negativos).
     */
    @Cuando("completa el formulario de login con un usuario generado y contraseña {string}")
    @Story("Ingreso de credenciales")
    @Severity(SeverityLevel.CRITICAL)
    public void completarFormularioLoginConUsuarioGenerado(String wrongPassword) {
        User lastUser = GeneratedUserStore.getLastCreated();
        Assert.assertNotNull(lastUser,
                "No hay usuarios generados. Ejecuta primero el feature @signup");

        log.info("🎬 When: login con usuario '{}' y contraseña incorrecta",
                lastUser.getUsername());

        Allure.step("Ingresar usuario: " + lastUser.getUsername(), () -> {
            loginPage.enterUsername(lastUser.getUsername());
        });

        Allure.step("Ingresar contraseña incorrecta", () -> {
            loginPage.enterPassword(wrongPassword);
        });
    }

    @Cuando("confirma el login")
    @Story("Ingreso de credenciales")
    @Severity(SeverityLevel.CRITICAL)
    public void confirmarLogin() {
        log.info("🎬 When: confirmando login");
        Allure.step("Hacer click en el botón Log in", () -> {
            loginPage.clickLogin();
        });
    }

    // ═══════════════════ THEN ═══════════════════

    @Entonces("debería ver el mensaje de bienvenida {string}")
    @Story("Acceso exitoso")
    @Severity(SeverityLevel.BLOCKER)
    public void deberiaVerMensajeBienvenida(String expectedWelcome) {
        log.info("✅ Then: validando mensaje de bienvenida '{}'", expectedWelcome);

        String actualWelcome = Allure.step("Capturar mensaje de bienvenida", () -> {
            return homePage.getWelcomeMessage();
        });

        log.info("   Mensaje actual: '{}'", actualWelcome);

        Allure.step("Validar mensaje de bienvenida", () -> {
            Assert.assertTrue(
                    actualWelcome.contains(expectedWelcome),
                    "Se esperaba: '" + expectedWelcome + "' pero se obtuvo: '" + actualWelcome + "'");
        });
    }
}