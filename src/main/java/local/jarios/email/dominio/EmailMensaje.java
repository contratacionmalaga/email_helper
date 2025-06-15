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
}

