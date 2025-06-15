package local.jarios.email.infraestructura;

import jakarta.mail.internet.AddressException;
import local.jarios.email.dominio.EmailMensaje;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import local.jarios.email.exception.EmailMimeMessageException;
import local.jarios.email.exception.EmailServiceException;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Clase encargada de construir instancias de {@link MimeMessage} a partir
 * de objetos de dominio {@link EmailMensaje}.
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
public class EmailMimeMessage {

    /**
     * Constructor vacío
     */
    public EmailMimeMessage() {
        // Constructor vacío
    }

    /**
     * Genera un objeto {@link MimeMessage} con los datos del {@link EmailMensaje}.
     *
     * @param session la sesión de JavaMail configurada (incluye propiedades SMTP, autenticación, etc.)
     * @param emailMensaje objeto con los datos necesarios para construir el correo
     * @return instancia de {@link MimeMessage} lista para ser enviada
     */
    public MimeMessage getMimeMessage(
            Session session,
            EmailMensaje emailMensaje
    ) throws EmailMimeMessageException {
        log.debug("[getMimeMessage] -- Generando MimeMessage{}", emailMensaje);

        MimeMessage mensaje = new MimeMessage(session);

        // VALIDAR DIRECCIÓN DE EMAIL DE REMITENTE
        // Validar que no sea nula y vacía
        if (emailMensaje.remitente() == null || emailMensaje.remitente().isBlank()) {
            throw new EmailServiceException("Remitente no puede estar vacío");
        }

        // Validar que sea válida(en lista con un solo elemento para reutilizar método)
        validarDireccionesRegex(List.of(emailMensaje.remitente()));

        // VALIDAR DIRECCIONES DE LOS DESTINATARIOS
        // Validar remitente (en lista con un solo elemento para reutilizar método)
        if (emailMensaje.destinatarios() == null || emailMensaje.destinatarios().isEmpty()) {
            throw new EmailServiceException("Debe haber al menos un destinatario");
        }

        // Validar destinatarios
        validarDireccionesRegex(emailMensaje.destinatarios());

        try {

            InternetAddress remitente = new InternetAddress(emailMensaje.remitente());
            InternetAddress[] destinatarios =
                    InternetAddress.parse(String.join(",", emailMensaje.destinatarios()));

            mensaje.setFrom(remitente);
            log.debug("[getMimeMessage] -- Asignado remitente a MimeMessage: {}", remitente);

            mensaje.setRecipients(Message.RecipientType.TO, destinatarios);
            log.debug(
                    "[getMimeMessage] -- Asignado destinatarios a MimeMessage: {}",
                    Arrays.stream(destinatarios).map(InternetAddress::toUnicodeString).toList());

            mensaje.setSubject(emailMensaje.asunto());
            log.debug("[getMimeMessage] -- Asignado asunto a MimeMessage: {}", emailMensaje.asunto());

            mensaje.setContent(emailMensaje.cuerpo(), "text/html; charset=UTF-8");
            log.debug("[getMimeMessage] -- Asignado cuerpo del mensaje a MimeMessage: {}", emailMensaje.cuerpo());

            return mensaje;

        } catch (AddressException e) {
            log.error("[getMimeMessage] -- Dirección de correo inválida.", e);
            throw new EmailMimeMessageException("Dirección de correo inválida", e);
        } catch (MessagingException e) {
            log.error("[getMimeMessage]. MessagingException.", e);
            throw new EmailMimeMessageException("Error al generar MimeMessage con InternetAddress", e);
        }
    }

    /**
     * Valida que cada dirección de correo en la lista cumpla con el formato estándar
     * definido por la expresión regular.
     *
     * @param correos lista de direcciones de correo electrónico a validar
     * @throws EmailServiceException si alguna dirección no es válida según la expresión regular
     */
    private void validarDireccionesRegex(List<String> correos) throws EmailMimeMessageException {
        // Regex sencillo y común para emails (RFC 5322 simplificado)
        String emailRegex = "^[A-Za-z0-9._%+-]+@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);

        for (String correo : correos) {
            if (!pattern.matcher(correo).matches()) {
                throw new EmailMimeMessageException(String.format("Dirección de correo inválida: %s", correo));
            }
        }
    }
}
