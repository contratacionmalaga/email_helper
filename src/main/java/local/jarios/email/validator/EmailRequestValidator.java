package local.jarios.email.validator;

import local.jarios.email.common.util.EmailValidator;
import local.jarios.email.exception.EmailServiceException;
import local.jarios.email.model.EmailData;

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
 * </p>
 *
 * <p>Esta clase no debe ser instanciada.</p>
 *
 * @author Juan
 * @since 1.0
 */
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
     * @throws EmailServiceException si se detecta algún valor inválido o faltante.
     */
    public static void validarEmailRequest(Properties props, EmailData data) throws EmailServiceException {
        if (props == null) {
            throw new EmailServiceException("Propiedades nulas");
        }
        if (data == null) {
            throw new EmailServiceException("EmailData nulo");
        }

        if (isBlank(data.from())) throw new EmailServiceException("'from' es obligatorio");
        if (isBlank(data.to())) throw new EmailServiceException("'to' es obligatorio");
        if (isBlank(data.subject())) throw new EmailServiceException("'subject' es obligatorio");
        if (isBlank(data.body())) throw new EmailServiceException("'body' es obligatorio");

        if (!EmailValidator.isValidEmailRFC(data.from())) {
            throw new EmailServiceException("Email 'from' inválido: " + data.from());
        }

        List<String> invalidTo = EmailValidator.getInvalidEmailsRFC(data.to());
        if (!invalidTo.isEmpty()) {
            throw new EmailServiceException("Email(s) 'to' inválidos: " + invalidTo);
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
     * @return El valor de la propiedad validada.
     * @throws EmailServiceException si la propiedad no está presente.
     */
    private static String validarPropiedad(Properties props, String clave, boolean ocultar) throws EmailServiceException {
        String valor = props.getProperty(clave);
        if (valor == null) {
            throw new EmailServiceException("Falta propiedad obligatoria: " + clave);
        }
        // Aquí se podría añadir logging condicional si fuera necesario
        return valor;
    }

    /**
     * Verifica si una cadena está vacía o compuesta solo por espacios.
     *
     * @param s Cadena a comprobar.
     * @return {@code true} si es nula o vacía tras recortes, {@code false} en caso contrario.
     */
    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
