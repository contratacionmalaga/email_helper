package local.jarios.models;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import local.jarios.entity.EstadisticaEntity;
import local.jarios.exceptions.MiMailException;
import local.jarios.helpers.MiMailHelper;
import local.jarios.properties.PropertyConstantes;
import local.jarios.properties.PropertyManager;
import local.jarios.utils.Mensajes;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * @author Juan Antonio
 */
@Slf4j
public final class MiMail {

    private final Message message;

    public MiMail(
            PropertyManager propertyManager,
            EstadisticaEntity estadisticaEntity) throws MiMailException {

        var properties = propertyManager.getProperties();

        try {
            var session = getSession(
                    properties,
                    propertyManager.getProperty(PropertyConstantes.EMAIL_USER),
                    propertyManager.getProperty(PropertyConstantes.EMAIL_PASSWORD));

            var asunto = propertyManager.getProperty(PropertyConstantes.CONFIG_NAME);

            this.message = getMessage(
                    session,
                    propertyManager,
                    asunto + MiMailHelper.getAsunto(),
                    MiMailHelper.getCuerpoMensaje(estadisticaEntity));

        } catch (MessagingException ex) {

            /// Registro la excepción
            log.error(Mensajes.EXCEPTION_ERROR_MIMAIL_MIMAIL, ex.getMessage());

            /// Devuelvo la excepción
            throw new MiMailException(ex);

        }
    }

    /**
     * Recbe como parametro una instancia del objeto EstadisticaEntity y la envía por correo a la dirección que se
     * encuentra definida dentro de la clase ConstantesEmail.
     *
     * <p>Esta función recibe la instancia de EstadisticaEntity y la envía por email</p>
     *
     * @param properties Objeto que contiene la información de conexión al servidor de correo
     * @param user Usuario con el que me autentico en el servidor de correo
     * @param password Clave del usuario para el acceso al servidor de Correo
     */
    private static Session getSession (
            Properties properties,
            String user,
            String password) {

        ///
        var passwordAuthentication = new PasswordAuthentication(user, password);

        ///
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return passwordAuthentication;
            }
        });
    }

    /**
     * Función encargada de formar un Message a partir de una session, asunto y mensaje.
     *
     * @param session Sesión utilizada para el envío del mensaje de correo
     * @param asunto Asunto del mensaje de correo
     * @param mensaje Cuerpo del mensaje de correo
     * @return Message Mensaje de correo bien formado
     */
    private static Message getMessage (
            Session session,
            PropertyManager propertyManager,
            String asunto,
            String mensaje) throws MessagingException {

        ///
        Message message = new MimeMessage(session);

        ///
        /// Remitente del mensaje
        ///
        String remitente = propertyManager.getProperty(PropertyConstantes.EMAIL_FROM);

        ///
        /// Asigno el remitente al mensaje
        ///
        message.setFrom(new InternetAddress(remitente));

        ///
        /// Destinatarios del mensaje
        ///
        String destinatario = propertyManager.getProperty(PropertyConstantes.EMAIL_TO);

        ///
        /// Asigno el destinario al mensaje
        ///
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));

        ///
        /// Asigno el asunto del mensaje
        ///
        message.setSubject(asunto);

        ///
        /// Asigno el Cuerpo del mensaje de correo junto con el formato del mensaje
        ///
        message.setContent(mensaje, "text/html; charset=utf-8");

        return message;
    }

    public void enviarEmail() throws MiMailException {

        ///
        try {

            ///
            Transport.send(this.message);

        } catch (MessagingException ex) {

            /// Registro la excepción
            log.error(Mensajes.EXCEPTION_ERROR_MIMAIL_ENVIARMAIL, ex.getMessage());

            /// Devuelvo la excepción
            throw new MiMailException(ex);

        }
    }
}