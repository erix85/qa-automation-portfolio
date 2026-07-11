package com.portafolio.steps;

import com.portafolio.pages.DashboardPage;
import com.portafolio.pages.LoginPage;
import io.cucumber.java.es.*;

import static org.testng.Assert.assertTrue;

public class LoginSteps {
    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    public LoginSteps() {
        loginPage = new LoginPage();
        dashboardPage = new DashboardPage();
    }

    @Dado("que el usuario está en la página de login")
    public void usuarioEnPaginaLogin() {
        loginPage.navigateTo();
        assertTrue(loginPage.isLogoDisplayed(), "El logo no está visible");
    }

    @Cuando("ingresa usuario {string} y contraseña {string}")
    public void ingresarCredenciales(String usuario, String password) {
        loginPage.enterUsername(usuario);
        loginPage.enterPassword(password);
        loginPage.clickLogin();
    }

    @Entonces("el sistema redirige al dashboard")
    public void sistemaRedirigeDashboard() {
        assertTrue(dashboardPage.isDashboardDisplayed(), "El dashboard no se muestra");
    }

    @Entonces("muestra el mensaje de error {string}")
    public void muestraMensajeError(String mensajeEsperado) {
        String mensajeActual = loginPage.getErrorMessage();
        assertTrue(mensajeActual.contains(mensajeEsperado), 
            "Mensaje esperado: " + mensajeEsperado + " pero se obtuvo: " + mensajeActual);
    }
}