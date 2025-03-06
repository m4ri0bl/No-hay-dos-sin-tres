package com.eventmanager.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase para gestionar la conexión a la base de datos Supabase.
 */
public class DatabaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    private static HikariDataSource dataSource;
    private static final Properties properties = new Properties();

    static {
        try {
            // Cargar propiedades desde archivo de configuración si existe
            try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (input != null) {
                    properties.load(input);
                    logger.info("Archivo de propiedades cargado correctamente");
                }
            } catch (IOException e) {
                logger.warn("No se pudo cargar el archivo de propiedades", e);
            }
            
            // Configuración del pool de conexiones HikariCP
            HikariConfig config = new HikariConfig();
            
            // Obtener variables de entorno o usar valores del archivo de propiedades o por defecto
            String dbUrl = System.getenv("SUPABASE_JDBC_URL");
            String dbUser = System.getenv("SUPABASE_DB_USER");
            String dbPassword = System.getenv("SUPABASE_DB_PASSWORD");
            
            if (dbUrl == null || dbUrl.isEmpty()) {
                dbUrl = properties.getProperty("db.url", "jdbc:postgresql://db.example.supabase.co:5432/postgres");
                dbUser = properties.getProperty("db.user", "postgres");
                dbPassword = properties.getProperty("db.password", "tu_contraseña");
                
                logger.warn("Usando configuración de base de datos por defecto. Configura las variables de entorno en producción.");
            }
            
            config.setJdbcUrl(dbUrl);
            config.setUsername(dbUser);
            config.setPassword(dbPassword);
            
            // Configuración adicional del pool
            config.setMaximumPoolSize(Integer.parseInt(properties.getProperty("db.pool.maxSize", "10")));
            config.setMinimumIdle(Integer.parseInt(properties.getProperty("db.pool.minIdle", "2")));
            config.setIdleTimeout(Long.parseLong(properties.getProperty("db.pool.idleTimeout", "30000")));
            config.setConnectionTimeout(Long.parseLong(properties.getProperty("db.pool.connectionTimeout", "30000")));
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            
            dataSource = new HikariDataSource(config);
            
            logger.info("Pool de conexiones inicializado correctamente");
        } catch (Exception e) {
            logger.error("Error al inicializar el pool de conexiones", e);
            throw new RuntimeException("Error al inicializar la conexión a la base de datos", e);
        }
    }

    /**
     * Obtiene una conexión del pool de conexiones.
     * @return Conexión a la base de datos
     * @throws SQLException Si ocurre un error al obtener la conexión
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null || dataSource.isClosed()) {
            throw new SQLException("El pool de conexiones no está disponible");
        }
        return dataSource.getConnection();
    }

    /**
     * Cierra el pool de conexiones.
     */
    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("Pool de conexiones cerrado correctamente");
        }
    }
    
    /**
     * Verifica si la conexión a la base de datos está disponible.
     * @return true si la conexión está disponible, false en caso contrario
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            logger.error("Error al probar la conexión a la base de datos", e);
            return false;
        }
    }
}

