package local.jarios.config;

import lombok.Getter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CorreoConfig {
    private final String smtpHost;
    private final int smtpPort;
    @Getter
    private final String usuario;
    @Getter
    private final String contrasena;
    private final boolean usarTLS;

    public CorreoConfig(String rutaArchivoProperties) throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(rutaArchivoProperties)) {
            props.load(fis);
        }

        this.smtpHost = props.getProperty("correo.smtp.host");
        this.smtpPort = Integer.parseInt(props.getProperty("correo.smtp.port", "587"));
        this.usarTLS = Boolean.parseBoolean(props.getProperty("correo.smtp.tls", "true"));

        this.usuario = resolverVariable(props.getProperty("correo.smtp.usuario"));
        this.contrasena = resolverVariable(props.getProperty("correo.smtp.clave"));
    }

    private String resolverVariable(String valor) {
        if (valor != null && valor.startsWith("${") && valor.endsWith("}")) {
            String envVar = valor.substring(2, valor.length() - 1);
            return System.getenv(envVar);
        }
        return valor;
    }

    public Properties getProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", String.valueOf(usarTLS));
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", String.valueOf(smtpPort));
        return props;
    }
}
