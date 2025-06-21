package local.jarios.email.api;

import local.jarios.email.model.EmailData;
import local.jarios.email.exception.EmailServiceException;
import org.junit.jupiter.api.*;
import org.mockito.*;

import jakarta.mail.*;

import java.util.Properties;

import static org.mockito.Mockito.*;

class EmailServiceImplTest {

    @Mock
    private EmailSender emailSender;

    @Mock
    private Session session;

    @Mock
    private EmailData data;

    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        emailService = new EmailServiceImpl(emailSender);
    }

    @Test
    void testSendEmailSuccess() throws EmailServiceException {
        // Arrange
        when(data.from()).thenReturn("from@example.com");
        when(data.to()).thenReturn("to@example.com");
        when(data.subject()).thenReturn("Subject");
        when(data.body()).thenReturn("Body");

        // Act & Assert
        Assertions.assertDoesNotThrow(() -> {
            emailService.sendEmail(new Properties(), data);
        });
    }

    @Test
    void testSendEmailFailure() throws EmailServiceException, MessagingException {
        // Arrange
        when(data.from()).thenReturn("from@example.com");
        when(data.to()).thenReturn("to@example.com");
        when(data.subject()).thenReturn("Subject");
        when(data.body()).thenReturn("Body");

        doThrow(new MessagingException("SMTP error")).when(emailSender).send(any(Session.class), any());

        // Act & Assert
        Assertions.assertThrows(EmailServiceException.class, () -> {
            emailService.sendEmail(new Properties(), data);
        });
    }
}
