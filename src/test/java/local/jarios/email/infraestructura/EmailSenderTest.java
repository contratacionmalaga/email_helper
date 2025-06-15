package local.jarios.email.infraestructura;

import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import local.jarios.email.exception.EmailSenderException;
import local.jarios.email.servicio.TransportSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link EmailSender}.
 * <p>
 * Se verifican distintos escenarios del método {@code enviarEmail}, incluyendo:
 * - Envío exitoso con destinatarios.
 * - Envío exitoso sin destinatarios.
 * - Manejo de excepciones cuando falla el transporte de email.
 * </p>
 */
class EmailSenderTest {

    private EmailSender emailSender;
    private TransportSender mockTransportSender;

    /**
     * Inicializa el mock de {@link TransportSender} y la instancia de {@link EmailSender}
     * antes de cada test, inyectando el mock para evitar llamadas reales.
     */
    @BeforeEach
    void setup() {
        mockTransportSender = mock(TransportSender.class);
        emailSender = new EmailSender(mockTransportSender);
    }

    /**
     * Test que verifica que {@link EmailSender#enviarEmail(Message)} llama correctamente a
     * {@link TransportSender#send(Message)} cuando el mensaje tiene destinatarios.
     *
     * @throws Exception si ocurre algún error durante el test.
     */
    @Test
    void enviarEmail_DeberiaEnviarMensajeConDestinatarios() throws Exception {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        Address[] destinatarios = new Address[]{mock(Address.class), mock(Address.class)};
        when(mimeMessage.getAllRecipients()).thenReturn(destinatarios);
        when(destinatarios[0].toString()).thenReturn("destino1@dominio.com");
        when(destinatarios[1].toString()).thenReturn("destino2@dominio.com");

        emailSender.enviarEmail(mimeMessage);

        // Verifica que el mock del TransportSender fue invocado con el mensaje
        verify(mockTransportSender, times(1)).send(mimeMessage);
    }

    /**
     * Test que verifica que {@link EmailSender#enviarEmail(Message)} llama correctamente a
     * {@link TransportSender#send(Message)} cuando el mensaje no tiene destinatarios.
     *
     * @throws Exception si ocurre algún error durante el test.
     */
    @Test
    void enviarEmail_DeberiaEnviarMensajeSinDestinatarios() throws Exception {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mimeMessage.getAllRecipients()).thenReturn(null);

        emailSender.enviarEmail(mimeMessage);

        // Verifica que el mock del TransportSender fue invocado con el mensaje
        verify(mockTransportSender, times(1)).send(mimeMessage);
    }

    /**
     * Verifica que {@link EmailSender#enviarEmail(Message)} lance una
     * {@link MessagingException} cuando falla el envío a través del transporte.
     *
     * @throws MessagingException En caso de error en la simulación.
     */
    @Test
    void enviarEmail_DeberiaLanzarEmailSenderExceptionCuandoTransportFalla() throws Exception {
        // Crear mock de TransportSender
        TransportSender mockTransportSender = mock(TransportSender.class);

        // Simular que el mock lanza MessagingException al enviar
        doThrow(new MessagingException("Fallo simulado")).when(mockTransportSender).send(any(Message.class));

        // Crear instancia de EmailSender con mock inyectado
        EmailSender sender = new EmailSender(mockTransportSender);

        // Preparar mensaje dummy
        Properties props = new Properties();
        Session session = Session.getInstance(props);
        MimeMessage dummyMessage = new MimeMessage(session);
        dummyMessage.setRecipients(Message.RecipientType.TO, "destinatario@ejemplo.com");
        dummyMessage.setFrom("remitente@ejemplo.com");
        dummyMessage.setSubject("Asunto");
        dummyMessage.setText("Contenido");

        // Verificar que se lance EmailSenderException cuando falla el envio
        EmailSenderException ex = assertThrows(
                EmailSenderException.class,
                () -> sender.enviarEmail(dummyMessage)
        );

        // Opcional: verificar que la causa sea MessagingException
        assert(ex.getCause() instanceof MessagingException);

        // Verificar que el método send del mock fue llamado una vez
        verify(mockTransportSender, times(1)).send(any(Message.class));
    }
}
