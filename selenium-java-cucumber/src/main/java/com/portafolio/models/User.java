package com.portafolio.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * <p>Se mapea con Jackson desde {@code users.json}.</p>
 *
 * @author Erick
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("email")
    private String email;

    @JsonProperty("role")
    private String role;

    // Constructor vacío requerido por Jackson
    public User() {
    }

    // Constructor de conveniencia
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // ═══════════════════ GETTERS ═══════════════════

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getRole() { return role; }

    // ═══════════════════ SETTERS ═══════════════════

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return "User{username='" + username + "', role='" + role + "'}";
    }
}