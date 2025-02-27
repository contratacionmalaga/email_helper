package local.jarios.exceptions;

import local.jarios.helpers.ComunHelper;
import local.jarios.managers.ManagerGsons;
import local.jarios.models.MiError;
import local.jarios.utils.ConstantesGenerales;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public final class MiManejadorDeExcepciones extends RuntimeException {

    ///
    private static final List<MiError> listError = new ArrayList<>();

    /// CONSTRUCTOR
    private MiManejadorDeExcepciones() {}

    /**
     * Función dedicada a almacenar en una lista los errores
     * @param message Mensaje con el error
     * @param listStackTraceElements Lista con la traza del error
     */
    public static void exceptionToLog(String message, StackTraceElement[] listStackTraceElements) {

        ///
        String[] pilaExcepcion = ComunHelper.getArrayStringFromArrayStackTraceElement(listStackTraceElements);

        ///
        listError.add(new MiError(message, pilaExcepcion));
    }

    /**
     * Imprime la lista de errores
     */
    public static void imprimirExcepciones() {

        ///
        Arrays
                .stream(
                        ManagerGsons
                                .objectToJsonPretty(listError)
                                .split(ConstantesGenerales.CR))
                .forEach(log::info);
    }
}
