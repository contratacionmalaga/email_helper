package local.jarios.email;

import local.jarios.email.api.EmailSender;
import local.jarios.email.api.EmailServiceImpl;
import local.jarios.email.exception.EmailServiceException;
import local.jarios.email.model.EmailData;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static local.jarios.email.common.util.Constantes.*;
import static org.junit.jupiter.api.Assertions.*;

class EmailDemoIntegrationTest {

    private Properties buildValidProps() {
        Properties props = new Properties();
        props.setProperty(SMTP_USER, "user@example.com");
        props.setProperty(SMTP_PASSWORD, "password");
        props.setProperty(SMTP_HOST, "smtp.example.com");
        props.setProperty(SMTP_PORT, "587");
        props.setProperty(SMTP_AUTH, "true");
        props.setProperty(SMTP_STARTTLS, "true");
        return props;
    }

    private EmailData buildValidEmail() {
        return new EmailData("sender@example.com", "receiver@example.com", "Test subject", "Test body");
    }

    @Test
    void testCorreoValido_noLanzaExcepcion() {
        Properties props = buildValidProps();
        EmailData data = buildValidEmail();

        EmailSender mockSender = (session, message) -> {
            // simulamos el envío, no hacemos nada
        };

        EmailServiceImpl service = new EmailServiceImpl(mockSender);

        assertDoesNotThrow(() -> service.sendEmail(props, data));
    }

    @Test
    void testPropiedadesFaltantes_lanzaExcepcion() {
        Properties props = buildValidProps();
        props.remove(SMTP_PASSWORD); // falta propiedad obligatoria
        EmailData data = buildValidEmail();

        EmailSender mockSender = (s, m) -> {};

        EmailServiceImpl service = new EmailServiceImpl(mockSender);

        EmailServiceException ex = assertThrows(EmailServiceException.class,
                () -> service.sendEmail(props, data));

        assertTrue(ex.getMessage().contains("Falta propiedad obligatoria"));
    }

    @Test
    void testCorreoFromInvalido_lanzaExcepcion() {
        Properties props = buildValidProps();
        EmailData data = new EmailData("no-es-un-email", "receiver@example.com", "Asunto", "Cuerpo");

        EmailSender mockSender = (s, m) -> {};

        EmailServiceImpl service = new EmailServiceImpl(mockSender);

        EmailServiceException ex = assertThrows(EmailServiceException.class,
                () -> service.sendEmail(props, data));

        assertTrue(ex.getMessage().contains("Email 'from' inválido"));
    }

    @Test
    void testCorreoToInvalido_lanzaExcepcion() {
        Properties props = buildValidProps();
        EmailData data = new EmailData("sender@example.com", "invalido-email", "Asunto", "Cuerpo");

        EmailSender mockSender = (s, m) -> {};

        EmailServiceImpl service = new EmailServiceImpl(mockSender);

        EmailServiceException ex = assertThrows(EmailServiceException.class,
                () -> service.sendEmail(props, data));

        assertTrue(ex.getMessage().contains("Email(s) 'to' inválidos"));
    }

    @Test
    void testErrorInternoEnEmailSender_lanzaEmailServiceException() {
        Properties props = buildValidProps();
        EmailData data = buildValidEmail();

        EmailSender throwingSender = (s, m) -> {
            throw new jakarta.mail.MessagingException("Simulando fallo SMTP");
        };

        EmailServiceImpl service = new EmailServiceImpl(throwingSender);

        EmailServiceException ex = assertThrows(EmailServiceException.class,
                () -> service.sendEmail(props, data));

        assertEquals("Error al enviar el correo electrónico", ex.getMessage());
        assertNotNull(ex.getCause());
    }
}
