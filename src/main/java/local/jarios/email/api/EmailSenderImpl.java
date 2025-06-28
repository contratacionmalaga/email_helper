package local.jarios.email.api;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import local.jarios.email.exception.EmailException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implementación de {@link EmailSender} que utiliza el protocolo SMTP para enviar correos electrónicos.
 * <p>
 * Esta clase delega la responsabilidad de enviar el mensaje al método estático {@link Transport#send(Message)} de la API Jakarta Mail.
 * </p>
 *
 * @since 1.0
 */
public class EmailSenderImpl implements EmailSender {

    /** LOGGER asociado al componente. */
    private static final Logger LOGGER = LogManager.getLogger("local.jarios.email");
    
    /**
     * Constructor por defecto.
     */
    public EmailSenderImpl() {
        // Constructor vacío intencional
    }

    /**
     * Envía un mensaje de correo electrónico utilizando una sesión SMTP proporcionada.
     * <p>
     * Este método delega la tarea de envío al método estático {@link Transport#send(Message)}.
     * </p>
     *
     * @param session La sesión SMTP que contiene las propiedades y la autenticación necesarias.
     * @param message El mensaje de correo electrónico a enviar.
     * @throws EmailException Si ocurre un error al enviar el mensaje.
     * @see Transport#send(Message)
     */
    @Override
    public void send(Session session, Message message) throws EmailException {
        try {

            Transport.send(message);
            LOGGER.debug("[send] - Email enviado correctamente.");

        } catch (MessagingException ex) {

            String msg = String.format("[send] - Excepción en el envío del email. Sessión: %s. Message: %s", session, message);
            LOGGER.error(msg, ex);
            throw  new EmailException(msg, ex);

        } catch (RuntimeException ex) {

            String msg = String.format("[send] - Excepción desconocida. Sessión: %s. Message: %s", session, message);
            LOGGER.error(msg, ex);
            throw  new EmailException(msg, ex);

        }

    }
}
