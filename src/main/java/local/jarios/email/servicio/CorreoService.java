package local.jarios.email.servicio;

import java.util.List;

public interface CorreoService {

    void enviarCorreo(String remitente, List<String> destinatarios, String asunto, String cuerpo);
}
