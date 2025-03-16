package local.jarios.helpers;

import local.jarios.entity.Log;
import local.jarios.interfaces.Actualizable;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

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
            Log logEntity,
            List<T> listElementosEnBaseDatos,
            List<T> listElementosPendientesImportar) {

        ///  DEFINICIÓN DE VARIABLES LOCALES
        Map<String, T> mapElementosEnBaseDatos = new HashMap<>();   /// Map con los elementos de la Base de Datos
        Set<String> conjuntoUniqueKeys = new HashSet<>();           /// Conjunto para llevar un registro de las IDs procesadas

        /// Itero la lista de elementos en base de datos para generar el Map
        for (T elementoEnBaseDatos : listElementosEnBaseDatos) {
            mapElementosEnBaseDatos.put(elementoEnBaseDatos.getUniqueKey(), elementoEnBaseDatos);
        }

        /// Itero la lista de los elementos pendientes de importar
        for (T elementoPendienteImportar : listElementosPendientesImportar) {

            ///  Consulto el elemento principal de la clase que he utilizado como key del Map
            T elementoEnBaseDatos = mapElementosEnBaseDatos.get(elementoPendienteImportar.getUniqueKey());

            if (elementoEnBaseDatos != null) {
                /// Existe el elemento pendiente en el MAP --> Veo si es igual al existente en la base de datos

                ///  Actualizo el conjunto de elementos procesados
                conjuntoUniqueKeys.add(elementoEnBaseDatos.getUniqueKey());

                if (!elementoPendienteImportar.equals(elementoEnBaseDatos)) {
                    /// Son diferentes --> ACTUALIZO

                    /// ACTUALIZO el registro en base de datos con el que está pendiente de importar

                    /// Actualizo el registro
                    elementoEnBaseDatos.actualizarCon(elementoPendienteImportar);
                    elementoEnBaseDatos.setLogEntity(logEntity);
                }

                /// Eliminar el elemento procesado del mapa (para evitar eliminarlo más tarde)
                /// mapElementosEnBaseDatos.remove(elementoPendienteImportar.getUniqueKey());

            } else {
                /// NO existe el elemento pendiente en el MAP --> INSERTO

                /// Actualizo el pendiente con LogEntity
                elementoPendienteImportar.setLogEntity(logEntity);

                /// Lo agrego a la lista
                listElementosEnBaseDatos.add(elementoPendienteImportar);
            }
        }
    }
}
