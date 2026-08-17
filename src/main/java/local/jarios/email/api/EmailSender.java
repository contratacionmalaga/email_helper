package local.jarios.email.api;

import jakarta.mail.Message;
import jakarta.mail.Session;
import local.jarios.email.exception.EmailException;

/**
 * Interfaz que define el contrato para el envío de correos electrónicos.
 *
 * <p>Esta interfaz permite enviar mensajes de correo electrónico utilizando una sesión SMTP
 * proporcionada.
 *
 * @since 1.0
 */
public interface EmailSender {

  /**
   * Envía un mensaje de correo electrónico utilizando una sesión SMTP proporcionada.
   *
   * <p>Este método delega la tarea de envío al método estático {@link
   * jakarta.mail.Transport#send(Message)}.
   *
   * @param session La sesión SMTP que contiene las propiedades y la autenticación necesarias.
   * @param message El mensaje de correo electrónico a enviar.
   * @throws EmailException Si ocurre un error al enviar el mensaje.
   * @see jakarta.mail.Transport#send(Message)
   */
  void send(Session session, Message message) throws EmailException;
}
