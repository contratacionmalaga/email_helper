package local.jarios.email.helper;

import local.jarios.email.common.util.Constantes;
import local.jarios.email.exception.EmailServiceException;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase auxiliar con métodos comunes y utilidades generales.
 * <p>
 * Proporciona funciones para obtener el nombre del host, imprimir objetos,
 * calcular tiempos de ejecución y formatear fechas.
 * </p>
 *
 * @author Juan Antonio
 */
@Slf4j
public final class ComunHelper {

    /**
     * Constructor privado para evitar instanciación.
     */
    private ComunHelper() { }

    /**
     * Obtiene el nombre del equipo donde se está ejecutando la aplicación.
     *
     * @return Nombre del host local.
     * @throws EmailServiceException Si no se puede resolver el nombre del host.
     */
    public static String getHostName() throws EmailServiceException {
        log.debug("[getHostName] -");
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            log.debug("[getHostName] - Nombre del host obtenido: {}", hostName);
            return hostName;
        } catch (UnknownHostException ex) {
            log.error("[getHostName] - Error al obtener el nombre del host", ex);
            throw new EmailServiceException("[getHostName] - Error al obtener el nombre del host", ex);
        }
    }

    /**
     * Formatea una marca temporal {@link Timestamp} a cadena con formato
     * "yyyy-MM-dd HH:mm:ss". Si la marca es null, se formatea la fecha y hora actuales.
     *
     * @param fechaHora Marca temporal a formatear.
     * @return Fecha y hora formateadas como cadena.
     */
    public static String getFechaHoraFormateada(Timestamp fechaHora) throws EmailServiceException{
        log.debug("[getFechaHoraFormateada] -");
        String fechaFormateada = null;
        try {
            LocalDateTime fecha = (fechaHora != null) ? fechaHora.toLocalDateTime() : LocalDateTime.now();
            log.debug("[getFechaHoraFormateada] - Valor de fecha: {}", fecha);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constantes.FORMATO_FECHA);
            log.debug("[getFechaHoraFormateada] - Valor del formateador: {}", formatter);
            fechaFormateada = fecha.format(formatter);
            log.debug("[getFechaHoraFormateada] - Fecha formateada: {}", fechaFormateada);
        } catch (IllegalArgumentException ex) {
            log.error("[getFechaHoraFormateada] - Error en el argumentos. Error: {}", ex.getMessage());
            throw new EmailServiceException("[getFechaHoraFormateada] - Error en el argumentos.", ex);
        }
        return fechaFormateada;
    }
}
