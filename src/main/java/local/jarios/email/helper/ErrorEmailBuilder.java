package local.jarios.email.helper;

import local.jarios.email.model.EmailData;

import java.time.LocalDateTime;

public final class ErrorEmailBuilder {

  /**
   * Constructor privado.
   */
  private ErrorEmailBuilder() {
    // NO IMPLEMENTADO
  }

  /**
   * Constructor de un objeto EmailData a partir de una excepción.
   *
   * @param ex Excepción.
   * @param contexto Contexto.
   * @param from Email desde el que se envía.
   * @param to Email al que se envía.
   * @return Objeto EmailData devuelto.
   */
  public static EmailData build(
      Throwable ex,
      String contexto,
      String from,
      String to
  ) {
    String subject = "❌ ERROR en servicio de envío de email";

    String body = (
        "<h2>Error detectado</h2>%n"
            + "<p><b>Contexto:</b> %s</p>%n"
            + "<p><b>Fecha:</b> %s</p>%n"
            + "<p><b>Excepción:</b> %s</p>%n"
            + "%n"
            + "<h3>Stacktrace</h3>%n"
            + "<pre>%s</pre>%n"
    ).formatted(
        EmailHelper.escapeHtml(contexto),
        LocalDateTime.now(),
        EmailHelper.escapeHtml(ex.getClass().getName() + ": " + ex.getMessage()),
        EmailHelper.escapeHtml(ExceptionUtils.stackTraceToString(ex))
    );

    return new EmailData(from, to, subject, body);
  }
}
