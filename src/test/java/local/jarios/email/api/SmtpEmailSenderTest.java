package local.jarios.email.api;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import static local.jarios.email.common.util.Constantes.SMTP_PROTOCOL;
import static org.mockito.Mockito.*;

class SmtpEmailSenderTest {

    @Mock
    private Session session;

    @InjectMocks
    private SmtpEmailSender emailSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendEmailSuccess() throws MessagingException {
        // Arrange
        Message message = new MimeMessage(session);
        message.setSubject("Test Subject");
        message.setText("Test Body");

        // Act & Assert
        emailSender.send(session, message);
        verify(session, times(1)).getTransport(SMTP_PROTOCOL);
    }

    @Test
    void testSendEmailFailure() throws MessagingException {
        // Arrange
        Message message = new MimeMessage(session);
        message.setSubject("Test Subject");
        message.setText("Test Body");

        doThrow(new MessagingException("SMTP error")).when(session).getTransport(SMTP_PROTOCOL);

        // Act & Assert
        Assertions.assertThrows(MessagingException.class, () -> {
            emailSender.send(session, message);
        });
    }
}
