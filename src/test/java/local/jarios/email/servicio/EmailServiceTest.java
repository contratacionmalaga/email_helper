package local.jarios.email.servicio;

import local.jarios.email.dominio.EmailMensaje;
import local.jarios.email.exception.EmailServiceException;
import local.jarios.properties.config.PropertiesManager;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Test
    void enviarCorreo_DeberiaInvocarElMetodo() throws EmailServiceException {
        // Arrange
        EmailService emailService = mock(EmailService.class);
        PropertiesManager propertiesManager = mock(PropertiesManager.class);
        EmailMensaje mensaje = new EmailMensaje(
                "remitente@dominio.com",
                List.of("destinatario@dominio.com"),
                "Asunto prueba",
                "Cuerpo del mensaje"
        );

        // Act
        emailService.enviarCorreo(propertiesManager, mensaje);

        // Assert
        verify(emailService).enviarCorreo(propertiesManager, mensaje);
    }
}
