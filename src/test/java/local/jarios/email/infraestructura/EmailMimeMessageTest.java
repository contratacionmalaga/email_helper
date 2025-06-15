package local.jarios.email.infraestructura;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import local.jarios.email.dominio.EmailMensaje;
import local.jarios.email.exception.EmailMimeMessageException;
import local.jarios.email.exception.EmailServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class EmailMimeMessageTest {

    private EmailMimeMessage generador;
    private Session session;

    @BeforeEach
    void setup() {
        generador = new EmailMimeMessage();
        Properties props = new Properties();
        // Propiedades mínimas para la sesión (puede ser vacío si no se usa SMTP real)
        session = Session.getInstance(props);
    }

    @Test
    void getMimeMessage_DebeCrearMimeMessageConDatosValidos() throws Exception {
        EmailMensaje correo = new EmailMensaje(
                "remitente@dominio.com",
                List.of("destino@dominio.com"),
                "Asunto de prueba",
                "<p>Mensaje HTML</p>"
        );

        MimeMessage mensaje = generador.getMimeMessage(session, correo);

        assertNotNull(mensaje);
        assertEquals("remitente@dominio.com", mensaje.getFrom()[0].toString());
        assertEquals("Asunto de prueba", mensaje.getSubject());
        assertEquals("text/html; charset=UTF-8", mensaje.getDataHandler().getContentType());
    }

    @Test
    void getMimeMessage_DebeLanzarExcepcionSiRemitenteNulo() {
        EmailMensaje correo = new EmailMensaje(
                null,
                List.of("destino@dominio.com"),
                "Asunto",
                "Cuerpo"
        );

        EmailServiceException ex = assertThrows(EmailServiceException.class, () -> {
            generador.getMimeMessage(session, correo);
        });

        assertEquals("Remitente no puede estar vacío", ex.getMessage());
    }

    @Test
    void getMimeMessage_DebeLanzarExcepcionSiRemitenteVacio() {
        EmailMensaje correo = new EmailMensaje(
                "  ",
                List.of("destino@dominio.com"),
                "Asunto",
                "Cuerpo"
        );

        EmailServiceException ex = assertThrows(EmailServiceException.class, () -> {
            generador.getMimeMessage(session, correo);
        });

        assertEquals("Remitente no puede estar vacío", ex.getMessage());
    }

    @Test
    void getMimeMessage_DebeLanzarExcepcionSiDestinatariosNulos() {
        EmailMensaje correo = new EmailMensaje(
                "remitente@dominio.com",
                null,
                "Asunto",
                "Cuerpo"
        );

        EmailServiceException ex = assertThrows(EmailServiceException.class, () -> {
            generador.getMimeMessage(session, correo);
        });

        assertEquals("Debe haber al menos un destinatario", ex.getMessage());
    }

    @Test
    void getMimeMessage_DebeLanzarExcepcionSiDestinatariosVacios() {
        EmailMensaje correo = new EmailMensaje(
                "remitente@dominio.com",
                List.of(),
                "Asunto",
                "Cuerpo"
        );

        EmailServiceException ex = assertThrows(EmailServiceException.class, () -> {
            generador.getMimeMessage(session, correo);
        });

        assertEquals("Debe haber al menos un destinatario", ex.getMessage());
    }

    @Test
    void getMimeMessage_DebeLanzarExcepcionSiDireccionInvalida() {
        EmailMensaje correo = new EmailMensaje(
                "remitente@dominio.com",
                List.of("destinatario@dominio..com"), // dirección inválida con doble punto
                "Asunto",
                "Cuerpo"
        );

        EmailMimeMessageException ex = assertThrows(EmailMimeMessageException.class, () -> {
            generador.getMimeMessage(session, correo);
        });

        assertTrue(ex.getMessage().contains("Dirección de correo inválida"));
    }
}
