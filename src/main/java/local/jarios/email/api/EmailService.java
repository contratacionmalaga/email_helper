package local.jarios.email.api;

import java.util.Properties;
import local.jarios.email.exception.EmailException;
import local.jarios.email.model.EmailData;

/** Interfaz que define los métodos para el servicio de envío de correos electrónicos. */
public interface EmailService {

  /**
   * Envía un correo electrónico utilizando los datos proporcionados.
   *
   * @param props Propiedades de configuración del servidor SMTP.
   * @param data Datos del correo electrónico a enviar.
   * @throws EmailException Si ocurre algún error durante el proceso de envío.
   */
  void sendEmail(Properties props, EmailData data) throws EmailException;
}
