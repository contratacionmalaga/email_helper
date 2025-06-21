package local.jarios.email.api;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;

/**
 * Implementación de {@link EmailSender} que utiliza el protocolo SMTP para enviar correos electrónicos.
 * <p>
 * Esta clase delega la responsabilidad de enviar el mensaje al método estático {@link Transport#send(Message)} de la API Jakarta Mail.
 * </p>
 *
 * @since 1.0
 */
public class SmtpEmailSender implements EmailSender {

    /**
     * Envía un mensaje de correo electrónico utilizando una sesión SMTP proporcionada.
     * <p>
     * Este método delega la tarea de envío al método estático {@link Transport#send(Message)}.
     * </p>
     *
     * @param session La sesión SMTP que contiene las propiedades y la autenticación necesarias.
     * @param message El mensaje de correo electrónico a enviar.
     * @throws MessagingException Si ocurre un error al enviar el mensaje.
     * @see Transport#send(Message)
     */
    @Override
    public void send(Session session, Message message) throws MessagingException {
        Transport.send(message);
    }
}
