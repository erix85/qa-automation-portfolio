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
 * <p><b>Prioridad de propiedades:</b> las propiedades del sistema
 * ({@code -Dclave=valor}) tienen prioridad sobre las del archivo
 * {@code qa.properties}. Esto permite sobrescribir valores sin modificar el
 * archivo, ideal para CI/CD y ejecuciones específicas.</p>
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

    // ═══════════════════ URLs ═══════════════════

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

    // ═══════════════════ TIPOS TIPADOS ═══════════════════

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    // ═══════════════════ SELENIUM GRID ═══════════════════

    /**
     * Indica si el framework está configurado para usar Selenium Grid.
     *
     * <p>Prioriza la propiedad del sistema ({@code -Dgrid.url=...}) sobre el
     * archivo {@code qa.properties}.</p>
     *
     * @return {@code true} si {@code grid.url} está definida y no vacía
     */
    public static boolean isGridEnabled() {
        return !getGridUrl().isEmpty();
    }

    /**
     * Devuelve la URL del Selenium Grid, priorizando la propiedad del sistema.
     *
     * @return la URL del Grid, o cadena vacía si no está configurada
     */
    public static String getGridUrl() {
        // 1) Prioridad: propiedad del sistema
        String systemGridUrl = System.getProperty("grid.url", "").trim();
        if (!systemGridUrl.isEmpty()) {
            return systemGridUrl;
        }
        // 2) Fallback: archivo de configuración
        return PROPERTIES.getProperty("grid.url", "").trim();
    }

    // ═══════════════════ HELPERS PRIVADOS ═══════════════════

    /**
     * Devuelve el valor de una propiedad, priorizando las propiedades del sistema
     * sobre el archivo de configuración.
     */
    private static String get(String key) {
        // 1) Prioridad: propiedad del sistema
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.trim().isEmpty()) {
            return systemValue.trim();
        }

        // 2) Fallback: archivo de configuración
        String fileValue = PROPERTIES.getProperty(key);
        if (fileValue == null) {
            throw new IllegalArgumentException(
                    "Propiedad no encontrada ni en el sistema ni en " +
                    CONFIG_FILE + ": " + key);
        }
        return fileValue.trim();
    }
}