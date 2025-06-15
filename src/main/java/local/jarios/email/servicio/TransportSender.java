package local.jarios.email.servicio;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;

/**
 * Interfaz que define el contrato para enviar mensajes de email.
 * Permite abstraer la llamada a la librería Jakarta Mail para facilitar
 * la simulación y testeo.
 */
public interface TransportSender {

    /**
     * Envía el mensaje de email.
     *
     * @param message El mensaje de email a enviar.
     * @throws MessagingException Si ocurre un error durante el envío.
     */
    void send(Message message) throws MessagingException;
}
