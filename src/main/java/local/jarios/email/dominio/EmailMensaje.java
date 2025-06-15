package local.jarios.email.dominio;

import java.util.List;

/**
 * Representa un mensaje de correo electrónico con la información básica necesaria para su envío.
 *
 * <p>Contiene los siguientes datos:</p>
 * <ul>
 *     <li><b>remitente</b>: dirección de correo del remitente</li>
 *     <li><b>destinatarios</b>: lista de direcciones de correo de los destinatarios</li>
 *     <li><b>asunto</b>: asunto del mensaje</li>
 *     <li><b>cuerpo</b>: contenido del mensaje (puede ser texto plano o HTML)</li>
 * </ul>
 *
 * <p>Este record puede ser utilizado como DTO para transportar la información necesaria
 * en el envío de correos dentro del sistema.</p>
 *
 * @param remitente dirección de correo del emisor
 * @param destinatarios lista de correos electrónicos de los receptores
 * @param asunto título o tema del correo
 * @param cuerpo contenido principal del mensaje
 *
 * @author Juan
 * @since 1.0
 */
public record EmailMensaje(String remitente, List<String> destinatarios, String asunto, String cuerpo) {

    /**
     * Devuelve una representación en texto del objeto {@link EmailMensaje}.
     * <p>
     * Incluye los campos: remitente, destinatarios, asunto y una versión
     * resumida del cuerpo (máximo 100 caracteres para evitar saturar los logs).
     * </p>
     *
     * @return una cadena representando el contenido del mensaje
     */
    @Override
    public String toString() {
        return String.format(
                "EmailMensaje {remitente='%s', destinatarios=%s, asunto='%s', cuerpo='%s'}",
                remitente,
                String.join(", ", destinatarios),
                asunto,
                resumirCuerpo(cuerpo)
        );
    }

    /**
     * Resume el contenido del cuerpo del mensaje, limitándolo a 100 caracteres.
     * <p>
     * Si el texto es más largo, se trunca y se agregan puntos suspensivos al final.
     * Si el texto es nulo, se devuelve una cadena vacía.
     * </p>
     *
     * @param texto el cuerpo completo del mensaje
     * @return versión resumida del cuerpo, adecuada para mostrarse en logs
     */
    private String resumirCuerpo(String texto) {
        if (texto == null) return "";
        return texto.length() > 100
                ? String.format("%s...", texto.substring(0, 97))
                : texto;
    }
}

