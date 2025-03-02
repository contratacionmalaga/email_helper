package local.jarios.helpers;

import local.jarios.interfaces.Actualizable;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author: juan
 * Date: 28/02/2025
 * Team:
 */

@Slf4j
public final class MapHelper {

    /**
     * CONSTRUCTOR PRIVADO DE LA CLASE PUESTO QUE ESTA FINAL
     */
    private MapHelper() { }

    /**
     *
     * @param list Lista del objeto T
     * @return Mapa con un identificador único (String) del objeto T
     */
    public static <T extends Actualizable<T>> Map<String, T> getMapFromList(List<T> list) {

        /// Defino el Map que voy a devolver --> NUNCA DEVUELVO NULL
        Map<String, T> mapaFromList = new HashMap<>();

        /// Recorro todos los elementos del la List
        for (T registro : list) {

            /// Cada elemento de la List lo inserto en el Map
            mapaFromList.put(registro.getUniqueKey(), registro);
        }

        /// Devuelvo el Map
        return mapaFromList;
    }
}
