package local.jarios.email.helper;

import local.jarios.email.model.EmailData;

import java.time.LocalDateTime;
import java.util.Objects;

public final class ErrorEmailBuilder {

    public static final String DEFAULT_ERROR_SUBJECT = "ERROR en servicio de envio de email";
    public static final int DEFAULT_STACKTRACE_MAX_LENGTH = 6000;

    /**
     * Constructor privado.
     */
    private ErrorEmailBuilder() {
        // No instanciable
    }

    /**
     * Constructor de un objeto EmailData a partir de una excepcion.
     *
     * @param ex Excepcion.
     * @param contexto Contexto.
     * @param from Email desde el que se envia.
     * @param to Email al que se envia.
     * @return Objeto EmailData devuelto.
     */
    public static EmailData build(
        Throwable ex,
        String contexto,
        String from,
        String to
    ) {
        return build(
            ex,
            contexto,
            from,
            to,
            DEFAULT_ERROR_SUBJECT,
            DEFAULT_STACKTRACE_MAX_LENGTH
        );
    }

    /**
     * Constructor configurable de un objeto EmailData a partir de una excepcion.
     *
     * @param ex Excepcion.
     * @param contexto Contexto.
     * @param from Email desde el que se envia.
     * @param to Email al que se envia.
     * @param subject Asunto del correo de error.
     * @param maxStackTraceLength Longitud maxima del stacktrace incluido.
     * @return Objeto EmailData devuelto.
     */
    public static EmailData build(
        Throwable ex,
        String contexto,
        String from,
        String to,
        String subject,
        int maxStackTraceLength
    ) {
        Throwable error = Objects.requireNonNull(ex, "ex");
        String stackTrace = truncate(
            ExceptionUtils.stackTraceToString(error),
            maxStackTraceLength
        );

        String body = (
            "<h2>Error detectado</h2>%n"
                + "<p><b>Contexto:</b> %s</p>%n"
                + "<p><b>Fecha:</b> %s</p>%n"
                + "<p><b>Excepcion:</b> %s</p>%n"
                + "%n"
                + "<h3>Stacktrace</h3>%n"
                + "<pre>%s</pre>%n"
        ).formatted(
            EmailHelper.escapeHtml(contexto),
            LocalDateTime.now(),
            EmailHelper.escapeHtml(error.getClass().getName() + ": " + error.getMessage()),
            EmailHelper.escapeHtml(stackTrace)
        );

        return new EmailData(from, to, subject, body);
    }

    private static String truncate(String value, int maxLength) {

        int safeMaxLength = Math.max(0, maxLength);
        if (value.length() <= safeMaxLength) {
            return value;
        }

        return value.substring(0, safeMaxLength) + System.lineSeparator() + "[stacktrace truncado]";
    }
}
