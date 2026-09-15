package com.portafolio.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.portafolio.pages.HomePage;
import com.portafolio.utils.AlertUtils;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.qameta.allure.Allure;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

/**
 * Step Definitions compartidos entre múltiples features.
 *
 * <p><b>Responsabilidad:</b> albergar steps genéricos que se reutilizan en
 * varios features (login, signup, carrito, checkout, etc.). Evita duplicar
 * definiciones de steps en clases distintas, lo cual causa
 * {@code DuplicateStepDefinitionException} en Cucumber.</p>
 *
 * @author Erick
 */
public class CommonSteps {

    private static final Logger log = LoggerFactory.getLogger(CommonSteps.class);

    private final HomePage homePage = new HomePage();

    // ═══════════════════ GIVEN ═══════════════════

    @Dado("que el usuario está en la página principal de DemoBlaze")
    @Story("Acceso a la tienda")
    @Severity(SeverityLevel.BLOCKER)
    public void usuarioEnPaginaPrincipal() {
        log.info("📋 Given: usuario en la página principal de DemoBlaze");

        Allure.step("Navegar a la página principal de DemoBlaze", () -> {
            homePage.navigateTo();
        });

        Allure.step("Verificar que la página principal está cargada", () -> {
            Assert.assertTrue(homePage.isPageLoaded(), "La página principal no cargó");
        });
    }

    // ═══════════════════ WHEN ═══════════════════

    @Cuando("hace click en el enlace {string}")
    @Story("Navegación")
    @Severity(SeverityLevel.NORMAL)
    public void hacerClickEnEnlace(String linkText) {
        log.info("🎬 When: click en enlace '{}'", linkText);

        Allure.step("Hacer click en el enlace: " + linkText, () -> {
            homePage.clickOnLink(linkText);
        });
    }

    // ═══════════════════ THEN ═══════════════════

    @Entonces("debería ver un alert con el mensaje {string}")
    @Story("Validación de alert")
    @Severity(SeverityLevel.BLOCKER)
    public void deberiaVerAlertConMensaje(String expectedMessage) {
        log.info("✅ Then: validando alert con mensaje '{}'", expectedMessage);

        String actualMessage = Allure.step("Capturar texto del alert", () -> {
            return AlertUtils.getAlertText();
        });

        Allure.step("Validar mensaje del alert", () -> {
            Assert.assertEquals(actualMessage, expectedMessage,
                    "El mensaje del alert no coincide");
        });
    }

    @Entonces("acepta el alert")
    @Story("Validación de alert")
    @Severity(SeverityLevel.NORMAL)
    public void aceptaElAlert() {
        log.info("✅ Then: aceptando alert");
        Allure.step("Aceptar el alert", () -> AlertUtils.acceptAlert());
    }
}