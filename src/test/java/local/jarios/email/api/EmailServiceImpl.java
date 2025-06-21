package local.jarios.email.api;

import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import local.jarios.email.exception.EmailServiceException;
import local.jarios.email.model.EmailData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.util.Properties;

import static local.jarios.email.common.util.Constantes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceImplTest {

    private EmailSender emailSender;
    private EmailServiceImpl emailService;
    private EmailData data;

    @BeforeEach
    void setup() {
        emailSender = mock(EmailSender.class);
        emailService = new EmailServiceImpl(emailSender);

        data = new EmailData(
                "from@example.com",
                "to@example.com",
                "Subject",
                "Body"
        );
    }

    @Test
    void testSendEmailSuccess() throws MessagingException {
        // Arrange
        Properties props = new Properties();
        props.setProperty(SMTP_USER, "user");
        props.setProperty(SMTP_PASSWORD, "pass");
        props.setProperty(SMTP_AUTH, "true");
        props.setProperty(SMTP_STARTTLS_ENABLE, "true");
        props.setProperty(SMTP_HOST, "smtp.example.com");
        props.setProperty(SMTP_PORT, "587");

        try (MockedConstruction<MimeMessage> mockedMimeMessage = mockConstruction(MimeMessage.class)) {
            // Act & Assert
            assertDoesNotThrow(() -> emailService.sendEmail(props, data));
            verify(emailSender).send(any(Session.class), any(Message.class));
        }
    }

    @Test
    void testSendEmail_whenSenderThrows_shouldThrowEmailServiceException() throws Exception {
        // Arrange
        Properties props = new Properties();
        props.setProperty(SMTP_USER, "user");
        props.setProperty(SMTP_PASSWORD, "pass");
        props.setProperty(SMTP_AUTH, "true");
        props.setProperty(SMTP_STARTTLS_ENABLE, "true");
        props.setProperty(SMTP_HOST, "smtp.example.com");
        props.setProperty(SMTP_PORT, "587");

        doThrow(new MessagingException("SMTP error")).when(emailSender).send(any(), any());

        // Act & Assert
        EmailServiceException ex = assertThrows(EmailServiceException.class, () ->
                emailService.sendEmail(props, data)
        );
        assertTrue(ex.getMessage().contains("Error al enviar"));
    }

    @Test
    void testSendEmail_shouldFailOnMissingProps() {
        // Arrange
        Properties props = new Properties(); // faltan propiedades obligatorias

        // Act & Assert
        EmailServiceException ex = assertThrows(EmailServiceException.class, () ->
                emailService.sendEmail(props, data)
        );
        assertTrue(ex.getMessage().contains("Falta propiedad obligatoria"));
    }
}
