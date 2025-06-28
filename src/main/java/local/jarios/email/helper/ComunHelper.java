package local.jarios.email.helper;

import local.jarios.email.common.util.Constantes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
public final class ComunHelper {

    /** LOGGER asociado al componente. */
    private static final Logger LOGGER = LogManager.getLogger("local.jarios.email");

    /**
     * Constructor privado para evitar instanciación.
     */
    private ComunHelper() { }

    /**
     * Obtiene el nombre del equipo donde se está ejecutando la aplicación.
     *
     * @return Nombre del host local.
     * @throws UnknownHostException Si no se puede resolver el nombre del host.
     */
    public static String getHostName() throws UnknownHostException {

        try {

            String hostName = InetAddress.getLocalHost().getHostName();
            LOGGER.debug("[getHostName] - Nombre del host obtenido: {}", hostName);
            return hostName;

        } catch (UnknownHostException ex) {

            String msg = String.format("[getHostName] - Error al obtener el nombre del host. Error: %s", ex.getMessage());
            LOGGER.error(msg, ex);
            throw new UnknownHostException(msg);

        }
    }

    /**
     * Formatea una marca temporal {@link Timestamp} a cadena con formato
     * "yyyy-MM-dd HH:mm:ss". Si la marca es null, se formatea la fecha y hora actuales.
     *
     * @param fechaHora Marca temporal a formatear.
     * @return Fecha y hora formateadas como cadena.
     */
    public static String getFechaHoraFormateada(Timestamp fechaHora) throws IllegalArgumentException {

        String fechaFormateada;

        try {

            LocalDateTime fecha = (fechaHora != null) ? fechaHora.toLocalDateTime() : LocalDateTime.now();
            LOGGER.debug("[getFechaHoraFormateada] - Valor de fecha: {}", fecha);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constantes.FORMATO_FECHA);
            LOGGER.debug("[getFechaHoraFormateada] - Valor del formateador: {}", formatter);
            fechaFormateada = fecha.format(formatter);
            LOGGER.debug("[getFechaHoraFormateada] - Fecha formateada: {}", fechaFormateada);

        } catch (IllegalArgumentException ex) {

            String msg = String.format("[getFechaHoraFormateada] - Error en el argumentos. Error: %s", ex.getMessage());
            LOGGER.error(msg, ex);
            throw new IllegalArgumentException(msg, ex);

        }

        return fechaFormateada;
    }
}
