package local.jarios.utils;

import local.jarios.enums.TipoFinalEjecucion;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Juan Antonio
 */
@Slf4j
public final class FinalDelPrograma {

    private FinalDelPrograma() {/* CONSTRUCTOR VACÍO */}

    public static void finalizar (TipoFinalEjecucion tipoFinal) {
        ///
        if (tipoFinal == TipoFinalEjecucion.CORRECTO) {
            ///
            log.info(Mensajes.FINAL_CORRECTO);
            ///
            System.exit(0);
        } else {
            ///
            log.error(Mensajes.FINAL_ERROR);
            ///
            System.exit(1);
        }
    }
}
