package local.jarios.database;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.HikariCPSettings;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.cfg.SchemaToolingSettings;
import org.hibernate.cfg.TransactionSettings;

import java.util.Properties;


/**
 * Description:
 * Author: juan
 * Date: 28/12/2024
 * Team:
 */
@Slf4j
public class HibernateConfiguration {

    private HibernateConfiguration() { }

    /// Método que devuelve una configuración de Hibernate con base en una clase de configuración
    public static Configuration getConfiguration(Properties properties, HikariDataSource hikariDataSource) {

        log.info ("IMPRESIÓN DE PROPERTIES DESDE EL HIBERNATE CONFIGURATION");
        /// Imprimir las propiedades
        for (Object o : properties.keySet()) {
            String key = (String) o;
            String value = properties.getProperty(key);
            log.info("{} =  {}", key, value);
        }

        /// Crear la configuración de Hibernate
        var configuration = new Configuration();

        /// Configurar Hibernate
        configuration.setProperties(properties);

        configuration.setProperty("hibernate.hikari.maxLifetime", 1800000);
        configuration.setProperty("hibernate.hikari.minimumIdle", 5);
        configuration.setProperty("hibernate.hikari.idleTimeout", 600000);
        configuration.setProperty("hibernate.hikari.maximumPoolSize", 10);
        configuration.setProperty("hibernate.hikari.connectionTimeout", 30000);

        ///
        return configuration;
    }

    public static Properties getProperties(DatabaseConfig databaseConfig) {

        /// Establecer las propiedades de Hibernate utilizando el objeto DatabaseConfig
        var properties = new Properties();

        ///
        properties.put(JdbcSettings.SHOW_SQL, databaseConfig.getShowSql());
        properties.put(JdbcSettings.FORMAT_SQL, databaseConfig.getFormatSql());
        properties.put(JdbcSettings.HIGHLIGHT_SQL, databaseConfig.getHighlightSql());

        properties.put(JdbcSettings.JAKARTA_JDBC_URL, databaseConfig.getJdbcUrl());
        properties.put(JdbcSettings.JAKARTA_JDBC_DRIVER, databaseConfig.getDriver());
        properties.put(JdbcSettings.JAKARTA_JDBC_USER, databaseConfig.getUsername());
        properties.put(JdbcSettings.JAKARTA_JDBC_PASSWORD, databaseConfig.getPassword());

        properties.put(HikariCPSettings.HIKARI_POOL_NAME, databaseConfig.getHikariConfig().getPoolName());
        properties.put(HikariCPSettings.HIKARI_MAX_LIFETIME, databaseConfig.getHikariConfig().getMaxLifetime());
        properties.put(HikariCPSettings.HIKARI_IDLE_TIMEOUT, databaseConfig.getHikariConfig().getIdleTimeout());
        properties.put(HikariCPSettings.HIKARI_MAX_SIZE, databaseConfig.getHikariConfig().getMaximumPoolSize());
        properties.put(HikariCPSettings.HIKARI_MIN_IDLE_SIZE, databaseConfig.getHikariConfig().getMinimumIdle());

        properties.put(SchemaToolingSettings.HBM2DDL_AUTO, databaseConfig.getHbm2ddl());

        properties.put(TransactionSettings.JTA_PLATFORM, databaseConfig.getJtaPlatform());

        return properties;
    }
}
