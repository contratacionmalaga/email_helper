package local.jarios.email.api;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import local.jarios.email.exception.EmailException;

/**
 * Implementación SMTP de {@link EmailSender}.
 *
 * <p>Responsabilidad: - Enviar el mensaje - Traducir errores técnicos a {@link EmailException} - No
 * exponer información sensible en logs
 *
 * @since 1.0
 */
public class EmailSenderImpl implements EmailSender {

  @Override
  public void send(Session session, Message message) throws EmailException {

    try {
      Transport.send(message);

    } catch (MessagingException ex) {
      throw new EmailException("Error SMTP al enviar el correo electrónico.", ex);

    } catch (RuntimeException ex) {
      throw new EmailException("Error inesperado al enviar el correo electrónico.", ex);
    }
  }
}
