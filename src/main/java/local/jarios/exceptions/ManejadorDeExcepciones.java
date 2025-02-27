package local.jarios.exceptions;

import local.jarios.models.Error;
import local.jarios.helpers.ComunHelper;
import local.jarios.managers.ManagerGsons;
import local.jarios.utils.ConstantesGenerales;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public final class ManejadorDeExcepciones extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManejadorDeExcepciones.class);

    private ManejadorDeExcepciones() {}

    public static void exceptionToLog(
            String message, StackTraceElement[] listStackTraceElements) {

        ///
        String[] pilaExcepcion = ComunHelper.getArrayStringFromArrayStackTraceElement(listStackTraceElements);

        ///
        var error = new Error (message, pilaExcepcion);

        ///
        Arrays
                .stream(
                        ManagerGsons
                                .objectToJsonPretty(error)
                                .split(ConstantesGenerales.CR))
                .forEach(LOGGER::info);
    }
}
