package local.jarios.email.model;

/**
 * Representa los datos necesarios para enviar un email.
 *
 * @param from Dirección de correo del remitente.
 * @param to Dirección(es) de correo del(los) destinatario(s).
 * @param subject Asunto del correo.
 * @param body Cuerpo del mensaje.
 */
public record EmailData(String from, String to, String subject, String body) {}
