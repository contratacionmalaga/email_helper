package local.jarios.email.common.util;

/**
 * Clase que contiene constantes generales utilizadas a lo largo de la aplicación.
 * <p>
 * Contiene cadenas comunes, formatos de fecha y caracteres de control,
 * para evitar el uso de valores mágicos en el código.
 * </p>
 *
 * <p><b>Author:</b> Juan Antonio</p>
 * <p><b>Date:</b> 04/06/2024</p>
 * <p><b>Team:</b> Juan Antonio</p>
 */
public final class Constantes {

    /**
     * Nombre de la propiedad que contiene el nombre de usuario del remitente (SMTP).
     * <p>Ejemplo: {@code noreply@empresa.com}</p>
     */
    public static final String SMTP_USER = "mail.smtp.user";

    /**
     * Nombre de la propiedad que contiene la contraseña del usuario SMTP.
     * <p>Debe mantenerse oculta en los logs por seguridad.</p>
     */
    public static final String SMTP_PASSWORD = "mail.smtp.password";

    /**
     * Nombre de la propiedad que indica si se requiere autenticación SMTP.
     * <p>Valor esperado: {@code true} o {@code false}.</p>
     */
    public static final String SMTP_AUTH = "mail.smtp.auth";

    /**
     * Nombre de la propiedad que habilita el protocolo STARTTLS.
     * <p>Valor esperado: {@code true} o {@code false}.</p>
     */
    public static final String SMTP_STARTTLS = "mail.smtp.starttls.enable";

    /**
     * Nombre de la propiedad que define el host del servidor SMTP.
     * <p>Ejemplo: {@code smtp.gmail.com}, {@code mail.miempresa.es}</p>
     */
    public static final String SMTP_HOST = "mail.smtp.host";

    /**
     * Nombre de la propiedad que define el puerto del servidor SMTP.
     * <p>Ejemplo: {@code 587} para TLS, {@code 465} para SSL.</p>
     */
    public static final String SMTP_PORT = "mail.smtp.port";

    /**
     * Nombre de la propiedad que define el puerto del servidor SMTP.
     */
    public static final String SMTP_SOCKETFACTORY_PORT = "mail.smtp.socketFactory.port";

    /**
     * Nombre de la propiedad que define el puerto del servidor SMTP.
     */
    public static final String SMTP_CHECKSERVERIDENTITY = "mail.smtp.checkserveridentity";

    /**
     * Nombre de la propiedad que define el puerto del servidor SMTP.
     */
    public static final String SMTP_PROTOCOLS = "mail.smtp.protocols";

    /**
     * Nombre de la propiedad que define el puerto del servidor SMTP.
     */
    public static final String SMTP_TRUST = "mail.smtp.trust";

    /**
     * Nombre de la propiedad que define el puerto del servidor SMTP.
     */
    public static final String SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";

    /** PATTERNS DE FECHA **/
    public static final String FORMATO_FECHA = "yyyy-MM-dd HH:mm:ss";

    /**
     * Nombre de la propiedad que define el puerto del servidor SMTP.
     * <p>Ejemplo: {@code 587} para TLS, {@code 465} para SSL.</p>
     */
    public static final int TAMANO_MAXIMO = 50;

    /**
     * Constructor privado para evitar instanciación.
     */
    private Constantes() {
        // No instanciable
    }
}
