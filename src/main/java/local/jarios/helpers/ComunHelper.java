package local.jarios.helpers;

import local.jarios.exceptions.MiManejadorDeExcepciones;
import local.jarios.managers.ManagerGsons;
import local.jarios.utils.ConstantesGenerales;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * @author Juan Antonio
 */
public final class ComunHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComunHelper.class);

    private ComunHelper() { }

    public static String getHostName () {

        String hostName = null;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            LOGGER.error("Error al obtener el nombre del host. Mensaje error: {}", ex.getMessage());
            MiManejadorDeExcepciones.exceptionToLog (ex.getMessage(), ex.getStackTrace());
        }

        return hostName;
    }

    public static String[] getArrayStringFromArrayStackTraceElement(StackTraceElement[] listStackTraceElements) {

        return Arrays.stream(listStackTraceElements)
                .map(StackTraceElement::toString)
                .toArray(String[]::new);
    }

    /**
     * Función encargada de imprimir un objeto
     *
     * @param object El objeto que voy a imprimir
     */
    public static void imprimir(Object object) {

        /// Imprimiendo el objeto
        Arrays
                .stream(
                        ManagerGsons
                                .objectToJsonPretty(object)
                                .split(ConstantesGenerales.CR))
                .forEach(LOGGER::info);
    }
}
