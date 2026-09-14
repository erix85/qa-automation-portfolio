package com.portafolio.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.portafolio.models.User;
import com.portafolio.pages.DashboardPage;
import com.portafolio.pages.LoginPage;
import com.portafolio.utils.TestDataReader;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

/**
 * Step Definitions para los escenarios de autenticación.
 *
 * <p><b>Anotaciones Allure:</b> {@code @Epic}, {@code @Feature}, {@code @Story}
 * y {@code @Severity} enriquecen el reporte con jerarquía funcional y criticidad,
 * permitiendo filtrar y agrupar resultados en Allure.</p>
 *
 * @author Erick
 */
@Epic("Autenticación")
@Feature("Login de usuarios")
public class LoginSteps {

    private static final Logger log = LoggerFactory.getLogger(LoginSteps.class);

    private final LoginPage loginPage;
    private final DashboardPage dashboardPage;

    public LoginSteps() {
        this.loginPage = new LoginPage();
        this.dashboardPage = new DashboardPage();
        log.debug("LoginSteps instanciado");
    }

    // ═══════════════════ GIVEN ═══════════════════

    @Dado("que el usuario está en la página de login")
    @Story("Acceso al portal")
    @Severity(SeverityLevel.BLOCKER)
    public void usuarioEnPaginaLogin() {
        log.info("📋 Given: usuario en página de login");

        Allure.step("Navegar a la página de login de SauceDemo", () -> {
            loginPage.navigateTo();
        });

        Allure.step("Verificar que el logo es visible", () -> {
            Assert.assertTrue(loginPage.isLogoDisplayed(), "El logo no está visible");
        });
    }

    // ═══════════════════ WHEN ═══════════════════

    @Cuando("el usuario {string} inicia sesión")
    @Story("Ingreso de credenciales")
    @Severity(SeverityLevel.CRITICAL)
    public void elUsuarioIniciaSesion(String userKey) {
        log.info("🎬 When: usuario '{}' inicia sesión", userKey);

        // ✅ DTO: datos tipados desde JSON
        User user = TestDataReader.getUser(userKey);

        // Adjuntar el JSON del usuario al reporte de Allure (evidencia)
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

        Allure.step("Hacer click en el botón Login", () -> {
            loginPage.clickLogin();
        });
    }

    // ═══════════════════ THEN ═══════════════════

    @Entonces("el sistema redirige al dashboard")
    @Story("Acceso exitoso")
    @Severity(SeverityLevel.BLOCKER)
    public void sistemaRedirigeDashboard() {
        log.info("✅ Then: validando redirección al dashboard");

        Allure.step("Verificar que el dashboard es visible", () -> {
            Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
                    "El dashboard no se muestra");
        });
    }

    @Entonces("muestra el mensaje de error {string}")
    @Story("Validación de errores")
    @Severity(SeverityLevel.NORMAL)
    public void muestraMensajeError(String mensajeEsperado) {
        log.info("✅ Then: validando mensaje de error");

        String mensajeActual = Allure.step("Capturar mensaje de error mostrado", () -> {
            return loginPage.getErrorMessage();
        });

        Allure.step("Validar mensaje esperado: '" + mensajeEsperado + "'", () -> {
            Assert.assertTrue(mensajeActual.contains(mensajeEsperado),
                    "Mensaje esperado: '" + mensajeEsperado + "' pero se obtuvo: '" + mensajeActual + "'");
        });
    }
}