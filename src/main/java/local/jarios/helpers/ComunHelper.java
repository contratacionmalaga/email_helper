package local.jarios.helpers;

import local.jarios.exceptions.MiUnknownHostException;
import local.jarios.managers.ManagerGsons;
import local.jarios.utils.ConstantesGenerales;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * @author Juan Antonio
 */
@Slf4j
public final class ComunHelper {

    /**
     * CONSTRUCTOR PRIVADO DE LA CLASE PUESTO QUE ESTA FINAL
     */
    private ComunHelper() { }

    /**
     * Devuelve el nombre del equipo que está ejecutando el código
     * @return String con el nombre del Equipo
     * @throws MiUnknownHostException Excepción en caso de no poder acceder
     */
    public static String getHostName () throws MiUnknownHostException {

        ///
        try {
            ///
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            ///
            log.info(ex.getMessage());
            ///
            throw new MiUnknownHostException(ex);
        }
    }

    /**
     * Función encargada de imprimir un objeto
     * @param object El objeto que voy a imprimir
     */
    public static void imprimir(Object object) {

        /// Imprimiendo el objeto
        Arrays
                .stream(
                        ManagerGsons
                                .objectToJsonPretty(object)
                                .split(ConstantesGenerales.CR))
                .forEach(log::info);
    }
}
