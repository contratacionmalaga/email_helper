package local.jarios.email.infraestructura;

import jakarta.mail.*;
import local.jarios.email.exception.EmailSenderException;
import local.jarios.email.exception.EmailServiceException;
import local.jarios.email.servicio.TransportSender;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

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
public class EmailSender {

    /**
     * Objeto que abstrae el envío de mensajes.
     */
    private final TransportSender transportSender;

    /**
     * Constructor que recibe la implementación de {@link TransportSender}.
     *
     * @param transportSender Implementación concreta que enviará el mensaje.
     */
    public EmailSender(TransportSender transportSender) {
        this.transportSender = transportSender;
    }

    /**
     * Envía el mensaje de correo especificado usando la clase {@link Transport}.
     *
     * @param mensaje mensaje de correo ya configurado (destinatario, asunto, contenido, etc.)
     * @throws EmailServiceException si ocurre un error durante el envío
     */
    public void enviarEmail(
            Message mensaje
    ) throws EmailSenderException {
        log.debug("[enviarMail] - Datos del mensaje que se desea envair.");
        try {
            log.debug("[enviarMail] - From: {}", (Object) mensaje.getFrom());
            log.debug("[enviarMail] - AllRecipients: {}", (Object) mensaje.getAllRecipients());
            log.debug("[enviarMail] - Subject: {}", mensaje.getSubject());
            log.debug("[enviarMail] - Subject: {}", mensaje.getContent());
            transportSender.send(mensaje);
        } catch (MessagingException e) {
            String msgError = "Error al realizar el envío";
            log.error(msgError, e);
            throw new EmailSenderException(msgError, e);
        } catch (IOException e) {
            String msgError = "Error en el contenido del mensaje";
            log.error(msgError, e);
            throw new EmailSenderException(msgError, e);
        }
    }
}
