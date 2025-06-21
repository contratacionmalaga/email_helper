package local.jarios.email.validator;

import local.jarios.email.common.util.EmailValidator;
import local.jarios.email.exception.EmailServiceException;
import local.jarios.email.model.EmailData;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.Properties;

import static local.jarios.email.common.util.Constantes.SMTP_PASSWORD;
import static local.jarios.email.common.util.Constantes.SMTP_USER;
import static org.mockito.Mockito.*;

class EmailRequestValidatorTest {

    @Mock
    private Properties props;

    @Mock
    private EmailData data;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidEmailRequest() throws EmailServiceException {
        // Arrange
        when(props.getProperty(SMTP_USER)).thenReturn("user");
        when(props.getProperty(SMTP_PASSWORD)).thenReturn("password");
        when(data.from()).thenReturn("from@example.com");
        when(data.to()).thenReturn("to@example.com");
        when(data.subject()).thenReturn("Subject");
        when(data.body()).thenReturn("Body");

        // Act & Assert
        Assertions.assertDoesNotThrow(() -> {
            EmailRequestValidator.validarEmailRequest(props, data);
        });
    }

    @Test
    void testInvalidEmailRequest() throws EmailServiceException {
        // Arrange
        when(props.getProperty(SMTP_USER)).thenReturn("user");
        when(props.getProperty(SMTP_PASSWORD)).thenReturn("password");
        when(data.from()).thenReturn("from@example.com");
        when(data.to()).thenReturn("to@example.com");
        when(data.subject()).thenReturn("Subject");
        when(data.body()).thenReturn("Body");

        // Simulate invalid email
        when(EmailValidator.isValidEmailRFC(data.from())).thenReturn(false);

        // Act & Assert
        Assertions.assertThrows(EmailServiceException.class, () -> {
            EmailRequestValidator.validarEmailRequest(props, data);
        });
    }
}
