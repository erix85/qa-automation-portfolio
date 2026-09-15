package com.portafolio.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Lector centralizado de configuración.
 *
 * <p>Responsabilidad única: cargar el archivo {@code qa.properties} del classpath
 * y exponer sus valores como métodos estáticos tipados.</p>
 *
 * <p>Principio de diseño: agnóstico al framework de testing. No conoce Cucumber,
 * JUnit ni TestNG.</p>
 *
 * @author Erick
 */
public final class ConfigReader {

    private static final Properties PROPERTIES = new Properties();
    private static final String CONFIG_FILE = "config/qa.properties";

    static {
        try (InputStream is = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {

            if (is == null) {
                throw new IllegalStateException(
                        "No se encontró " + CONFIG_FILE + " en el classpath");
            }
            PROPERTIES.load(is);

        } catch (IOException e) {
            throw new RuntimeException("Error cargando " + CONFIG_FILE, e);
        }
    }

    private ConfigReader() {
        // Clase de utilidad
    }

    /**
     * Devuelve la URL base de la aplicación (ej. https://www.demoblaze.com).
     */
    public static String getBaseUrl() {
        return get("base.url");
    }

    /**
     * Devuelve una ruta relativa (ej. /index.html, /cart.html).
     */
    public static String getPath(String key) {
        return get(key);
    }

    /**
     * Construye la URL completa: base.url + path.
     *
     * @param pathKey clave del path en el properties (ej. "home.path")
     */
    public static String getFullUrl(String pathKey) {
        return getBaseUrl() + get(pathKey);
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    private static String get(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException(
                    "Propiedad no encontrada en " + CONFIG_FILE + ": " + key);
        }
        return value.trim();
    }
}