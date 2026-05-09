package local.jarios.email.helper;

import local.jarios.email.common.util.Mensajes;
import local.jarios.email.enums.TipoFinalEjecucion;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase utilitaria para finalizar la ejecución del programa
 * registrando el resultado final mediante logs y terminando el proceso
 * con el código adecuado.
 * <p>
 * El método {@code finalizar} acepta un tipo de finalización que determina
 * si la ejecución terminó correctamente o con error y actúa en consecuencia.
 * </p>
 *
 * <p><b>Author:</b> Juan Antonio</p>
 */
@Slf4j
public final class FinalDelProgramaHelper {

    /**
     * Constructor privado para evitar instanciación.
     */
    private FinalDelProgramaHelper() {
        /* CONSTRUCTOR VACÍO */
    }

    /**
     * Finaliza la ejecución del programa.
     * @param tipoFinal Tipo de finalización de la ejecución.
     */
    public static void finalizar(TipoFinalEjecucion tipoFinal) {

        finalizar(tipoFinal, (String)null);
    }

    /**
     * Finaliza la ejecución del programa registrando un mensaje
     * de resultado y llamando a {@code System.exit} con el código
     * 0 para ejecución correcta o 1 para error.
     *
     * @param tipoFinal Tipo de finalización de la ejecución.
     */
    public static void finalizar(TipoFinalEjecucion tipoFinal, String mensajeError) {
        String mensaje;
        int exitCode;
        if (tipoFinal == TipoFinalEjecucion.CORRECTO) {
            mensaje = "La ejecución ha finalizado CORRECTAMENTE.";
            exitCode = 0;
        } else {
            mensaje = "!!!! La ejecución ha finalizado con ERRORES !!!!";
            exitCode = 1;
            if (mensajeError != null && !mensajeError.isBlank()) {
                mensaje = mensaje + " Detalle: " + mensajeError;
            }
        }

        log.info(mensaje);
        log.info("==== FINAL DE LA APLICACIÓN: version-helper ====  ");
        System.out.flush();
        System.err.flush();
        System.exit(exitCode);
    }
}

