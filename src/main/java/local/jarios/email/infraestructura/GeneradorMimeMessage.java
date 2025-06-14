package local.jarios.email.infraestructura;

import local.jarios.email.dominio.CorreoMensaje;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase encargada de construir instancias de {@link MimeMessage} a partir
 * de objetos de dominio {@link CorreoMensaje}.
 *
 * <p>Encapsula la lógica de construcción y asignación de campos necesarios
 * para el envío de correos mediante JavaMail.</p>
 *
 * <p>Ejemplo de uso:</p>
 * <pre>{@code
 * Session session = ...;
 * CorreoMensaje correo = new CorreoMensaje("remitente@dominio.com",
 *      List.of("destino@dominio.com"), "Asunto", "Mensaje");
 * MimeMessage mensaje = new GeneradorMimeMessage().generarMensaje(session, correo);
 * }</pre>
 *
 * @author Juan
 * @since 1.0
 */
@Slf4j
public class GeneradorMimeMessage {

    /**
     * Genera un objeto {@link MimeMessage} con los datos del {@link CorreoMensaje}.
     *
     * @param session la sesión de JavaMail configurada (incluye propiedades SMTP, autenticación, etc.)
     * @param correoMensaje objeto con los datos necesarios para construir el correo
     * @return instancia de {@link MimeMessage} lista para ser enviada
     * @throws MessagingException si ocurre un error durante la construcción del mensaje
     */
    public MimeMessage generarMensaje(Session session, CorreoMensaje correoMensaje) throws MessagingException {
        log.debug("Generando MimeMessage desde {}", correoMensaje);

        MimeMessage mensaje = new MimeMessage(session);

        mensaje.setFrom(new InternetAddress(correoMensaje.remitente()));
        mensaje.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(String.join(",", correoMensaje.destinatarios()))
        );
        mensaje.setSubject(correoMensaje.asunto());
        mensaje.setText(correoMensaje.cuerpo());

        log.debug("MimeMessage generado correctamente con asunto: '{}'", correoMensaje.asunto());

        return mensaje;
    }
}
