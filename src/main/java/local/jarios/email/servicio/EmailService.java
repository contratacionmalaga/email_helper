package local.jarios.email.servicio;

import local.jarios.email.dominio.EmailMensaje;

/**
 * Servicio para el envío de correos electrónicos.
 * <p>
 * Esta interfaz define el contrato para enviar mensajes de correo electrónico
 * a través de diferentes implementaciones que pueden utilizar SMTP, APIs externas,
 * colas de mensajes, etc.
 * </p>
 *
 * @author Juan
 * @since 1.0
 */
public interface EmailService {

    /**
     * Envía un correo electrónico basado en la información proporcionada
     * en el objeto {@link EmailMensaje}.
     *
     * @param emailMensaje objeto que contiene
     *                     el remitente, destinatarios, asunto y el cuerpo asocicados al mensaje.
     * @throws IllegalArgumentException si alguno de los campos requeridos es inválido o nulo.
     * @throws local.jarios.email.exception.EmailException si ocurre un error al intentar enviar el correo.
     */
    void enviarCorreo(EmailMensaje emailMensaje);
}
