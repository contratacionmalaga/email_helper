package local.jarios.helpers;

import local.jarios.entity.LogEntity;
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
public final class ListHelper {

    /**
     * CONSTRUCTOR PRIVADO DE LA CLASE PUESTO QUE ESTA FINAL
     */
    private ListHelper() { }

    /**
     * Método encargado de unificar dos listas de un tipo de objeto <T>
     * @param logEntity Objeto LogEntity necesario para los nuevos registros que se deban añadir a la Lista de BD
     * @param listElementosEnBaseDatos Lista con los datos existenten en la base de datos
     * @param listElementosPendientesImportar Lista con los datos que acabo de importar
     * @param <T> Objeto genérico asociado a ambas listas
     */
    public static <T extends Actualizable<T>> void unificarListas(
            LogEntity logEntity,
            List<T> listElementosEnBaseDatos,
            List<T> listElementosPendientesImportar) {

        /// Crear un mapa para los elementos en la base de datos, donde la clave es el id
        Map<String, T> mapElementosEnBaseDatos = new HashMap<>();

        /// Itero la lista de elementos en base de datos para generar el Map
        for (T elementoEnBase : listElementosEnBaseDatos) {
            mapElementosEnBaseDatos.put(elementoEnBase.getUniqueKey(), elementoEnBase);
        }

        /// Itero la lista de los elementos pendientes de importar
        for (T elementoPendiente : listElementosPendientesImportar) {

            ///  Consulto el elemento principal de la clase que he utilizado como key del Map
            T elementoEnBase = mapElementosEnBaseDatos.get(elementoPendiente.getUniqueKey());


            if (elementoEnBase != null) {
                /// Existe el elemento pendiente en el Map

                if (!elementoPendiente.equals(elementoEnBase)) {
                    /// Si son diferentes, actualizar los valores del elemento en la base de datos
                    elementoEnBase.actualizarCon(elementoPendiente);
                }

                /// Eliminar el elemento procesado del mapa (para evitar eliminarlo más tarde)
                mapElementosEnBaseDatos.remove(elementoPendiente.getUniqueKey());

            } else {

                /// Actualizo el pendiente con LogEntity
                elementoPendiente.setLogEntity(logEntity);
                /// No existe el elemento pendiente en el Map -> lo agrego
                listElementosEnBaseDatos.add(elementoPendiente);
            }
        }

        /// Eliminar los elementos de la base de datos que no están en la lista de pendientes de importar
        /// listElementosEnBaseDatos.removeIf(
        ///             elementoEnBase -> !mapElementosEnBaseDatos.containsKey(elementoEnBase.getUniqueKey()));
    }
}
