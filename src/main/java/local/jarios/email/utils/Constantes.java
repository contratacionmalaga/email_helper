package local.jarios.email.utils;/**

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

    /** Ruta del directorio con los ficheros properties */
    public static final String CONFIG_DIR = "config";

    /** Nombre sin extensión del fichero app.properties */
    public static final String APP_PROPERTIES = "app";

    /** Nombre sin extensión del fichero app.properties */
    public static final String EMAIL_PROPERTIES = "email";

    /** Clave maestra */
    public final static String CLAVE_MAESTRA = "Malaga$$2025";

    /** Key mail.user */
    public final static String KEY_USER = "mail.user";

    /** Key mail.password */
    public final static String KEY_PASSWORD = "mail.password";

    /**
     * Constructor privado para evitar instanciación.
     */
    private Constantes() {
        // No instanciable
    }
}