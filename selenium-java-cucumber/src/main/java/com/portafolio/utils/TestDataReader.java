package com.portafolio.utils;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portafolio.models.User;

/**
 * Lector de datos de prueba desde archivos JSON.
 *
 * <p><b>Responsabilidad única:</b> cargar y deserializar datos de prueba.
 * No conoce a Cucumber ni a Selenium. Puede ser usado por cualquier runner.</p>
 *
 * <p><b>Ubicación de los datos:</b> {@code src/test/resources/testdata/}.
 * Al estar en el classpath de test, se accede vía {@code getResourceAsStream},
 * lo que funciona tanto en local como en CI.</p>
 *
 * @author Erick
 */
public final class TestDataReader {

    private static final Logger log = LoggerFactory.getLogger(TestDataReader.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private TestDataReader() {
        // Clase de utilidad
    }

    /**
     * Lee un usuario desde {@code testdata/users.json}.
     *
     * @param userKey clave del usuario en el JSON (ej. "validUser", "lockedUser")
     * @return el {@link User} deserializado
     * @throws IllegalArgumentException si la clave no existe en el archivo
     * @throws RuntimeException si el archivo no se encuentra o falla la lectura
     */
    public static User getUser(String userKey) {
        log.debug("📖 Leyendo usuario '{}' desde users.json", userKey);

        JsonNode root = readJsonFile("testdata/users.json");
        JsonNode userNode = root.get(userKey);

        if (userNode == null) {
            throw new IllegalArgumentException(
                    "Usuario no encontrado en users.json: '" + userKey + "'. " +
                    "Claves disponibles: " + getAvailableKeys(root));
        }

        try {
            User user = MAPPER.treeToValue(userNode, User.class);
            log.debug("✅ Usuario cargado: {}", user);
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Error deserializando usuario: " + userKey, e);
        }
    }

    /**
     * Lee un archivo JSON del classpath y devuelve el nodo raíz.
     */
    private static JsonNode readJsonFile(String resourcePath) {
        try (InputStream is = TestDataReader.class
                .getClassLoader()
                .getResourceAsStream(resourcePath)) {

            if (is == null) {
                throw new RuntimeException("Archivo no encontrado en classpath: " + resourcePath);
            }
            return MAPPER.readTree(is);

        } catch (IOException e) {
            throw new RuntimeException("Error leyendo archivo JSON: " + resourcePath, e);
        }
    }

    private static String getAvailableKeys(JsonNode root) {
        StringBuilder sb = new StringBuilder();
        root.fieldNames().forEachRemaining(name -> sb.append(name).append(" "));
        return sb.toString().trim();
    }
}