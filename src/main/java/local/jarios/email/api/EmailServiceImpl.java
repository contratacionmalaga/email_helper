package local.jarios.email.api;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.NoSuchProviderException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.Session;
import local.jarios.email.common.util.Constantes;
import local.jarios.email.common.util.EmailValidator;
import local.jarios.email.exception.EmailServiceException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Properties;

import static local.jarios.email.common.util.Constantes.EMAIL_PATTERN;

/**
 * Implementación del servicio de envío de correos electrónicos.
 *
 * @author Juan
 * @since 1.0
 */
@Slf4j
public class EmailServiceImpl implements EmailService {

    /**
     * Constructor vacío
     */
    public EmailServiceImpl() {
        // Constructor vacío
    }

    /**
     * Envía un correo electrónico con los parámetros especificados.
     *
     * @param props properties con las propiedades del servidor de correo
     * @param from  remitente
     * @param to  lista de destinatarios separados por coma
     * @param subject  lista de destinatarios separados por coma
     * @param body  lista de destinatarios separados por coma
     * @throws EmailServiceException si ocurre un error durante el proceso de envío
     */
    @Override
    public void enviarEmail(Properties props, String from, String to, String subject, String body)
            throws EmailServiceException {

        if ((props == null) ||
                (from == null) ||
                (from.isBlank()) ||
                (to == null) ||
                (to.isBlank()) ||
                (subject == null) ||
                (subject.isBlank()) ||
                (body == null) ||
                (body.isBlank()))  {
            log.debug("[enviarEmail] - Invalid parameters");
            throw new EmailServiceException("[enviarEmail] - Invalid parameters");
        }
        log.debug("[enviarEmail] - Propiedades utilizadas: {}", props.toString());

        // COMPROBACIÓN DE LAS DIRECCIONES DE EMAIL from Y to

        // Validación de direcciones
        if (!EmailValidator.isValidEmailRFC(from)) {
            throw new EmailServiceException("La dirección 'from' no es válida: " + from);
        }

        List<String> invalidTo = EmailValidator.getInvalidEmailsRFC(to);
        if (!invalidTo.isEmpty()) {
            throw new EmailServiceException("Direcciones 'to' inválidas: " + invalidTo);
        }

        // COMPROBACIÓN DE LAS PROPERTIES

        String msg;

        // mail.smpt.user
        String username = props.getProperty("mail.smtp.user");
        if (username == null) {
            log.debug("[enviarEmail] - 'mail.smtp.user' es nulo");
            throw new EmailServiceException("[enviarEmail] - 'mail.smtp.user' es nulo");
        }
        log.debug("[enviarEmail] - 'mail.smtp.user': {}", username);

        // mail.smpt.password
        String password = props.getProperty("mail.smtp.password");
        if (password == null) {
            msg = "[enviarEmail] - 'mail.smtp.password' es nulo";
            log.debug(msg);
            throw new EmailServiceException(msg);
        }
        log.debug("[enviarEmail] - 'mail.smtp.password': {}", password);

        // mail.smpt.auth
        String auth = props.getProperty("mail.smtp.auth");
        if (auth == null) {
            msg = "[enviarEmail] - 'mail.smtp.auth' es nulo";
            log.debug(msg);
            throw new EmailServiceException(msg);
        }
        log.debug("[enviarEmail] - 'mail.smtp.auth': {}", auth);

        // mail.smpt.starttls.enable
        String starttls = props.getProperty("mail.smtp.starttls.enable");
        if (starttls == null) {
            msg = "[enviarEmail] - 'mail.smtp.starttls.enable' es nulo";
            log.debug(msg);
            throw new EmailServiceException(msg);
        }
        log.debug("[enviarEmail] - 'mail.smtp.starttls.enable': {}", starttls);

        // mail.smtp.host
        String host = props.getProperty("mail.smtp.host");
        if (host == null) {
            msg = "[enviarEmail] - 'mail.smtp.host' es nulo";
            log.debug(msg);
            throw new EmailServiceException(msg);
        }
        log.debug("[enviarEmail] - 'mail.smtp.host': {}", host);

        // mail.smtp.port
        String port = props.getProperty("mail.smtp.port");
        if (port == null) {
            msg = "[enviarEmail] - 'mail.smtp.port' es nulo";
            log.debug(msg);
            throw new EmailServiceException(msg);
        }
        log.debug("[enviarEmail] - 'mail.smtp.port': {}", port);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        log.debug("[enviarEmail] - Creación de la sesión -> NO IMPLICA que se ha conectado con el servidor.");

        try {

            //
            Transport transport = session.getTransport(Constantes.PROTOCOL);
            transport.connect(host, Integer.parseInt(port), username, password);
            log.debug("[enviarEmail] - Conexión realizada corerctamente al servidor.");

            // Crear mensaje
            Message message = new MimeMessage(session);
            log.debug("[enviarEmail] - Creación de un Message asociado a la Session correctamente.");

            message.setFrom(new InternetAddress(from));
            log.debug("[enviarEmail] - Definido el remitente en em Message correctamente.");

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            log.debug("[enviarEmail] - Definidos los destinatarios en em Message correctamente.");

            message.setSubject(subject);
            log.debug("[enviarEmail] - Definido el subject al Message correctamente.");

            message.setText(body);
            log.debug("[enviarEmail] - Definidos el body al  Message correctamente.");

            // Enviar el mensaje
            Transport.send(message);
            log.debug("[enviarEmail] - Enviado el correo correctamente.");

        } catch (NoSuchProviderException e) {
            log.error("La conexión del tipo 'smtp' no está permitda para este servidor. Error: {}", e.getMessage());
            throw new EmailServiceException("La conexión del tipo 'smtp' no está permitda para este servidor", e);
        } catch (MessagingException e) {
            log.error("Error en la conexión con el servidor. Error: {}", e.getMessage());
            throw new EmailServiceException("Error en la conexión con el servidor", e);
        }
    }
}
