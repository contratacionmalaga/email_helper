package local.jarios.email.infraestructura;

import local.jarios.email.config.CorreoConfig;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import local.jarios.email.exception.EmailException;
import local.jarios.properties.config.PropertiesManager;
import local.jarios.properties.exception.PropertiesLoadException;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * Fábrica responsable de crear sesiones de correo autenticadas utilizando
 * los datos proporcionados por {@link CorreoConfig}.
 *
 * <p>Esta clase encapsula la configuración de autenticación necesaria para
 * establecer una conexión SMTP segura mediante JavaMail.</p>
 *
 * <p>La configuración se basa en {@link java.util.Properties} cargadas con
 * {@code PropertiesManager}, y complementadas con credenciales de acceso
 * recuperadas (y posiblemente descifradas) desde {@link CorreoConfig}.</p>
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
public class SesionCorreoFactory {

    /**
     * Crea una nueva instancia de {@link Session} autenticada con usuario y clave.
     *
     * @param config configuración de correo previamente cargada con {@link CorreoConfig}
     * @return instancia de sesión JavaMail autenticada
     * @throws EmailException si ocurre un error al cargar las propiedades
     */
    public Session crearSesion(CorreoConfig config) {
        log.debug("Creando sesión de correo para el host: {}", config.getHost());

        try {
            PropertiesManager propertiesManager = PropertiesManager.getInstance();
            Properties properties = propertiesManager.getProperties("email");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    log.debug("Autenticando con el usuario: {}", config.getUser());
                    return new PasswordAuthentication(config.getUser(), config.getPassword());
                }
            });

            log.info("Sesión de correo creada correctamente para el servidor: {}", config.getHost());
            return session;

        } catch (PropertiesLoadException e) {
            log.error("Error al cargar las propiedades de la sesión de correo", e);
            throw new EmailException("No se pudieron cargar las propiedades de configuración de correo", e);
        }
    }
}
