package local.jarios.email.servicio;

import local.jarios.email.config.CorreoConfig;
import local.jarios.email.dominio.CorreoMensaje;
import local.jarios.email.exception.EmailException;
import local.jarios.email.infraestructura.*;

import jakarta.mail.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Implementación del servicio de envío de correos electrónicos.
 * <p>
 * Utiliza la configuración proporcionada mediante {@link CorreoConfig}, genera el mensaje
 * en formato MIME y lo envía usando {@link jakarta.mail.Transport}.
 * </p>
 *
 * <p>Este servicio admite múltiples destinatarios y permite especificar el remitente manualmente.</p>
 *
 * @author Juan
 * @since 1.0
 */
@Slf4j
public class CorreoServiceImpl implements CorreoService {

    private final CorreoConfig config;
    private final SesionCorreoFactory sesionFactory;
    private final GeneradorMimeMessage generadorMensaje;
    private final CorreoSender sender;

    /**
     * Constructor que inicializa los componentes necesarios para el envío de correos.
     *
     * @param config configuración SMTP ya cargada
     */
    public CorreoServiceImpl(CorreoConfig config) {
        this.config = config;
        this.sesionFactory = new SesionCorreoFactory();
        this.generadorMensaje = new GeneradorMimeMessage();
        this.sender = new CorreoSender();
    }

    /**
     * Envía un correo electrónico con los parámetros especificados.
     *
     * @param remitente     dirección de correo del remitente
     * @param destinatarios lista de destinatarios
     * @param asunto        asunto del mensaje
     * @param cuerpo        contenido del mensaje
     * @throws EmailException si ocurre un error durante el proceso de envío
     */
    @Override
    public void enviarCorreo(String remitente, List<String> destinatarios, String asunto, String cuerpo) {
        log.debug("Preparando envío de correo desde [{}] a [{}]", remitente, String.join(", ", destinatarios));
        try {
            // Crear sesión SMTP autenticada
            Session sesion = sesionFactory.crearSesion(config);
            log.debug("Sesión SMTP creada correctamente.");

            // Crear objeto de dominio del mensaje
            CorreoMensaje mensaje = new CorreoMensaje(remitente, destinatarios, asunto, cuerpo);
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
