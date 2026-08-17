package local.jarios.email.exception;

/**
 * Excepción base para errores relacionados con el módulo de envío de correos electrónicos.
 *
 * <p>Se lanza en situaciones donde ocurre un fallo en la configuración, composición o envío de
 * mensajes de correo electrónico. Esta excepción puede encapsular otras excepciones como problemas
 * al leer propiedades, errores de cifrado o fallos de conexión SMTP.
 *
 * <p>Ejemplos de uso:
 *
 * <ul>
 *   <li>Error al cargar la configuración de correo
 *   <li>Error al descifrar la contraseña SMTP
 *   <li>Problemas durante el envío del mensaje
 * </ul>
 *
 * <p>Puede ser utilizada directamente o extendida por excepciones más específicas.
 *
 * @author Juan
 * @since 13/06/2025
 */
public class EmailException extends RuntimeException {

  /**
   * Crea una nueva excepción con un mensaje descriptivo.
   *
   * @param message descripción del error
   */
  public EmailException(String message) {
    super(message);
  }

  /**
   * Crea una nueva excepción con un mensaje y una causa original.
   *
   * @param message descripción del error
   * @param cause excepción que causó este error
   */
  public EmailException(String message, Throwable cause) {
    super(message, cause);
  }
}
