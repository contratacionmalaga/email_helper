package local.jarios.email.helper;

/**
 * Helper responsable de construir el contenido HTML y el asunto
 * de los correos electrónicos.
 *
 * <p>
 * Esta clase no realiza logging en el flujo normal.
 * El logging debe hacerse en la capa de servicio o aplicación.
 * </p>
 *
 * @author Juan
 * @since 2025-02-28
 */
public final class EmailHelper {

    private EmailHelper() {
        // No instanciable
    }

    /* ===================== */
    /* HTML COMÚN */
    /* ===================== */

    public static String getCabeceraHtml() {
        return "<!DOCTYPE html><html lang='es'>";
    }

    public static String getPieHtml() {
        return "</html>";
    }

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

    public static String getCabeceraBody(String titulo) {
        return
            "<body>" +
                "<div class='container'>" +
                "<div class='header'><h1>" + escapeHtml(titulo) + "</h1></div>";
    }

    public static String getPieBody() {
        return
            "<div class='footer'><p>Reporte generado automáticamente.</p></div>" +
                "</div>" +
                "</body>";
    }

    public static String getInicioTable() {
        return "<table class='stats-table'>";
    }

    public static String getPieTable() {
        return "</table>";
    }

    public static String getFila(String key, String value) {
        return "<tr><th>" + escapeHtml(key) + "</th><td>" + escapeHtml(value) + "</td></tr>";
    }

    /* ===================== */
    /* ASUNTO */
    /* ===================== */

    public static String getAsunto(
        String appName,
        String appVersion,
        String equipo,
        boolean success) {

        String status = success
            ? "Ejecución SIN ERRORES"
            : "Ejecución CON ERRORES";

        return String.format(
            "[%s-%s] - %s - %s - %s",
            appName,
            appVersion,
            equipo,
            status,
            ComunHelper.getFechaHoraFormateada(null)
        );
    }

    /* ===================== */
    /* CUERPO */
    /* ===================== */

    private static String construirCuerpo(String[][] filas, boolean isExcepcion) {

        String titulo = isExcepcion
            ? "Errores durante la ejecución"
            : "Estadísticas de la ejecución";

        StringBuilder cuerpo = new StringBuilder();

        cuerpo.append(getCabeceraHtml())
            .append(getHead())
            .append(getCabeceraBody(titulo))
            .append(getInicioTable());

        for (String[] fila : filas) {
            cuerpo.append(getFila(fila[0], fila[1]));
        }

        cuerpo.append(getPieTable())
            .append(getPieBody())
            .append(getPieHtml());

        return cuerpo.toString();
    }

    public static String getCuerpoEstadistica(String[][] estadistica) {
        return construirCuerpo(estadistica, false);
    }

    public static String getCuerpoExcepcion(String[] excepcion) {

        String[][] filas = new String[excepcion.length][2];

        for (int i = 0; i < excepcion.length; i++) {
            filas[i][0] = "Traza del error";
            filas[i][1] = excepcion[i];
        }

        return construirCuerpo(filas, true);
    }

    static String escapeHtml(String value) {

        if (value == null) {
            return "";
        }

        return value
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }
}
