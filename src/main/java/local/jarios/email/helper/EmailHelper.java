package local.jarios.email.helper;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * Clase helper encargada de construir mensajes de correo electrónico HTML con estadísticas del procesamiento.
 * También genera el asunto del mensaje.
 *
 * @author Juan
 * @since 2025-02-28
 */
@Slf4j
public final class EmailHelper {

    /**
     * Constructor privado para evitar la instanciación de la clase utilitaria {@code MiMailHelper}.
     */
    private EmailHelper() {
        // No instanciable
    }

    /**
     * Genera el cuerpo del mensaje HTML con los datos de la ejecución.
     *
     * @return Cadena HTML representando el HEAD de la página web quese enviará por correo.
     */
    public static String getCabeceraHtml() {


        return
                "<!DOCTYPE html>" +
                        "<html lang='es'>";
    }

    /**
     * Genera el cuerpo del mensaje HTML con los datos de la ejecución.
     *
     * @return Cadena HTML representando el HEAD de la página web quese enviará por correo.
     */
    public static String getPieHtml() {


        return
                "</html>";
    }

    /**
     * Genera el cuerpo del mensaje HTML con los datos de la ejecución.
     *
     * @return Cadena HTML representando el HEAD de la página web quese enviará por correo.
     */
    public static String getHead() {


        return
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>Estadísticas</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #f4f4f9; margin: 0; padding: 0; }" +
                ".container { width: 80%; margin: 0 auto; background-color: #ffffff; " +
                "box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); padding: 20px; }" +
                ".header { background-color: #007bff; color: #ffffff; padding: 10px 0; text-align: center; }" +
                ".content { padding: 20px; }" +
                ".content h2 { color: #333333; }" +
                ".stats-table { width: 100%; border-collapse: collapse; margin-top: 20px; }" +
                ".stats-table th, .stats-table td { padding: 10px; text-align: left; border-bottom: 1px solid #dddddd; }" +
                ".stats-table th { background-color: #f2f2f2; }" +
                ".footer { text-align: center; padding: 10px; font-size: 12px; color: #666666; background-color: #f9f9f9; }" +
                "</style>" +
                "</head>";
    }

    /**
     * Genera el fragmento HTML correspondiente a la cabecera del cuerpo del mensaje,
     * incluyendo el título principal del reporte.
     *
     * @param titulo Texto que se mostrará como encabezado dentro del contenido HTML.
     * @return Una cadena HTML representando la sección de cabecera con el título especificado.
     */
    public static String getCabeceraBody(String titulo) {


       return
                "<body>" +
                "<div class='container'>" +
                "<div class='header'><h1>" + titulo + "</h1></div>";
    }

    /**
     * Genera el cuerpo del mensaje HTML con los datos de la ejecución.
     *
     * @return Cadena HTML representando el cuerpo del mensaje.
     */
    public static String getPieBody(){


        return
                "<div class='footer'><p>Reporte generado automáticamente.</p></div>" +
                        "</div>" +
                        "</body>";
    }

    /**
     * Construye una tabla HTML con los valores estadísticos y de entorno.
     *
     * @return Cadena HTML con la tabla de datos.
     */
    public static String getInicioTable() {


        return
                "<table class='stats-table'>";
    }

    /**
     * Construye una tabla HTML con los valores estadísticos y de entorno.
     *
     * @return Cadena HTML con la tabla de datos.
     */
    public static String getPieTable() {


        return
                "</table>";
    }

    /**
     * Construye una tabla HTML con los valores estadísticos y de entorno.
     *
     * @param key Objeto con las estadísticas procesadas.
     * @param value Objeto con las estadísticas procesadas.
     * @return Cadena HTML con la tabla de datos.
     */
    public static String getFila(String key, String value) {


        return
                "<tr><th>" + key + "</th><td>" + value + "</td></tr>";
    }

    /**
     * Crea el asunto del correo de notificación de ejecución.
     *
     * <p>Incluye el nombre de la aplicación, versión, equipo, estado de la ejecución
     * (con o sin errores) y la fecha/hora actual formateada.</p>
     *
     * @param appName     the name of the application, not null
     * @param appVersion  the version of the application, not null
     * @param equipo      the identifier of the host or team executing the import, not null
     * @param success     indicates if execution succeeded (true) or failed (false)
     * @return the formatted subject line, never null
     */
    public static String getAsunto(String appName, String appVersion, String equipo, boolean success) {
        String status = success ? "Ejecución SIN ERRORES" : "Ejecución CON ERRORES";
        String asunto = String.format("[%s-%s] - %s - %s - %s",
                appName, appVersion, equipo, status, ComunHelper.getFechaHoraFormateada(null));
        log.debug("[getAsunto] - Asunto: {}", asunto);
        return asunto;
    }

    /**
     * Construye el cuerpo del correo electrónico de notificación de ejecución.
     *
     * <p>Dependiendo del contenido proporcionado, se genera un cuerpo HTML con las estadísticas
     * de ejecución o con los detalles de la excepción.</p>
     *
     * @param filas matriz bidimensional con los datos de las filas a incluir en la tabla; cada fila es un array de Strings
     * @param isExcepcion indica si las filas corresponden a una excepción (true) o a estadísticas (false)
     * @return el contenido HTML del cuerpo del correo; nunca {@code null}
     */
    private static String construirCuerpo(String[][] filas, boolean isExcepcion) {
        StringBuilder cuerpo = new StringBuilder();

        String titulo = isExcepcion ? "Estadísticas de la ejecución" : "Errores durante la ejecución";
        log.debug("[construirCuerpo] - Titulo del email: {}", titulo);

        // Cabecera HTML común
        cuerpo.append(EmailHelper.getCabeceraHtml())
                .append(EmailHelper.getHead())
                .append(EmailHelper.getCabeceraBody(titulo))
                .append(EmailHelper.getInicioTable());

        for (String[] fila : filas) {
            // En excepciones solo se usa la primera columna y null para la segunda
            cuerpo.append(getFila(fila[0], fila[1]));
        }

        // Pie de la tabla y del HTML
        cuerpo.append(EmailHelper.getPieTable())
                .append(EmailHelper.getPieBody())
                .append(EmailHelper.getPieHtml());

        log.debug("[construirCuerpo] - Obtención de cuerpo a partir de {}", isExcepcion ? "excepción." : "estadística.");
        return cuerpo.toString();
    }

    /**
     * Construye el cuerpo del correo electrónico con las estadísticas de ejecución.
     *
     * @param estadistica matriz bidimensional con los datos de las estadísticas de ejecución
     * @return el contenido HTML del cuerpo del correo con estadísticas; nunca {@code null}
     */
    public static String getCuerpoEstadistica(String[][] estadistica) {
        return construirCuerpo(estadistica, false);
    }

    /**
     * Construye el cuerpo del correo electrónico con los detalles de una excepción.
     *
     * @param excepcion array con los detalles de la excepción en caso de error
     * @return el contenido HTML del cuerpo del correo con detalles de la excepción; nunca {@code null}
     */
    public static String getCuerpoExcepcion(String[] excepcion) {
        log.debug("[getCuerpoExcepcion] - Vector Excepción: {}", Arrays.toString(excepcion));
        // Convertimos el array unidimensional a bidimensional para reutilizar construirCuerpo
        String[][] filas = new String[excepcion.length][2];
        log.debug("[getCuerpoExcepcion] - Creada matriz de tamañano: [{}x2]", excepcion.length);
        for (int i = 0; i < excepcion.length; i++) {
            filas[i][0] = "Traza del error";
            filas[i][1] = excepcion[i];
            log.debug("[getCuerpoExcepcion] - Fila '{}' de la matriz: [{},{}]", i, filas[i][0], filas[i][1]);
        }
        return construirCuerpo(filas, true);
    }

}
