package com.portafolio.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.portafolio.models.User;
import com.portafolio.pages.HomePage;
import com.portafolio.pages.LoginPage;
import com.portafolio.utils.GeneratedUserStore;
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
 * <p><b>Fuentes de datos soportadas:</b></p>
 * <ul>
 *   <li><b>Estáticas</b>: usuarios de {@code users.json} que ya existen en la app
 *       (ej. {@code validUser}). Se leen con {@link TestDataReader}.</li>
 *   <li><b>Dinámicas</b>: usuarios generados por el feature de signup y
 *       persistidos en {@code target/generated-users.json}. Se leen con
 *       {@link GeneratedUserStore}.</li>
 * </ul>
 *
 * <p>Esta separación permite que el feature de login sea <b>autocontenido</b>
 * cuando usa datos estáticos, sin depender del orden de ejecución con signup.</p>
 *
 * @author Erick
 */
@Epic("Autenticación")
@Feature("Login de usuarios")
public class LoginSteps {

    private static final Logger log = LoggerFactory.getLogger(LoginSteps.class);

    private final HomePage homePage = new HomePage();
    private final LoginPage loginPage = new LoginPage();

    // ═══════════════════ WHEN — DATOS ESTÁTICOS ═══════════════════

    /**
     * Inicia sesión con un usuario estático desde {@code users.json}.
     *
     * <p>Autocontenido: no depende de ejecuciones previas de signup.</p>
     *
     * @param userKey clave del usuario en {@code users.json} (ej. "validUser")
     */
    @Cuando("el usuario {string} inicia sesión con sus credenciales")
    @Story("Ingreso con usuario estático")
    @Severity(SeverityLevel.CRITICAL)
    public void elUsuarioIniciaSesionConCredenciales(String userKey) {
        log.info("🎬 When: login con usuario estático '{}'", userKey);

        User user = TestDataReader.getUser(userKey);

        Allure.addAttachment(
                "Credenciales usadas",
                "application/json",
                "user: " + user.getUsername() + ", role: " + user.getRole(),
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

    /**
     * Completa el formulario de login con un usuario estático pero una contraseña incorrecta.
     *
     * @param userKey clave del usuario en {@code users.json}
     * @param wrongPassword contraseña incorrecta a usar
     */
    @Cuando("el usuario {string} inicia sesión con contraseña incorrecta {string}")
    @Story("Ingreso con contraseña incorrecta")
    @Severity(SeverityLevel.CRITICAL)
    public void elUsuarioIniciaSesionConPasswordIncorrecta(String userKey, String wrongPassword) {
        log.info("🎬 When: usuario estático '{}' con contraseña incorrecta", userKey);

        User user = TestDataReader.getUser(userKey);

        Allure.step("Ingresar usuario: " + user.getUsername(), () -> {
            loginPage.enterUsername(user.getUsername());
        });

        Allure.step("Ingresar contraseña incorrecta", () -> {
            loginPage.enterPassword(wrongPassword);
        });
    }

    /**
     * Completa el formulario de login con un usuario literal (texto plano).
     * Útil para escenarios negativos con usuarios inexistentes.
     */
    @Cuando("completa el formulario de login con usuario {string} y contraseña {string}")
    @Story("Ingreso de credenciales literales")
    @Severity(SeverityLevel.CRITICAL)
    public void completarFormularioLogin(String username, String password) {
        log.info("🎬 When: completando formulario de login con usuario literal '{}'", username);

        Allure.step("Ingresar usuario: " + username, () -> {
            loginPage.enterUsername(username);
        });

        Allure.step("Ingresar contraseña", () -> {
            loginPage.enterPassword(password);
        });
    }

    // ═══════════════════ WHEN — DATOS DINÁMICOS ═══════════════════

    /**
     * Inicia sesión con el último usuario generado por el feature de signup.
     *
     * <p><b>Prerequisito:</b> debe haberse ejecutado antes el escenario
     * {@code @signup @positive}, que deja el usuario persistido en
     * {@code target/generated-users.json}.</p>
     *
     * <p><b>Uso recomendado:</b> solo para tests end-to-end que prueban la
     * integración signup → login. Para tests de login puros, usar
     * {@code el usuario "validUser" inicia sesión con sus credenciales}.</p>
     */
    @Cuando("el usuario recién creado inicia sesión")
    @Story("Ingreso con usuario generado")
    @Severity(SeverityLevel.NORMAL)
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

    // ═══════════════════ WHEN — COMÚN ═══════════════════

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