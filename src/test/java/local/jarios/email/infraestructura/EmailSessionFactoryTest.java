package local.jarios.email.infraestructura;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import local.jarios.email.exception.EmailSenderException;
import local.jarios.email.servicio.TransportSender;
import local.jarios.properties.config.PropertiesManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailSessionFactoryTest {

    private EmailSessionFactory factory;
    private PropertiesManager propertiesManager;

    @BeforeEach
    void setUp() {
        factory = new EmailSessionFactory();
        propertiesManager = mock(PropertiesManager.class);
    }

    @Test
    void getSession_DeberiaCrearSesionConCredenciales() throws Exception {
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");
        when(propertiesManager.getProperties(anyString())).thenReturn(props);

        doNothing().when(propertiesManager).printProperties(props);

        String user = "usuario@dominio.com";
        String password = "claveSecreta";

        Session session = factory.getSession(propertiesManager, user, password);

        assertNotNull(session);

        verify(propertiesManager).getProperties("email");
        verify(propertiesManager).printProperties(props);
    }

    @Test
    public void enviarEmail_DeberiaLanzarEmailSenderExceptionCuandoTransportFalla() throws Exception {
        // Setup
        TransportSender mockTransportSender = mock(TransportSender.class);
        doThrow(new MessagingException("Fallo simulado")).when(mockTransportSender).send(any(Message.class));

        EmailSender sender = new EmailSender(mockTransportSender);

        // Verificar que se lanza EmailSenderException, no MessagingException
        EmailSenderException thrown = assertThrows(
                EmailSenderException.class,
                () -> sender.enviarEmail(mock(Message.class))
        );

        // Opcional: verificar que la causa es MessagingException
        assertNotNull(thrown.getCause());
        assertInstanceOf(MessagingException.class, thrown.getCause());
        assertEquals("Fallo simulado", thrown.getCause().getMessage());

        verify(mockTransportSender).send(any(Message.class));
    }

}
