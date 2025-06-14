package local.jarios.email;


import local.jarios.email.dominio.CorreoMensaje;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class GeneradorMimeMessage {
    public MimeMessage generarMensaje(Session session, CorreoMensaje correoMensaje) throws MessagingException {
        MimeMessage mensaje = new MimeMessage(session);
        mensaje.setFrom(new InternetAddress(correoMensaje.getRemitente()));
        mensaje.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(String.join(",", correoMensaje.getDestinatarios())));
        mensaje.setSubject(correoMensaje.getAsunto());
        mensaje.setText(correoMensaje.getCuerpo());
        return mensaje;
    }
}
