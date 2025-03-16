package local.jarios.helpers;

import local.jarios.entity.Estadistica;
import local.jarios.exceptions.MiUnknownHostException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * @author Juan Antonio
 */
@Slf4j
public final class MiMailHelper {

    private MiMailHelper() { }

    public static String getCuerpoMensaje(Estadistica estadisticaEntity) throws MiUnknownHostException {

        return "<!DOCTYPE html>"
                + "<html lang='es'>"
                + "<head>"
                + "<meta charset='UTF-8'>"
                + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "<title>Estadisticas</title>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; background-color: #f4f4f9; margin: 0; padding: 0; }"
                + ".container { "
                + "width: 80%; "
                + "margin: 0 auto; background-color: #ffffff; "
                + "box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); "
                + "padding: 20px; }"
                + ".header { background-color: #007bff; color: #ffffff; padding: 10px 0; text-align: center; }"
                + ".content { padding: 20px; }"
                + ".content h2 { color: #333333; }"
                + ".stats-table { width: 100%; border-collapse: collapse; margin-top: 20px; }"
                + ".stats-table th, .stats-table td { "
                + "padding: 10px; "
                + "text-align: left; border-bottom: 1px solid #dddddd; }"
                + ".stats-table th { background-color: #f2f2f2; }"
                + ".footer { "
                + "text-align: center; "
                + "padding: 10px; "
                + "font-size: 12px; "
                + "color: #666666; "
                + "background-color: #f9f9f9; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<div class='header'><h1>Reporte de Estadisticas</h1></div>"
                + "<div class='content'>" + getTablaEstadisticas(estadisticaEntity) + "</div>"
                + "<div class='footer'><p>Reporte generado automaticamente.</p></div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }

    private static String getTablaEstadisticas(Estadistica estadistica) throws MiUnknownHostException {

        return "<table class='stats-table'>"
                + "<tr><th>Fecha y Hora del Envio</th><td>" +
                LocalDateTime.now() + "</td></tr>"
                + "<tr><th>Equipo desde el que se realiza el Envio</th><td>" +
                ComunHelper.getHostName() + "</td></tr>"
                + "<tr><th>Número de ficheros en la carpeta</th><td>" +
                estadistica.getNTotalFicherosLeidos() + "</td></tr>"
                + "<tr><th>Número de ficheros procesados</th><td>" +
                estadistica.getNTotalFicherosProcesados() + "</td></tr>"
                + "<tr><th>Tiempo de ejecución Parseo</th><td>" +
                estadistica.getDuracionParseo() + "</td></tr>"
                + "<tr><th>Tiempo de ejecución persistencia en Base de Datos</th><td>" +
                estadistica.getDuracionBaseDatos() + "</td></tr>"
                + "</table>";
    }

    /**
     * Función encargada de devolver el asunto de un mensaje de correo
     *
     * @return String Asunto del correo
     */
    public static String getAsunto() throws MiUnknownHostException {

        return String.format (
                " - Reporte de Estadisticas. Equipo: (%s). Fecha y hora: (%s)",
                ComunHelper.getHostName(),
                LocalDateTime.now());
    }
}