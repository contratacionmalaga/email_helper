package local.jarios.email.helper;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import local.jarios.email.common.util.Constantes;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase auxiliar con métodos comunes y utilidades generales. Helpers: - No loguean en flujo normal
 * - Solo informan en caso de error
 *
 * @author Juan
 */
@Slf4j
public final class ComunHelper {

  private ComunHelper() {
    // No instanciable
  }

  /**
   * Obtiene el nombre del host donde se ejecuta la aplicación.
   *
   * @return nombre del host
   * @throws UnknownHostException si no se puede resolver
   */
  public static String getHostName() throws UnknownHostException {

    try {
      return InetAddress.getLocalHost().getHostName();

    } catch (UnknownHostException ex) {
      log.error("No se ha podido obtener el nombre del host.", ex);
      throw ex;
    }
  }

  /**
   * Devuelve una fecha formateada según {@link Constantes#FORMATO_FECHA}. Si la fecha es null, se
   * utiliza la fecha y hora actual.
   *
   * @param fechaHora fecha opcional
   * @return fecha formateada
   * @throws IllegalArgumentException si el formato es inválido
   */
  public static String getFechaHoraFormateada(Timestamp fechaHora) {

    try {
      LocalDateTime fecha = (fechaHora != null) ? fechaHora.toLocalDateTime() : LocalDateTime.now();

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constantes.FORMATO_FECHA);

      return fecha.format(formatter);

    } catch (IllegalArgumentException ex) {
      log.error("Formato de fecha inválido: {}", Constantes.FORMATO_FECHA, ex);
      throw ex;
    }
  }
}
