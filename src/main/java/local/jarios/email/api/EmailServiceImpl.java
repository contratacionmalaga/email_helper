package local.jarios.email.api;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import local.jarios.email.model.EmailData;
import local.jarios.email.validator.EmailRequestValidator;
import local.jarios.email.exception.EmailServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Implementación del servicio de envío de correos electrónicos.
 * Utiliza {@link EmailSender} para el envío efectivo de los correos.
 */
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final EmailSender emailSender;

    /**
     * Constructor que recibe una instancia de {@link EmailSender}.
     *
     * @param emailSender Instancia de {@link EmailSender} para el envío de correos.
     */
    public EmailServiceImpl(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    /**
     * Envía un correo electrónico utilizando los datos proporcionados.
     *
     * @param props Propiedades de configuración del servidor SMTP.
     * @param data  Datos del correo electrónico a enviar.
     * @throws EmailServiceException Si ocurre algún error durante el proceso de envío.
     */
    @Override
    public void sendEmail(Properties props, EmailData data) throws EmailServiceException {
        try {
            // Validación de los datos del correo
            EmailRequestValidator.validarEmailRequest(props, data);

            // Configuración de la sesión de correo
            Session session = createSession(props);

            // Creación del mensaje MIME
            Message message = createMimeMessage(session, data);

            // Envío del mensaje
            emailSender.send(session, message);

            log.info("Correo enviado exitosamente a {}", data.to());

        } catch (MessagingException e) {
            log.error("Error al enviar el correo electrónico", e);
            throw new EmailServiceException("Error al enviar el correo electrónico", e);
        }
    }

    /**
     * Crea una sesión de correo utilizando las propiedades proporcionadas.
     *
     * @param props Propiedades de configuración del servidor SMTP.
     * @return Instancia de {@link Session} configurada.
     */
    private Session createSession(Properties props) {
        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                String username = props.getProperty("smtp.user");
                String password = props.getProperty("smtp.password");
                return new PasswordAuthentication(username, password);
            }
        });
    }

    /**
     * Crea un mensaje MIME utilizando los datos proporcionados.
     *
     * @param session Instancia de {@link Session} configurada.
     * @param data    Datos del correo electrónico a enviar.
     * @return Instancia de {@link Message} configurada.
     * @throws MessagingException Si ocurre un error al crear el mensaje.
     */
    private Message createMimeMessage(Session session, EmailData data) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(data.from()));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(data.to()));
        message.setSubject(data.subject());
        message.setText(data.body());
        return message;
    }
}
