package local.jarios.email.helper;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class ExceptionUtils {

  /** Constructor privado. */
  private ExceptionUtils() {
    // NO IMPLEMENTADO
  }

  /**
   * Utilidad para pasar un StackTrace a un String.
   *
   * @param ex Excepción que pasaremos a String.
   * @return Cadena con la información de la excepción.
   */
  public static String stackTraceToString(Throwable ex) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    ex.printStackTrace(pw);
    return sw.toString();
  }
}
