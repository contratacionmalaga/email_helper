package local.jarios.email.exception;

/**
 * Excepción base para errores relacionados con el módulo de envío de correos electrónicos.
 *
 * <p>Se lanza en situaciones donde ocurre un fallo en la configuración, composición
 * o envío de mensajes de correo electrónico. Esta excepción puede encapsular
 * otras excepciones como problemas al leer propiedades, errores de cifrado o fallos de conexión SMTP.</p>
 *
 * <p>Ejemplos de uso:</p>
 * <ul>
 *     <li>Error al cargar la configuración de correo</li>
 *     <li>Error al descifrar la contraseña SMTP</li>
 *     <li>Problemas durante el envío del mensaje</li>
 * </ul>
 *
 * <p>Puede ser utilizada directamente o extendida por excepciones más específicas.</p>
 *
 * @author Juan
 * @since 13/06/2025
 */
public class EmailServiceException extends RuntimeException {

    /**
     * Crea una nueva excepción con un mensaje descriptivo.
     *
     * @param message descripción del error
     */
    public EmailServiceException(String message) {
        super(message);
    }

    /**
     * Crea una nueva excepción con un mensaje y una causa original.
     *
     * @param message descripción del error
     * @param cause excepción que causó este error
     */
    public EmailServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
