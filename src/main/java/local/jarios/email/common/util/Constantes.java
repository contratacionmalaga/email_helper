package local.jarios.email.common.util;

import java.util.regex.Pattern;

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

    /** Protocolo para realizar el envío */
    public static final String PROTOCOL = "smtp";

    /** Clave por defecto */
    public static final String EMAIL_REGEX = "^[\\\\w.-]+@[\\\\w.-]+\\\\.[a-zA-Z]{2,}$";

    /** Nombre sin extensión del fichero app.properties */
    public static final Pattern EMAIL_PATTERN =Pattern.compile(EMAIL_REGEX);

    /**
     * Constructor privado para evitar instanciación.
     */
    private Constantes() {
        // No instanciable
    }
}
