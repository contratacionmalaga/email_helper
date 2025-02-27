package local.jarios.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import local.jarios.enums.DbConfig;
import local.jarios.enums.Permiso;
import local.jarios.properties.PropertyConstantes;
import local.jarios.properties.PropertyManager;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Description:
 * Author: juan
 * Date: 28/12/2024
 * Team:
 */
@Getter
@Setter
public class DatabaseConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConfig.class);

    /// Propiedades relacionadas con la conexión a la base de datos
    private String jdbcUrl;
    private String driver;
    private String username;
    private String password;
    private String dialect;
    private String showSql;
    private String formatSql;
    private String highlightSql;
    private String hbm2ddl;
    private String jtaPlatform;
    private String ssl;
    private Permiso permiso;

    /// Propiedades de HikariCP
    private HikariConfig hikariConfig;
    private HikariDataSource hikariDataSource;


    /**
     * Constructor para crear una configuración de base de datos desde el fichero de propiedades.
     */
    public DatabaseConfig(DbConfig dbConfig, Permiso permiso, PropertyManager propertyManager)  {

        /// Mapa de configuraciones por tipo
        Map<DbConfig, String[]> configMap = Map.of(
                DbConfig.PRINCIPAL, new String[]{
                        propertyManager.getProperty(PropertyConstantes.HIBERNATE_PRINCIPAL_URL),
                        propertyManager.getProperty(PropertyConstantes.HIBERNATE_PRINCIPAL_DRIVER),
                        propertyManager.getProperty(PropertyConstantes.HIBERNATE_PRINCIPAL_DIALECT),
                        propertyManager.getProperty(PropertyConstantes.HIBERNATE_PRINCIPAL_USERNAME),
                        propertyManager.getProperty(PropertyConstantes.HIBERNATE_PRINCIPAL_PASSWORD)
                }
        );

        /// Consulto en el Map el tipo de DbConfig (ENUMERADO)
        String[] configKeys = configMap.get(dbConfig);

        /// Si existe el tipo de DbConfig en el Map cargo el resto de valores que son comunes para todas las DbConfig
        if (configKeys != null) {

            /// Si la configuración es válida, inicializamos con las claves correspondientes
            initializeConfig(configKeys, permiso, propertyManager);

        } else {

            ///
            LOGGER.error("Tipo de configuración de base de datos no reconocido. Configuración: {}", dbConfig);
        }
    }

    /**
     * Inicializa la configuración de la base de datos a partir de las propiedades proporcionadas.
     *
     * @param configKeys Las claves de configuración necesarias para inicializar la conexión.
     */
    private void initializeConfig(String[] configKeys, Permiso permiso, PropertyManager propertyManager) {

        /// Configuración de las propidades dependientes de la conexión
        this.jdbcUrl = configKeys[0];
        this.driver = configKeys[1];
        this.dialect = configKeys[2];
        this.username = configKeys[3];
        this.password = configKeys[4];

        ///  Configuración de las propiedades comunes
        this.showSql = propertyManager.getProperty(PropertyConstantes.HIBERNATE_SHOW_SQL);
        this.formatSql = propertyManager.getProperty(PropertyConstantes.HIBERNATE_FORMAT_SQL);
        this.highlightSql = propertyManager.getProperty(PropertyConstantes.HIBERNATE_HIGHLIGHT_SQL);
        this.hbm2ddl = propertyManager.getProperty(PropertyConstantes.HIBERNATE_HBM2DDL_AUTO);
        this.jtaPlatform = propertyManager.getProperty(PropertyConstantes.HIBERNATE_JTA_PLATFORM);
        this.ssl = propertyManager.getProperty(PropertyConstantes.HIBERNATE_SSL);
        this.permiso = permiso;

        /// Asignar las propiedades de HikariCP
        configureHikariCP(propertyManager);

    }

    /**
     * Configura el HikariDataSource con las propiedades leídas.
     */
    private void configureHikariCP(PropertyManager propertyManager) {

        ///
        hikariConfig = new HikariConfig();

        /// Asignar las propiedades de HikariCP
        hikariConfig.setJdbcUrl(this.jdbcUrl);
        hikariConfig.setUsername(this.username);
        hikariConfig.setPassword(this.password);
        hikariConfig.setDriverClassName(this.driver);
        hikariConfig.setPoolName(propertyManager.getProperty(PropertyConstantes.HIBERNATE_HIKARI_POOLNAME));

        hikariConfig.setMaximumPoolSize(Integer.parseInt(propertyManager.getProperty(PropertyConstantes.HIBERNATE_HIKARI_MAXIMUMPOOLSIZE)));
        hikariConfig.setMinimumIdle(Integer.parseInt(propertyManager.getProperty(PropertyConstantes.HIBERNATE_HIKARI_MINIMUMIDLE)));
        hikariConfig.setConnectionTimeout(30000);
        hikariConfig.setIdleTimeout(600000);
        hikariConfig.setMaxLifetime(1800000);

        hikariDataSource = new HikariDataSource(hikariConfig);
    }
}
