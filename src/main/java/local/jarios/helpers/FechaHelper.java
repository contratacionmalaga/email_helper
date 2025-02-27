package local.jarios.helpers;

import local.jarios.utils.ConstantesGenerales;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * @author Juan Antonio
 */
public final class FechaHelper {

    private FechaHelper() { }

    public static String getFormatoFechaLargo (Timestamp timestampFechaHora) {

        /// Definir el formato deseado
        DateTimeFormatter formato = DateTimeFormatter.ofPattern(ConstantesGenerales.FORMATO_FECHA);

        /// Convertir el Timestamp a LocalDateTime
        LocalDateTime fechaLocal = timestampFechaHora.toLocalDateTime();

        /// Convertir la fecha a String con el formato
        return fechaLocal.format(formato);
    }
}
