package local.jarios.email.api;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import local.jarios.email.exception.EmailException;
import local.jarios.email.model.EmailData;
import local.jarios.email.validator.EmailRequestValidator;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

import static local.jarios.email.common.util.Constantes.SMTP_HOST;
import static local.jarios.email.common.util.Constantes.SMTP_PASSWORD;
import static local.jarios.email.common.util.Constantes.SMTP_PORT;
import static local.jarios.email.common.util.Constantes.SMTP_USER;

/**
 * Implementación del servicio de envío de correos electrónicos.
 *
 * <p>Esta clase:</p>
 * <ul>
 *   <li>Orquesta el envío del correo</li>
 *   <li>Realiza logging a nivel de negocio</li>
 *   <li>No expone información sensible</li>
 * </ul>
 */
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final EmailSender emailSender;

    public EmailServiceImpl(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendEmail(Properties props, EmailData data) throws EmailException {

        try {
            log.debug("Inicio del envío de correo electrónico.");

            EmailRequestValidator.validarEmailRequest(props, data);

            Session session = createSession(props);
            Message message = createMimeMessage(session, data);

            emailSender.send(session, message);

            log.debug("Correo electrónico enviado correctamente.");

        } catch (EmailException ex) {

            log.error(
                "Fallo SMTP | host={} | port={} | user={}",
                getSafeProperty(props, SMTP_HOST),
                getSafeProperty(props, SMTP_PORT),
                getSafeProperty(props, SMTP_USER)
            );

            throw ex;

        } catch (RuntimeException ex) {

            log.error("Error inesperado durante el envío del correo.");

            throw new EmailException(
                "Error inesperado durante el envío del correo electrónico.",
                ex
            );
        }
    }


    /* ===================== */
    /* MÉTODOS PRIVADOS */
    /* ===================== */

    private Session createSession(Properties props) {

        String username = getSafeProperty(props, SMTP_USER);
        String password = props.getProperty(SMTP_PASSWORD);

        return Session.getInstance(
            props,
            new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            }
        );
    }

    private Message createMimeMessage(Session session, EmailData data)
        throws EmailException {

        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(data.from()));
            message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(data.to())
            );
            message.setSubject(data.subject());
            message.setContent(data.body(), "text/html; charset=utf-8");
            message.saveChanges();

            return message;

        } catch (MessagingException ex) {
            throw new EmailException(
                "Error creando el mensaje de correo electrónico.",
                ex
            );
        }
    }
    private String getSafeProperty(Properties props, String key) {

        return props == null ? null : props.getProperty(key);
    }
}
