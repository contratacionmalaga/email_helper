package local.jarios.email.servicio;

import local.jarios.email.config.EmailConfig;
import local.jarios.email.dominio.EmailMensaje;
import local.jarios.email.exception.EmailException;
import local.jarios.email.infraestructura.*;

import jakarta.mail.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Implementación del servicio de envío de correos electrónicos.
 * <p>
 * Utiliza la configuración proporcionada mediante {@link EmailConfig}, genera el mensaje
 * en formato MIME y lo envía usando {@link jakarta.mail.Transport}.
 * </p>
 *
 * <p>Este servicio admite múltiples destinatarios y permite especificar el remitente manualmente.</p>
 *
 * @author Juan
 * @since 1.0
 */
@Slf4j
public class EmailServiceImpl implements EmailService {

    /**
     * Configuración del servidor SMTP para el envío de correos.
     * Incluye parámetros como host, puerto, usuario y contraseña.
     */
    private final EmailConfig config;

    /**
     * Fábrica responsable de crear instancias de {@link jakarta.mail.Session}
     * configuradas con los parámetros SMTP definidos en {@link EmailConfig}.
     */
    private final SesionCorreoFactory sesionFactory;

    /**
     * Generador de objetos {@link jakarta.mail.internet.MimeMessage} a partir
     * de la información contenida en un {@code EmailMensaje}.
     */
    private final GeneradorMimeMessage generadorMensaje;

    /**
     * Encapsula la lógica de envío de correos electrónicos utilizando
     * una instancia de {@link jakarta.mail.Transport}.
     */
    private final CorreoSender sender;


    /**
     * Constructor que inicializa los componentes necesarios para el envío de correos.
     *
     * @param config configuración SMTP ya cargada
     */
    public EmailServiceImpl(EmailConfig config) {
        this.config = config;
        this.sesionFactory = new SesionCorreoFactory();
        this.generadorMensaje = new GeneradorMimeMessage();
        this.sender = new CorreoSender();
    }

    /**
     * Envía un correo electrónico con los parámetros especificados.
     *
     * @param emailMensaje     Mensaje de email
     * @throws EmailException si ocurre un error durante el proceso de envío
     */
    @Override
    public void enviarCorreo(EmailMensaje emailMensaje) {
        String remitente = emailMensaje.remitente();
        List<String> destinatarios = emailMensaje.destinatarios();
        String asunto = emailMensaje.asunto();
        String cuerpo = emailMensaje.cuerpo();
        log.debug("Preparando envío de correo desde [{}] a [{}]", remitente, String.join(", ", destinatarios));
        try {
            // Crear sesión SMTP autenticada
            Session sesion = sesionFactory.crearSesion(config);
            log.debug("Sesión SMTP creada correctamente.");

            // Crear objeto de dominio del mensaje
            EmailMensaje mensaje = new EmailMensaje(remitente, destinatarios, asunto, cuerpo);
            log.debug("CorreoMensaje creado con asunto: '{}'", asunto);

            // Generar mensaje MIME listo para enviar
            Message mimeMessage = generadorMensaje.generarMensaje(sesion, mensaje);
            log.debug("Mensaje MIME generado correctamente.");

            // Enviar el mensaje
            sender.enviar(mimeMessage);

        } catch (MessagingException e) {
            log.error("Error al enviar el correo electrónico: {}", e.getMessage(), e);
            throw new EmailException("Error al enviar el correo", e);
        }
    }
}
