package local.jarios.dominio;

import java.util.List;

public class CorreoMensaje {
    private final String remitente;
    private final List<String> destinatarios;
    private final String asunto;
    private final String cuerpo;

    public CorreoMensaje(String remitente, List<String> destinatarios, String asunto, String cuerpo) {
        this.remitente = remitente;
        this.destinatarios = destinatarios;
        this.asunto = asunto;
        this.cuerpo = cuerpo;
    }

    public String getRemitente() { return remitente; }
    public List<String> getDestinatarios() { return destinatarios; }
    public String getAsunto() { return asunto; }
    public String getCuerpo() { return cuerpo; }
}

