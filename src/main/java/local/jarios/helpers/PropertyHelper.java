package local.jarios.helpers;

import local.jarios.utils.ConstantesGenerales;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * @author Juan Antonio
 */
public final class PropertyHelper {

    ///
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyHelper.class);

    ///
    private PropertyHelper() { }

    ///
    public static void imprimirProperty (Properties properties, String[] sensitiveKeys) {


        /// Ordenar las propiedades por clave (key)
        Map<String, String> sortedProperties = new TreeMap<>();

        /// Convertir las propiedades en un TreeMap para ordenarlas
        properties.forEach((key, value) -> sortedProperties.put(key.toString(), value.toString()));

        ///
        sortedProperties.forEach((key, value) -> {

            ///
            if ((sensitiveKeys == null) || (!isSensitiveKey(key, sensitiveKeys))) {

                ///
                imprimirPropiedad(key, value);
            }
        });
    }

    /// Verificar si la clave es sensible
    private static boolean isSensitiveKey(String key, String[] sensitiveKeys) {

        ///
        for (String sensitiveKey : sensitiveKeys) {

            ///
            if (key.contains(sensitiveKey)) {

                ///
                return true;
            }
        }

        ///
        return false;
    }

    ///
    private static void imprimirPropiedad(Object key, Object value) {

        ///
        LOGGER.info("{}Propiedad leída: {} = {}", ConstantesGenerales.TABULADOR_1, key, value);
    }
}
