package local.jarios.email.api;

import local.jarios.email.exception.EmailServiceException;

import java.util.Properties;

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
     * Envía un correo electrónico con los parámetros especificados.
     *
     * @param props properties con las propiedades del servidor de correo
     * @param from  remitente
     * @param to  lista de destinatarios separados por coma
     * @param subject  lista de destinatarios separados por coma
     * @param body  lista de destinatarios separados por coma
     * @throws EmailServiceException si ocurre un error durante el proceso de envío
     */
    void enviarEmail(Properties props, String from, String to, String subject, String body)
            throws EmailServiceException;
}
