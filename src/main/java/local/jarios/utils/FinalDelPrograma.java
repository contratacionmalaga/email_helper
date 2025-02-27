package local.jarios.utils;

import local.jarios.enums.TipoFinalEjecucion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Juan Antonio
 */
public final class FinalDelPrograma {

    private static final Logger LOGGER = LoggerFactory.getLogger(FinalDelPrograma.class);

    private FinalDelPrograma() {/* CONSTRUCTOR VACÍO */}

    public static void finalizar (TipoFinalEjecucion tipoFinal, String mensaje) {

        ///
        if (tipoFinal == TipoFinalEjecucion.CORRECTO) {

            ///
            LOGGER.info(Mensajes.FINAL_CORRECTO);

            ///
            System.exit(0);

        } else {

            ///
            LOGGER.error(mensaje);

            ///
            System.exit(1);
        }
    }
}
