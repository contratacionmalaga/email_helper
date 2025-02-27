package local.jarios.database;

import local.jarios.properties.PropertyConstantes;
import local.jarios.properties.PropertyManager;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

/**
 * Description:
 * Author: juan
 * Date: 28/12/2024
 * Team:
 */
@Slf4j
public class SessionFactoryProvider {

    public SessionFactoryProvider() { /* CONSTRUCTOR VACÍO */}

    public SessionFactory getSessionFactory(
            DatabaseConfig databaseConfig,
            PropertyManager propertyManager) throws SessionFactoryProviderException {

        ///
        var properties = HibernateConfiguration.getProperties(databaseConfig);

        /// Obtener la configuración de Hibernate con los parámetros de la base de datos
        var configuration = HibernateConfiguration.getConfiguration(properties, databaseConfig.getHikariDataSource());

        /// Aquí podemos agregar el escaneo de entidades y la configuración del DataSource, si es necesario
        var entityScanner = new EntityScanner();

        ///
        entityScanner.scanAndAddEntities(configuration, propertyManager.getProperty(PropertyConstantes.CONFIG_PACKAGE_NAME));

        try {

            /// Obtengo el objeto SessionFactory
            return configuration.buildSessionFactory();

        } catch (HibernateException ex) {

            throw new SessionFactoryProviderException(ex.getMessage(), ex);
        }
    }
}
