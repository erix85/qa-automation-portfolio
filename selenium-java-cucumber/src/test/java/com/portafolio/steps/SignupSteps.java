package com.portafolio.steps;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.portafolio.models.User;
import com.portafolio.pages.SignupPage;
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
 * Step Definitions específicos del feature de signup.
 *
 * <p><b>Nota:</b> los steps compartidos (navegación, validación de alerts,
 * etc.) viven en {@link CommonSteps}. Aquí solo los específicos del signup.</p>
 *
 * <p><b>Persistencia:</b> cuando se registra un usuario nuevo, se guarda en
 * {@code target/generated-users.json} a través de {@link GeneratedUserStore}.
 * Esto permite reutilizarlo en el feature de login u otros.</p>
 *
 * @author Erick
 */
@Epic("Autenticación")
@Feature("Registro de usuarios")
public class SignupSteps {

    private static final Logger log = LoggerFactory.getLogger(SignupSteps.class);
    private static final DateTimeFormatter TIMESTAMP =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final SignupPage signupPage = new SignupPage();

    // ═══════════════════ WHEN ═══════════════════

    /**
     * Completa el formulario con un usuario único generado en runtime.
     *
     * <p>El usuario se guarda en {@code target/generated-users.json} <b>antes</b>
     * de enviarlo al navegador, para que quede registro incluso si el envío falla.</p>
     */
    @Cuando("completa el formulario de registro con un usuario único y contraseña {string}")
    @Story("Registro de usuario")
    @Severity(SeverityLevel.CRITICAL)
    public void completarFormularioConUsuarioUnico(String password) {
        String uniqueUsername = generateUniqueUsername();
        log.info("🎬 When: registrando usuario único '{}'", uniqueUsername);

        // 1) Construir el DTO con metadatos
        User newUser = new User();
        newUser.setUsername(uniqueUsername);
        newUser.setPassword(password);
        newUser.setRole("CUSTOMER");
        newUser.setCreatedAt(LocalDateTime.now().format(TIMESTAMP));
        newUser.setSource("signup.feature");

        // 2) Persistir el usuario en el archivo runtime ANTES de enviarlo al navegador
        GeneratedUserStore.save(newUser);

        // 3) Adjuntar evidencia a Allure
        Allure.addAttachment(
                "Usuario generado en runtime",
                "application/json",
                newUser.toString(),
                ".txt");

        // 4) Escribir en el formulario
        Allure.step("Ingresar usuario único: " + uniqueUsername, () -> {
            signupPage.enterUsername(uniqueUsername);
        });

        Allure.step("Ingresar contraseña", () -> {
            signupPage.enterPassword(password);
        });
    }

    /**
     * Completa el formulario con un usuario específico (para escenarios negativos).
     */
    @Cuando("completa el formulario de registro con usuario {string} y contraseña {string}")
    @Story("Registro de usuario")
    @Severity(SeverityLevel.CRITICAL)
    public void completarFormularioConUsuario(String username, String password) {
        log.info("🎬 When: completando formulario con usuario '{}'", username);

        Allure.step("Ingresar usuario: " + username, () -> {
            signupPage.enterUsername(username);
        });

        Allure.step("Ingresar contraseña", () -> {
            signupPage.enterPassword(password);
        });
    }

    @Cuando("confirma el registro")
    @Story("Registro de usuario")
    @Severity(SeverityLevel.CRITICAL)
    public void confirmarRegistro() {
        log.info("🎬 When: confirmando registro");
        Allure.step("Click en el botón Sign up", () -> {
            signupPage.clickSignupButton();
        });
    }

    // ═══════════════════ THEN ═══════════════════

    /**
     * Verifica que el usuario recién creado quedó persistido en el archivo runtime.
     *
     * <p>Este step es opcional, se ejecuta al final del escenario positivo y sirve
     * como sanity check de la persistencia.</p>
     */
    @Entonces("el usuario recién creado queda guardado para futuros tests")
    @Story("Registro exitoso")
    @Severity(SeverityLevel.NORMAL)
    public void usuarioRecienCreadoGuardado() {
        User lastUser = GeneratedUserStore.getLastCreated();
        Assert.assertNotNull(lastUser, "No se encontró ningún usuario guardado");

        log.info("✅ Usuario recién creado disponible: {}", lastUser.getUsername());

        Allure.step("Verificar persistencia del usuario", () -> {
            Assert.assertNotNull(lastUser.getCreatedAt(),
                    "El usuario no tiene timestamp de creación");
            Assert.assertTrue(GeneratedUserStore.count() >= 1,
                    "El archivo de usuarios generados está vacío");
        });
    }

    // ═══════════════════ HELPERS ═══════════════════

    /**
     * Genera un username único basado en timestamp + número aleatorio.
     *
     * <p><b>Por qué:</b> DemoBlaze no permite registrar el mismo usuario dos veces.
     * Un username único garantiza que el escenario positivo sea idempotente.</p>
     *
     * @return username único, ej. {@code qa_user_1695432123456_4821}
     */
    private String generateUniqueUsername() {
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 10000);
        return "qa_user_" + timestamp + "_" + random;
    }
}