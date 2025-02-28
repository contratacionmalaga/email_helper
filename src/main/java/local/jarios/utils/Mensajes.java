package local.jarios.utils;

/**
 * Description: LogEntity
 * Author: Juan Antonio
 * Date: 04/06/2024
 * Team: Juan Antonio
 */
public final class Mensajes {

    public static final String PROPERTY_LOG =
            "Contenido de las variables definidas en los ficheros properties.";

    public static final String NUEMRO_FICHEROS_LEIDOS =
            "Se han leído {} ficheros del directorio: {}";

    public static final String NUEMRO_FICHEROS_PROCESADOS =
            "Se han procesado un total de {} ficheros";

    public static final String RUTA_FICHEROS =
            "Ruta desde la que se importarán los ficheros: {}";

    public static final String FINAL_CORRECTO =
            "La ejecución ha finalizado CORRECTAMENTE.";

    public static final String RESUMEN_EJECUCION =
            "***** RESUMEN DE LA IMPORTACIÓN *****";

    public static final String ENTIDADES =
            "Se han encontrado {} entidades dentro del paquete {}.";

    public static final String EXCEPTION_ERROR_MIMAIL_ENVIARMAIL =
            "Excepción ocurrida en el método MiMail.enviarMail(). Mensaje: {}";

    public static final String EXCEPTION_ERROR_MIMAIL_MIMAIL =
            "Excepción ocurrida en el método MiMail.MiMail(). Mensaje: {}";

    public static final String EXCEPTION_ERROR_SERVICEIMPL_SAVE =
            "Excepción ocurrida en el método ServiceImpl.save(). Mensaje: {}";

    public static final String EXCEPTION_ERROR_REPOSITORYIMPL_SAVE =
            "Excepción ocurrida en el método RespositoryImpl.save(). Mensaje: {}";

    public static final String DROP_TABLE =
            "{}Borrada la tabla: {}";

    public static final String CREATE_TABLE =
            "{}Creada la tabla: {}";

    public static final String INSERT_RECORDS =
            "{}Insertardos {} registros en la tabla {}";

    private Mensajes() {}

}
