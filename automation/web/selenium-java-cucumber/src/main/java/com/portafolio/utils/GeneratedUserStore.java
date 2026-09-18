package com.portafolio.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.portafolio.models.User;

/**
 * Almacén de usuarios generados en runtime.
 *
 * <p><b>Responsabilidad única:</b> persistir en disco los usuarios que se crean
 * dinámicamente durante la ejecución de los tests, para reutilizarlos
 * posteriormente (por ejemplo, en el feature de login).</p>
 *
 * <p><b>Ubicación del archivo:</b> {@code target/generated-users.json}. Se guarda
 * en {@code target/} (no en {@code src/}) por tres razones:</p>
 * <ul>
 *   <li>No ensucia el repositorio Git.</li>
 *   <li>Maven limpia {@code target/} con cada {@code mvn clean}.</li>
 *   <li>Es un artefacto de build, no código fuente.</li>
 * </ul>
 *
 * <p><b>Concurrencia:</b> todos los métodos son {@code synchronized} para evitar
 * corrupción del archivo cuando se ejecutan tests en paralelo dentro de la misma JVM.</p>
 *
 * @author Erick
 */
public final class GeneratedUserStore {

    private static final Logger log = LoggerFactory.getLogger(GeneratedUserStore.class);

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    private static final Path FILE_PATH = Paths.get("target", "generated-users.json");

    private GeneratedUserStore() {
        // Clase de utilidad, no instanciable
    }

    // ═══════════════════ ESCRITURA ═══════════════════

    /**
     * Guarda un usuario generado en el archivo runtime.
     * Si el archivo ya existe, añade el usuario a la lista existente.
     *
     * @param user el usuario a guardar (no puede ser {@code null})
     */
    public static synchronized void save(User user) {
        if (user == null || user.getUsername() == null) {
            log.warn("⚠️ Intento de guardar usuario inválido, ignorado");
            return;
        }

        try {
            List<User> users = readAll();
            users.add(user);

            ensureParentDirectoryExists();
            MAPPER.writeValue(FILE_PATH.toFile(), users);

            log.info("💾 Usuario guardado en {}: {}", FILE_PATH, user.getUsername());

        } catch (IOException e) {
            log.error("❌ Error guardando usuario en {}: {}", FILE_PATH, e.getMessage());
        }
    }

    // ═══════════════════ LECTURA ═══════════════════

    /**
     * Lee todos los usuarios generados previamente.
     *
     * @return lista de usuarios (vacía si el archivo no existe)
     */
    public static synchronized List<User> readAll() {
        if (!Files.exists(FILE_PATH)) {
            return new ArrayList<>();
        }

        try {
            User[] users = MAPPER.readValue(FILE_PATH.toFile(), User[].class);
            return new ArrayList<>(List.of(users));
        } catch (IOException e) {
            log.error("❌ Error leyendo {}: {}", FILE_PATH, e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Busca un usuario por su username.
     *
     * @param username el username a buscar
     * @return el usuario, o {@code null} si no se encuentra
     */
    public static synchronized User findByUsername(String username) {
        return readAll().stream()
                .filter(u -> username.equals(u.getUsername()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Devuelve el último usuario guardado (el más reciente).
     *
     * @return el último usuario, o {@code null} si no hay ninguno
     */
    public static synchronized User getLastCreated() {
        List<User> users = readAll();
        return users.isEmpty() ? null : users.get(users.size() - 1);
    }

    /**
     * Devuelve el total de usuarios generados en la sesión.
     */
    public static synchronized int count() {
        return readAll().size();
    }

    // ═══════════════════ MANTENIMIENTO ═══════════════════

    /**
     * Limpia el archivo (útil para tests que necesitan partir de cero).
     */
    public static synchronized void clear() {
        try {
            boolean deleted = Files.deleteIfExists(FILE_PATH);
            if (deleted) {
                log.info("🧹 Archivo de usuarios generados limpiado: {}", FILE_PATH);
            } else {
                log.debug("No había archivo que limpiar en {}", FILE_PATH);
            }
        } catch (IOException e) {
            log.error("❌ Error limpiando {}: {}", FILE_PATH, e.getMessage());
        }
    }

    // ═══════════════════ HELPERS PRIVADOS ═══════════════════

    private static void ensureParentDirectoryExists() throws IOException {
        Path parent = FILE_PATH.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }
}