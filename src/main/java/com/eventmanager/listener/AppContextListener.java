package com.eventmanager.listener;

import com.eventmanager.service.GuestService;
import com.eventmanager.util.SupabaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Listener para inicializar y liberar recursos al iniciar y detener la aplicación.
 */
@WebListener
public class AppContextListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(AppContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Inicializando la aplicación...");
        
        // Verificar la configuración de Supabase
        if (!SupabaseConfig.isConfigValid()) {
            logger.error("La configuración de Supabase no es válida. Por favor, verifica las variables de entorno.");
            sce.getServletContext().setAttribute("configError", "La configuración de Supabase no es válida. Por favor, verifica las variables de entorno.");
        } else {
            // Verificar la conexión a Supabase
            try {
                GuestService guestService = new GuestService();
                boolean connectionOk = guestService.testConnection();
                if (connectionOk) {
                    logger.info("Conexión a Supabase establecida correctamente");
                } else {
                    logger.error("No se pudo establecer la conexión a Supabase");
                    sce.getServletContext().setAttribute("configError", "No se pudo establecer la conexión a Supabase. Verifica las credenciales.");
                }
            } catch (Exception e) {
                logger.error("Error al verificar la conexión a Supabase", e);
                sce.getServletContext().setAttribute("configError", "Error al verificar la conexión a Supabase: " + e.getMessage());
            }
        }
        
        logger.info("Aplicación inicializada correctamente");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Deteniendo la aplicación...");
        
        // No hay recursos específicos que liberar para la API REST
        
        logger.info("Aplicación detenida correctamente");
    }
}

