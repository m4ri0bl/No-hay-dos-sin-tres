package com.eventmanager.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Clase para gestionar la configuración de la API de Supabase.
 */
public class SupabaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(SupabaseConfig.class);
    private static final Properties properties = new Properties();

    private static String supabaseUrl;
    private static String supabaseKey;
    private static String supabaseServiceKey;

    static {
        try {
            // Cargar propiedades desde archivo de configuración si existe
            try (InputStream input = SupabaseConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (input != null) {
                    properties.load(input);
                    logger.info("Archivo de propiedades cargado correctamente");
                }
            } catch (IOException e) {
                logger.warn("No se pudo cargar el archivo de propiedades", e);
            }

            // Obtener variables de entorno o usar valores del archivo de propiedades
            supabaseUrl = System.getenv("SUPABASE_URL");
            supabaseKey = System.getenv("SUPABASE_KEY");
            supabaseServiceKey = System.getenv("SUPABASE_SERVICE_KEY");

            if (supabaseUrl == null || supabaseUrl.isEmpty()) {
                supabaseUrl = properties.getProperty("supabase.url", "https://your-project.supabase.co");
                logger.warn("SUPABASE_URL no encontrado en variables de entorno, usando valor por defecto: {}", supabaseUrl);
            } else {
                logger.info("SUPABASE_URL configurado desde variables de entorno");
            }

            if (supabaseKey == null || supabaseKey.isEmpty()) {
                supabaseKey = properties.getProperty("supabase.key", "your-anon-key");
                logger.warn("SUPABASE_KEY no encontrado en variables de entorno, usando valor por defecto");
            } else {
                logger.info("SUPABASE_KEY configurado desde variables de entorno");
            }

            if (supabaseServiceKey == null || supabaseServiceKey.isEmpty()) {
                supabaseServiceKey = properties.getProperty("supabase.service_key", "your-service-key");
                logger.warn("SUPABASE_SERVICE_KEY no encontrado en variables de entorno, usando valor por defecto");
            } else {
                logger.info("SUPABASE_SERVICE_KEY configurado desde variables de entorno");
            }

            // Eliminar la barra final de la URL si existe
            if (supabaseUrl.endsWith("/")) {
                supabaseUrl = supabaseUrl.substring(0, supabaseUrl.length() - 1);
                logger.info("Se eliminó la barra final de la URL de Supabase");
            }

            logger.info("Configuración de Supabase inicializada. URL: {}", supabaseUrl);
        } catch (Exception e) {
            logger.error("Error al inicializar la configuración de Supabase", e);
            throw new RuntimeException("Error al inicializar la configuración de Supabase", e);
        }
    }

    /**
     * Obtiene la URL base de la API de Supabase.
     * @return URL de Supabase
     */
    public static String getSupabaseUrl() {
        return supabaseUrl;
    }

    /**
     * Obtiene la clave anónima de la API de Supabase.
     * @return Clave anónima de Supabase
     */
    public static String getSupabaseKey() {
        return supabaseKey;
    }

    /**
     * Obtiene la clave de servicio de la API de Supabase.
     * @return Clave de servicio de Supabase
     */
    public static String getSupabaseServiceKey() {
        return supabaseServiceKey;
    }

    /**
     * Verifica si la configuración de Supabase es válida.
     * @return true si la configuración es válida, false en caso contrario
     */
    public static boolean isConfigValid() {
        boolean isValid = supabaseUrl != null && !supabaseUrl.isEmpty() &&
                supabaseKey != null && !supabaseKey.isEmpty() &&
                !supabaseUrl.equals("https://your-project.supabase.co") &&
                !supabaseKey.equals("your-anon-key");

        if (!isValid) {
            logger.error("Configuración de Supabase no válida. URL: {}", supabaseUrl);
        }

        return isValid;
    }
}

