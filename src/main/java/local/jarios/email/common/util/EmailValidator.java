package local.jarios.email.common.util;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilidad para la validación de direcciones de correo electrónico según el estándar RFC 5322.
 * Utiliza {@link jakarta.mail.internet.InternetAddress} para comprobar la validez de los correos.
 */
public class EmailValidator {

    /**
     * Constructor vacío
     */
    private EmailValidator() {
        // Constructor vacío
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

        for (String email : emails) {
            String trimmed = email.trim();
            if (!trimmed.isEmpty() && !isValidEmailRFC(trimmed)) {
                invalids.add(trimmed);
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
    public static boolean isValidEmailRFC(String email) {
        try {
            InternetAddress addr = new InternetAddress(email, true);
            addr.validate(); // lanza excepción si el correo no es válido
            return true;
        } catch (AddressException e) {
            return false;
        }
    }
}
