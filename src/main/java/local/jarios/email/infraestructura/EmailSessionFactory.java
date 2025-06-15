package local.jarios.email.infraestructura;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import local.jarios.email.exception.EmailServiceException;
import local.jarios.email.exception.EmailSessionFactoryException;
import local.jarios.email.utils.Constantes;
import local.jarios.properties.config.PropertiesManager;
import local.jarios.properties.exception.PropertiesLoadException;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * Fábrica responsable de crear sesiones de correo autenticadas.
 *
 * <p>Esta clase encapsula la configuración de autenticación necesaria para
 * establecer una conexión SMTP segura mediante JavaMail.</p>
 *
 * <p>La configuración se basa en {@link java.util.Properties} cargadas con
 * {@code PropertiesManager}, y complementadas con credenciales de acceso
 * recuperadas (y posiblemente descifradas).</p>
 *
 * <p>Ejemplo de uso:</p>
 * <pre>{@code
 * CorreoConfig config = new CorreoConfig();
 * config.cargarConfiguracion("config/correo.properties", "miClaveMaestra");
 * Session session = new SesionCorreoFactory().crearSesion(config);
 * }</pre>
 *
 * @author Juan
 * @since 1.0
 */
@Slf4j
public class EmailSessionFactory {

    /**
     * Constructor vacío
     */
    public EmailSessionFactory() {
        // Constructor vacío
    }

    /**
     * Crea una nueva instancia de {@link Session} autenticada con usuario y clave.
     *
     * @param propertiesManager objeto que contiene las propiedades asociadas al proyecto
     * @param user usuario con el que nos autenticamos para obtener la sesión
     * @param password clave utilizada para la autenticación y posteriorobtención de la sesión
     * @return instancia de sesión JavaMail autenticada
     * @throws EmailServiceException si ocurre un error al cargar las propiedades
     */
    public Session getSession(
            PropertiesManager propertiesManager,
            String user,
            String password
    ) throws EmailSessionFactoryException {
        log.debug("[getSession] - Obteniendo una sesió para el usuario: {}", user);

        try {

            Properties properties = propertiesManager.getProperties(Constantes.EMAIL_PROPERTIES);
            log.debug("[getSession] - Obtenidas las propiedades del fichero: {}", Constantes.EMAIL_PROPERTIES);

            log.debug("[getSession] - Impresión de las propiedades del fichero: {}", Constantes.EMAIL_PROPERTIES);
            propertiesManager.printProperties(properties);

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    log.debug("Autenticando con: <{}, {}>", user, password);
                    return new PasswordAuthentication(user, password);
                }
            });

            log.debug("Sesión de correo creada correctamente para el usuario: {}", user);
            return session;

        } catch (PropertiesLoadException e) {
            log.error("Error al cargar las propiedades de la sesión de correo", e);
            throw new EmailSessionFactoryException("Error al cargar las porpiedades del properties de correo", e);
        }
    }
}

