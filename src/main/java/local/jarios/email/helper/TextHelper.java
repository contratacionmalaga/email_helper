package local.jarios.email.helper;

import local.jarios.email.exception.EmailServiceException;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase auxiliar con métodos comunes y utilidades generales.
 * <p>
 * Proporciona funciones para obtener el nombre del host, imprimir objetos,
 * calcular tiempos de ejecución y formatear fechas.
 * </p>
 *
 * @author Juan Antonio
 */
@Slf4j
public final class TextHelper {

    /**
     * Constructor privado para evitar instanciación.
     */
    private TextHelper() { }

    /**
     * Obtiene el nombre del equipo donde se está ejecutando la aplicación.
     *
     * @return Nombre del host local.
     * @throws EmailServiceException Si no se puede resolver el nombre del host.
     */
    public static String recortar(String cadena, int tamano) {

        // Se verifica primero que la longitud sea mayor a tamano para evitar StringIndexOutOfBoundsException.
        if (cadena.length() > tamano) {
            cadena = cadena.substring(0, tamano); // Cortar a los primeros 50 caracteres
        }

        return cadena;
    }
}
