package local.jarios.email.infraestructura;

import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Transport;
import local.jarios.email.exception.EmailException;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio responsable de enviar mensajes de correo electrónico mediante la API de JavaMail.
 * <p>
 * Esta clase encapsula el uso de {@link Transport#send(Message)} y proporciona trazabilidad
 * mediante logs. Es útil para desacoplar el envío real de correos del resto de la aplicación.
 * </p>
 *
 * <p>Ejemplo de uso:</p>
 * <pre>{@code
 * MimeMessage mensaje = new MimeMessage(session);
 * // configurar destinatarios, asunto, contenido, etc.
 * CorreoSender sender = new CorreoSender();
 * sender.enviar(mensaje);
 * }</pre>
 *
 * @author Juan
 * @since 1.0
 */
@Slf4j
public class CorreoSender {

    /**
     * Constructor vacío
     */
    public CorreoSender() {
        // Constructor vacío
    }

    /**
     * Envía el mensaje de correo especificado usando la clase {@link Transport}.
     *
     * @param mensaje mensaje de correo ya configurado (destinatario, asunto, contenido, etc.)
     * @throws EmailException si ocurre un error durante el envío
     */
    public void enviar(Message mensaje) throws EmailException {
        try {
            log.debug("Enviando mensaje a: {}", String.join(", ",
                    mensaje.getAllRecipients() != null ?
                            java.util.Arrays.stream(mensaje.getAllRecipients())
                                    .map(Address::toString)
                                    .toArray(String[]::new)
                            : new String[]{"[sin destinatarios]"}
            ));

            Transport.send(mensaje);
            log.debug("Mensaje enviado correctamente.");

        } catch (MessagingException e) {
            log.error("Error al enviar el mensaje", e);
            throw new EmailException("Error al enviar el correo electrónico", e);
        }
    }
}
