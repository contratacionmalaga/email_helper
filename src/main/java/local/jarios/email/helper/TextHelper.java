package local.jarios.email.helper;

/**
 * Clase auxiliar con métodos comunes y utilidades generales.
 * <p>
 * Proporciona funciones para obtener el nombre del host, imprimir objetos,
 * calcular tiempos de ejecución y formatear fechas.
 * </p>
 *
 * @author Juan Antonio
 */
public final class TextHelper {

    /**
     * Constructor privado para evitar instanciación.
     */
    private TextHelper() { }

    /**
     * Obtiene el nombre del equipo donde se está ejecutando la aplicación.
     *
     * @param cadena String a recortar
     * @param tamano Entero con el valor a recortar
     * @return Nombre del host local.
     */
    public static String recortar(String cadena, int tamano) {

        if (cadena == null) {
            return null;
        }

        // Se verifica primero que la longitud sea mayor a tamano para evitar StringIndexOutOfBoundsException.
        if (cadena.length() > tamano) {
            cadena = cadena.substring(0, tamano); // Cortar a los primeros 50 caracteres
        }

        return cadena;
    }
}
