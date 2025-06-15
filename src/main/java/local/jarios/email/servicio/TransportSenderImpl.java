package local.jarios.email.servicio;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Transport;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación concreta de {@link TransportSender} que utiliza
 * la clase estática {@link Transport} para enviar emails.
 */
@Slf4j
public class TransportSenderImpl implements TransportSender {

    /**
     * Constructor vacío
     */
    public TransportSenderImpl() {
        // Constructor vacío
    }

    /**
     * Envía el mensaje usando {@link Transport#send(Message)}.
     *
     * @param message El mensaje de email a enviar.
     * @throws MessagingException Si ocurre un error durante el envío.
     */
    @Override
    public void send(Message message) throws MessagingException {
        log.debug("[send] - Envío del mensaje en TransportSenderImpl.");
        Transport.send(message);
    }
}
