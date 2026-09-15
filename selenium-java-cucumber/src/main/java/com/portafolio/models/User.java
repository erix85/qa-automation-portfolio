package com.portafolio.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO que representa un usuario de prueba.
 *
 * <p><b>¿Por qué un DTO?</b> Los Strings sueltos ("standard_user", "secret_sauce")
 * son propensos a errores tipográficos, difíciles de mantener y no expresan
 * la intención del negocio. Un DTO tipado:</p>
 * <ul>
 *   <li>Centraliza los datos en un solo lugar.</li>
 *   <li>El IDE autocompleta los campos (sin typos).</li>
 *   <li>Documenta qué campos tiene un usuario.</li>
 * </ul>
 *
 * <p><b>Campos de runtime:</b> los campos {@code createdAt} y {@code source} se
 * rellenan cuando el usuario se genera dinámicamente durante la ejecución
 * (ver {@link com.portafolio.utils.GeneratedUserStore}).</p>
 *
 * <p>Se mapea con Jackson desde {@code users.json} y desde
 * {@code target/generated-users.json}.</p>
 *
 * @author Erick
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("email")
    private String email;

    @JsonProperty("role")
    private String role;

    // ═══════════════════ METADATOS DE RUNTIME ═══════════════════

    /** Timestamp en formato {@code yyyy-MM-dd HH:mm:ss} de la creación del usuario. */
    @JsonProperty("createdAt")
    private String createdAt;

    /** Origen del usuario, ej. {@code signup.feature}, {@code users.json}, {@code api}. */
    @JsonProperty("source")
    private String source;

    // ═══════════════════ CONSTRUCTORES ═══════════════════

    /** Constructor vacío requerido por Jackson. */
    public User() {
    }

    /** Constructor de conveniencia para username + password. */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /** Constructor completo para escenarios que requieren todos los campos. */
    public User(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    // ═══════════════════ GETTERS ═══════════════════

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getCreatedAt() { return createdAt; }
    public String getSource() { return source; }

    // ═══════════════════ SETTERS ═══════════════════

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setSource(String source) { this.source = source; }

    // ═══════════════════ UTILIDADES ═══════════════════

    /**
     * {@inheritDoc}
     *
     * <p><b>Nota de seguridad:</b> la contraseña no se incluye en la representación
     * para evitar fugas en logs y reportes.</p>
     */
    @Override
    public String toString() {
        return "User{username='" + username + "', role='" + role + "'}";
    }
}