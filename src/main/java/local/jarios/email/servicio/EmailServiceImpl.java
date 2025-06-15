package local.jarios.email.servicio;

import jakarta.mail.internet.MimeMessage;
import jakarta.mail.Session;
import local.jarios.email.dominio.EmailMensaje;
import local.jarios.email.exception.EmailServiceException;
import local.jarios.email.exception.EmailSessionFactoryException;
import local.jarios.email.infraestructura.EmailMimeMessage;
import local.jarios.email.infraestructura.EmailSender;
import local.jarios.email.infraestructura.EmailSessionFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * Implementación del servicio de envío de correos electrónicos.
 *
 * <p>Este servicio admite múltiples destinatarios y permite especificar el remitente manualmente.</p>
 * <p>Utiliza un {@link EmailSender} configurado con un {@link TransportSender} para realizar el envío.</p>
 *
 * @author Juan
 * @since 1.0
 */
@Slf4j
public class EmailServiceImpl implements EmailService {

    /**
     * Fábrica responsable de crear instancias de {@link Session}
     * configuradas con los parámetros SMTP definidos en el fichero properties.
     */
    private final EmailSessionFactory emailSesionFactory;

    /**
     * Generador de objetos {@link MimeMessage} a partir
     * de la información contenida en un {@code EmailMensaje}.
     */
    private final EmailMimeMessage enmailMimeMessage;

    /**
     * Servicio responsable de enviar mensajes de correo electrónico.
     */
    private final EmailSender enmailSender;

    /**
     * Constructor que inicializa los componentes necesarios para el envío de correos,
     * usando una implementación por defecto de {@link TransportSender}.
     */
    public EmailServiceImpl() {
        this.emailSesionFactory = new EmailSessionFactory();
        this.enmailMimeMessage = new EmailMimeMessage();
        this.enmailSender = new EmailSender(new TransportSenderImpl());
    }

    /**
     * Constructor que permite inyectar un {@link TransportSender} personalizado.
     *
     * @param transportSender implementación concreta de {@link TransportSender} para envío.
     */
    public EmailServiceImpl(TransportSender transportSender) {
        this.emailSesionFactory = new EmailSessionFactory();
        this.enmailMimeMessage = new EmailMimeMessage();
        this.enmailSender = new EmailSender(transportSender);
    }

    /**
     * Envía un correo electrónico con los parámetros especificados.
     *
     * @param properties objeto que contiene las propiedades asociadas al proyecto
     * @param emailMensaje      mensaje de email (remitente, destinatarios, asunto y cuerpo del mensaje)
     * @throws EmailServiceException si ocurre un error durante el proceso de envío
     */
    @Override
    public void enviarEmail(
            Properties properties,
            String user,
            String password,
            EmailMensaje emailMensaje
    ) throws EmailServiceException {
        log.debug("[enviarCorreo] - Enviar correo: {}", emailMensaje);

        try {

            // Crear sesión SMTP autenticada
            Session sesion = emailSesionFactory.getSession(properties, user, password);
            log.debug("Sesión SMTP creada correctamente.");

            // Generar mensaje MIME listo para enviar
            MimeMessage mimeMessage = enmailMimeMessage.getMimeMessage(sesion, emailMensaje);
            log.debug("Mensaje MIME generado correctamente.");

            // Enviar el mensaje
            enmailSender.enviarEmail(mimeMessage);

        } catch (EmailSessionFactoryException e) {
            log.error("Error al enviar el correo electrónico: {}", e.getMessage(), e);
            throw new EmailServiceException("Error al enviar el correo", e);
        }
    }
}
