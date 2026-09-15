package com.portafolio.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.portafolio.models.User;
import com.portafolio.pages.HomePage;
import com.portafolio.pages.LoginPage;
import com.portafolio.utils.TestDataReader;

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
 * @author Erick
 */
@Epic("Autenticación")
@Feature("Login de usuarios")
public class LoginSteps {

    private static final Logger log = LoggerFactory.getLogger(LoginSteps.class);

    private final HomePage homePage = new HomePage();
    private final LoginPage loginPage = new LoginPage();

    // ═══════════════════ WHEN ═══════════════════

    @Cuando("el usuario {string} inicia sesión con su contraseña")
    @Story("Ingreso de credenciales")
    @Severity(SeverityLevel.CRITICAL)
    public void elUsuarioIniciaSesion(String userKey) {
        log.info("🎬 When: usuario '{}' inicia sesión", userKey);

        // ✅ DTO: datos tipados desde JSON
        User user = TestDataReader.getUser(userKey);

        // Evidencia en Allure
        Allure.addAttachment(
                "Datos del usuario: " + userKey,
                "application/json",
                user.toString(),
                ".txt");

        Allure.step("Ingresar usuario: " + user.getUsername(), () -> {
            loginPage.enterUsername(user.getUsername());
        });

        Allure.step("Ingresar contraseña", () -> {
            loginPage.enterPassword(user.getPassword());
        });

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

        Allure.step("Validar mensaje de bienvenida", () -> {
            Assert.assertTrue(
                    actualWelcome.contains(expectedWelcome),
                    "Se esperaba: '" + expectedWelcome + "' pero se obtuvo: '" + actualWelcome + "'");
        });
    }
}