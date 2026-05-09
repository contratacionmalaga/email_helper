package local.jarios.email.validator;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import local.jarios.email.exception.EmailException;
import local.jarios.email.model.EmailData;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static local.jarios.email.common.util.Constantes.SMTP_AUTH;
import static local.jarios.email.common.util.Constantes.SMTP_HOST;
import static local.jarios.email.common.util.Constantes.SMTP_PASSWORD;
import static local.jarios.email.common.util.Constantes.SMTP_PORT;
import static local.jarios.email.common.util.Constantes.SMTP_STARTTLS;
import static local.jarios.email.common.util.Constantes.SMTP_USER;

/**
 * Validador de los datos necesarios para el envío de correos electrónicos.
 *
 * <p>Esta clase:</p>
 * <ul>
 *   <li>No realiza logging en el flujo normal</li>
 *   <li>Lanza {@link EmailException} con mensajes claros</li>
 *   <li>Delega el logging a la capa de servicio</li>
 * </ul>
 *
 * @author Juan
 * @since 1.0
 */
public final class EmailRequestValidator {

    private EmailRequestValidator() {
        // No instanciable
    }

    /**
     * Valida todos los datos necesarios para el envío de un correo electrónico.
     *
     * @param props propiedades SMTP
     * @param data  datos del email
     * @throws EmailException si algún dato es inválido
     */
    public static void validarEmailRequest(Properties props, EmailData data)
        throws EmailException {

        if (props == null) {
            throw new EmailException("Las propiedades SMTP son obligatorias.");
        }

        if (data == null) {
            throw new EmailException("Los datos del email son obligatorios.");
        }

        validarEmailData(data);
        validarDirecciones(data);
        validarPropiedadesSMTP(props);
    }

    /* ===================== */
    /* VALIDACIONES */
    /* ===================== */

    private static void validarEmailData(EmailData data) {

        if (isBlank(data.from())) {
            throw new EmailException("El remitente ('from') es obligatorio.");
        }

        if (isBlank(data.to())) {
            throw new EmailException("El destinatario ('to') es obligatorio.");
        }

        if (isBlank(data.subject())) {
            throw new EmailException("El asunto ('subject') es obligatorio.");
        }

        if (isBlank(data.body())) {
            throw new EmailException("El cuerpo del mensaje ('body') es obligatorio.");
        }

    }

    private static void validarDirecciones(EmailData data) {

        if (isInvalidEmailRFC(data.from())) {
            throw new EmailException("Email 'from' inválido: " + data.from());
        }

        List<String> invalidTo = getInvalidEmailsRFC(data.to());
        if (!invalidTo.isEmpty()) {
            throw new EmailException("Email(s) 'to' inválidos: " + invalidTo);
        }
    }

    private static void validarPropiedadesSMTP(Properties props) {

        validarPropiedad(props, SMTP_USER);
        validarPropiedad(props, SMTP_PASSWORD);
        validarPropiedad(props, SMTP_AUTH);
        validarPropiedad(props, SMTP_STARTTLS);
        validarPropiedad(props, SMTP_HOST);
        validarPropiedad(props, SMTP_PORT);
    }

    private static void validarPropiedad(Properties props, String clave) {

        String valor = props.getProperty(clave);

        if (valor == null || valor.isBlank()) {
            throw new EmailException("Falta la propiedad SMTP obligatoria: " + clave);
        }
    }

    /* ===================== */
    /* UTILIDADES */
    /* ===================== */

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static List<String> getInvalidEmailsRFC(String commaSeparatedEmails) {

        List<String> invalids = new ArrayList<>();

        for (String email : commaSeparatedEmails.split(",")) {

            String trimmed = email.trim();

            if (!trimmed.isEmpty() && isInvalidEmailRFC(trimmed)) {
                invalids.add(trimmed);
            }
        }

        return invalids;
    }

    private static boolean isInvalidEmailRFC(String email) {

        try {
            InternetAddress addr = new InternetAddress(email, true);
            addr.validate();
            return false;
        } catch (AddressException ex) {
            return true;
        }
    }
}
