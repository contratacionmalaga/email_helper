package local.jarios.email.api;

import jakarta.mail.*;
import jakarta.mail.internet.*;
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

            if (isErrorNotification(data)) {
                log.error("Fallo enviando correo de error. Abortando para evitar bucle.");
                return; // 🔴 CORTE DEFINITIVO
            }

            log.error(
                "Fallo SMTP | host={} | port={} | user={}",
                props.getProperty(SMTP_HOST),
                props.getProperty(SMTP_PORT),
                props.getProperty(SMTP_USER)
            );

            throw ex;

        } catch (RuntimeException ex) {

            if (isErrorNotification(data)) {
                log.error("Fallo crítico enviando correo de error. Abortando.");
                return;
            }

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

    private boolean isErrorNotification(EmailData data) {
        return data.subject() != null && data.subject().startsWith("❌ ERROR");
    }

    private Session createSession(Properties props) {

        String username = props.getProperty(SMTP_USER);
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

            return message;

        } catch (MessagingException ex) {
            throw new EmailException(
                "Error creando el mensaje de correo electrónico.",
                ex
            );
        }
    }
}
