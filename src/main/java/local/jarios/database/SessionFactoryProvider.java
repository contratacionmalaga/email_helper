package local.jarios.database;

import local.jarios.exceptions.MiSessionFactoryProviderException;
import local.jarios.utils.Mensajes;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import java.util.Properties;

/**
 * Description:
 * Author: juan
 * Date: 28/12/2024
 * Team:
 */
@Slf4j
public class SessionFactoryProvider {

    private static final String CONFIG_PACKAGE_NAME = "local.jarios.entity";

    public SessionFactoryProvider() { /* CONSTRUCTOR VACÍO */}

    public SessionFactory getSessionFactory(Properties hibernateProperties) throws MiSessionFactoryProviderException {

        /// Creo el objeto HibernateConfigurer con el las propiedades leídas desde los ficheros
        var hibernateConfigurer = new HibernateConfigurer();

        /// Obtener la configuración de Hibernate particular para la conexión
        var configuration = hibernateConfigurer.buildConfiguration(hibernateProperties);

        /// Aquí podemos agregar el escaneo de entidades y la configuración del DataSource, si es necesario
        var entityScanner = new EntityScanner();

        ///
        entityScanner.scanAndAddEntities(configuration, CONFIG_PACKAGE_NAME);

        try {

            /// Obtengo el objeto SessionFactory
            return configuration.buildSessionFactory();

        } catch (HibernateException ex) {

            /// Obtener la pila de ejecución
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

            /// El primer elemento de stackTrace es getStackTrace(), el segundo es el método actual
            String className = stackTrace[1].getClassName();    /// Nombre de la clase
            String methodName = stackTrace[1].getMethodName();  /// Nombre del método

            /// Registro la excepción
            log.error(Mensajes.EXCEPTION_ERROR, className, methodName, ex.getMessage());

            /// Devuelvo la excepción
            throw new MiSessionFactoryProviderException(ex);
        }
    }
}
