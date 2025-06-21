package local.jarios.email.api;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmailSenderImplTest {

    private EmailSenderImpl emailSender;

    @BeforeEach
    void setUp() {
        emailSender = new EmailSenderImpl();
    }

    @Test
    void send_shouldCallTransportSend() throws Exception {
        try (MockedStatic<Transport> transportMock = mockStatic(Transport.class)) {
            Session session = mock(Session.class);
            Message message = mock(Message.class);

            emailSender.send(session, message);

            transportMock.verify(() -> Transport.send(message), times(1));
        }
    }

    @Test
    void send_whenTransportThrows_shouldThrowMessagingException() {
        try (MockedStatic<Transport> transportMock = mockStatic(Transport.class)) {
            Session session = mock(Session.class);
            Message message = mock(Message.class);

            transportMock.when(() -> Transport.send(any(Message.class)))
                    .thenThrow(new MessagingException("SMTP error"));

            assertThrows(MessagingException.class, () -> {
                emailSender.send(session, message);
            });
        }
    }
}
