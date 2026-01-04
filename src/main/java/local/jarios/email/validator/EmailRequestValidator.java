package local.jarios.email.validator;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import local.jarios.email.common.util.Constantes;
import local.jarios.email.exception.EmailException;
import local.jarios.email.helper.TextHelper;
import local.jarios.email.model.EmailData;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static local.jarios.email.common.util.Constantes.*;

/**
 * Clase utilitaria encargada de validar los parámetros necesarios para el envío de correos electrónicos.
 * <p>
 * Esta clase realiza las siguientes validaciones:
 * <ul>
 *     <li>Validez y formato del objeto {@link EmailData}</li>
 *     <li>Formato correcto de direcciones de correo electrónico</li>
 *     <li>Presencia de propiedades SMTP obligatorias</li>
 * </ul>
 *
 * <p>Esta clase no debe ser instanciada.
 *
 * @author Juan
 * @since 1.0
 */
@Slf4j
public final class EmailRequestValidator {
    
    /**
     * Constructor privado para evitar instanciación.
     */
    private EmailRequestValidator() {
        // Evitar instanciación
    }

    /**
     * Valida todos los datos necesarios para el envío de un correo electrónico.
     *
     * @param props Propiedades del servidor SMTP.
     * @param data  Datos del correo electrónico (remitente, destinatario, asunto, cuerpo).
     * @throws EmailException si se detecta algún valor inválido o faltante.
     */
    public static void validarEmailRequest(Properties props, EmailData data) throws EmailException {
        if (props == null) {
            log.debug("[validarEmailRequest] - Propiedades nulas.");
            throw new EmailException("Propiedades nulas");
        }
        if (data == null) {
            log.debug("[validarEmailRequest] - Email data nulo.");
            throw new EmailException("EmailData nulo");
        }

        if (isBlank(data.from())) {
            log.debug("[validarEmailRequest] - Campo 'from' obligatorio en EmailData.");
            throw new EmailException("'from' es obligatorio");
        }
        if (isBlank(data.to())) {
            log.debug("[validarEmailRequest] - Campo 'to' obligatorio en EmailData.");
            throw new EmailException("'to' es obligatorio");
        }
        if (isBlank(data.subject())) {
            log.debug("[validarEmailRequest] - Campo 'subject' obligatorio en EmailData.");
            throw new EmailException("'subject' es obligatorio");
        }
        if (isBlank(TextHelper.recortar(data.body(), Constantes.TAMANO_MAXIMO))) {
            log.debug("[validarEmailRequest] - Campo 'body' obligatorio en EmailData.");
            throw new EmailException("'body' es obligatorio");
        }

        if (isInvalidEmailRFC(data.from())) {
            log.debug("[validarEmailRequest] - Email 'from' inválido: {}", data.from());
            throw new EmailException("Email 'from' inválido: " + data.from());
        }

        List<String> invalidTo = getInvalidEmailsRFC(data.to());
        if (!invalidTo.isEmpty()) {
            log.debug("[validarEmailRequest] - Email 'to' inválido: {}", invalidTo);
            throw new EmailException("Email(s) 'to' inválidos: " + invalidTo);
        }

        // Validación de propiedades SMTP requeridas
        validarPropiedad(props, SMTP_USER, false);
        validarPropiedad(props, SMTP_PASSWORD, true);
        validarPropiedad(props, SMTP_AUTH, false);
        validarPropiedad(props, SMTP_STARTTLS, false);
        validarPropiedad(props, SMTP_HOST, false);
        validarPropiedad(props, SMTP_PORT, false);
    }

    /**
     * Valida que una propiedad exista en el objeto {@link Properties}.
     *
     * @param props         Objeto de propiedades a validar.
     * @param clave         Clave de la propiedad a validar.
     * @param ocultar       Si es {@code true}, el valor no debe ser mostrado en logs por seguridad.
     * @throws EmailException si la propiedad no está presente.
     */
    private static void validarPropiedad(Properties props, String clave, boolean ocultar) throws EmailException {
        String valor = props.getProperty(clave);
        String valorLog = ocultar ? "******" : valor;
        log.debug("[validarPropiedad] - Valor de la clave '{}': {}", clave, valorLog);
        if ((valor == null) || (valor.isBlank())) {
            log.debug("[validarPropiedad] - Falta propiedad obligatoria '{}'.", clave);
            throw new EmailException("Falta propiedad obligatoria: " + clave);
        }
        // Aquí se podría añadir logging condicional si fuera necesario
    }

    /**
     * Verifica si una cadena está vacía o compuesta solo por espacios.
     *
     * @param s Cadena a comprobar.
     * @return {@code true} si es nula o vacía tras recortes, {@code false} en caso contrario.
     */
    private static boolean isBlank(String s) {
        boolean valor = s == null || s.trim().isEmpty();
        log.debug("[isBlank] - isBlanck '{}' - {}", s, valor);
        return valor;
    }

    /**
     * Valida una lista de correos electrónicos separados por comas.
     * Devuelve una lista con aquellos correos que no cumplen con el formato RFC 5322.
     *
     * @param commaSeparatedEmails Una cadena con direcciones de correo separadas por comas.
     * @return Lista de correos inválidos según la validación RFC.
     */
    public static List<String> getInvalidEmailsRFC(String commaSeparatedEmails) {

        List<String> invalids = new ArrayList<>();
        String[] emails = commaSeparatedEmails.split(",");
        log.debug("[getInvalidEmailsRFC] - Array de emails: {}", Arrays.toString(emails));
        for (String email : emails) {
            log.debug("[getInvalidEmailsRFC] - Procesando el email: {}", email);
            String trimmed = email.trim();
            log.debug("[getInvalidEmailsRFC] - Email sin espacios en blanco: {}", trimmed);
            if (!trimmed.isEmpty() && isInvalidEmailRFC(trimmed)) {
                invalids.add(trimmed);
                log.debug("[getInvalidEmailsRFC] - Email no valido: {}", email);
            }
        }

        return invalids;
    }

    /**
     * Verifica si una dirección de correo electrónico es válida
     * según las reglas del estándar RFC 5322.
     *
     * @param email Dirección de correo electrónico a validar.
     * @return {@code true} si el correo es válido; {@code false} en caso contrario.
     */
    private static boolean isInvalidEmailRFC(String email) {

        try {
            InternetAddress addr = new InternetAddress(email, true);
            addr.validate(); // lanza excepción si no es válido
            log.debug("[isInvalidEmailRFC] - Cumple con el RFC de email: {}", email);
            return false;    // es válido, no está inválido
        } catch (AddressException e) {
            log.debug("[isInvalidEmailRFC] - NO cumple con el RFC de email: {}", email);
            return true;     // inválido
        }
    }
}
