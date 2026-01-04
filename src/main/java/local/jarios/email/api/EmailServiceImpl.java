package local.jarios.email.api;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import local.jarios.email.model.EmailData;
import local.jarios.email.validator.EmailRequestValidator;
import local.jarios.email.exception.EmailException;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

import static local.jarios.email.common.util.Constantes.SMTP_PASSWORD;
import static local.jarios.email.common.util.Constantes.SMTP_USER;

/**
 * Implementación del servicio de envío de correos electrónicos.
 * Utiliza {@link EmailSender} para el envío efectivo de los correos.
 */
@Slf4j
public class EmailServiceImpl implements EmailService {
    
    /**
     * Instancia del servicio que implementa el envío de emails vía SMTP.
     * Se utiliza para delegar la lógica de envío en esta clase.
     */
    private final EmailSender emailSender;

    /**
     * Constructor que recibe una instancia de {@link EmailSender}.
     *
     * @param emailSender Instancia de {@link EmailSender} para el envío de correos.
     */
    public EmailServiceImpl(EmailSender emailSender) {

        this.emailSender = emailSender;
        log.debug("[EmailServiceImpl] - EmailSender asignado correctamente.");
    }

    /**
     * Envía un correo electrónico utilizando los datos proporcionados.
     *
     * @param props Propiedades de configuración del servidor SMTP.
     * @param data  Datos del correo electrónico a enviar.
     * @throws EmailException Si ocurre algún error durante el proceso de envío.
     */
    @Override
    public void sendEmail(Properties props, EmailData data) throws EmailException {

        try {

            // Validación de los datos del correo
            EmailRequestValidator.validarEmailRequest(props, data);
            log.debug("[sendEmail] - Validados Properties e EmailData.");

            // Configuración de la sesión de correo
            Session session = createSession(props);
            log.debug("[sendEmail] - Creado el objeto Session correctamente con Properties.");

            // Creación del mensaje MIME
            Message message = createMimeMessage(session, data);
            log.debug("[sendEmail] - Creación de un Message a partir de la Sesión e EmailData correctamente.");

            // Envío del mensaje
            emailSender.send(session, message);
            log.debug("[sendEmail] - Correo enviado exitosamente a {}", data.to());

        } catch (RuntimeException ex) {

            String msg = String.format("[sendEmail] - Excepción desconocida. Error: %s; Properties: %s; EmailData: %s", ex.getMessage(), props, data);
            log.error(msg, ex);
            throw new EmailException(msg, ex);

        }
    }

    /**
     * Crea una sesión de correo utilizando las propiedades proporcionadas.
     *
     * @param props Propiedades de configuración del servidor SMTP.
     * @return Instancia de {@link Session} configurada.
     */
    private Session createSession(Properties props) {

        String username = props.getProperty(SMTP_USER);
        log.debug("[createSession] - Obtengo el usuario desde el objeto Propertes: {}", username);
        String password = props.getProperty(SMTP_PASSWORD);
        log.debug("[createSession] - Obtengo la clave desde el objeto Properties: {}", password);

        Session session =  Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        log.debug("[createSession] - Sesión creada correctamente para el usuario: {}", username);
        return session;
    }

    /**
     * Crea un mensaje MIME utilizando los datos proporcionados.
     *
     * @param session Instancia de {@link Session} configurada.
     * @param data    Datos del correo electrónico a enviar.
     * @return Instancia de {@link Message} configurada.
     * @throws EmailException Si ocurre un error al crear el mensaje.
     */
    private Message createMimeMessage(Session session, EmailData data) throws EmailException {

        //
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(data.from()));
            log.debug("[createMimeMessage] - Asignamos el 'from' al objeto Message: {}", data.from());
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(data.to()));
            log.debug("[createMimeMessage] - Asignamos el 'to' al objeto Message: {}", data.to());
            message.setSubject(data.subject());
            log.debug("[createMimeMessage] - Asignamos el 'subject' al objeto Message.");
            message.setContent(data.body(), "text/html; charset=utf-8");
            log.debug("[createMimeMessage] - Asignamos el 'body' al objeto Message con formato HTML y UTF-8.");
            return message;

        }  catch (MessagingException ex) {

            String msg = String.format(
                "[createMimeMessage] - Excepción AddressExcepction | MessagngException. Error: %s; EmailData: %s",
                ex.getMessage(),
                data
            );
            log.error(msg, ex);
            throw new EmailException(msg, ex);

        }
    }
}
