package local.jarios.helpers;

import local.jarios.entity.FicheroGcEntity;
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

    public static void unificarListasFicherosGc(
            List<FicheroGcEntity> listFicherosGcEnBaseDatos,
            List<FicheroGcEntity> listFicherosGcPendientesImportar) {

        /// Crear un mapa para los ficheros en la base de datos, donde la clave es el id
        Map<String, FicheroGcEntity> mapFicherosEnBaseDatos = new HashMap<>();
        for (FicheroGcEntity ficheroEnBase : listFicherosGcEnBaseDatos) {
            mapFicherosEnBaseDatos.put(ficheroEnBase.getCanonicalUri(), ficheroEnBase);
        }

        /// Iterar sobre los elementos de la lista pendientes de importar
        for (FicheroGcEntity ficheroPendiente : listFicherosGcPendientesImportar) {
            FicheroGcEntity ficheroEnBase = mapFicherosEnBaseDatos.get(ficheroPendiente.getCanonicalUri());

            if (ficheroEnBase != null) {
                /// Si son diferentes, actualizar los valores del fichero en la base de datos
                if (!ficheroPendiente.equals(ficheroEnBase)) {
                    ficheroEnBase.setShortName(ficheroPendiente.getShortName());
                    ficheroEnBase.setLongName(ficheroPendiente.getLongName());
                    ficheroEnBase.setVersion(ficheroPendiente.getVersion());
                    ficheroEnBase.setCanonicalUri(ficheroPendiente.getCanonicalUri());
                    ficheroEnBase.setCanonicalVersionUri(ficheroPendiente.getCanonicalVersionUri());
                    ficheroEnBase.setLocationUri(ficheroPendiente.getLocationUri());
                }
                /// Eliminar el fichero procesado del mapa (para evitar eliminarlo más tarde)
                mapFicherosEnBaseDatos.remove(ficheroPendiente.getCanonicalUri());
            } else {
                /// Si el fichero no existe, agregarlo
                listFicherosGcEnBaseDatos.add(ficheroPendiente);
            }
        }

        /// Eliminar los elementos de la base de datos que no están en la lista de pendientes de importar
        // listFicherosGcEnBaseDatos.removeIf(ficheroEnBase -> !mapFicherosEnBaseDatos.containsKey(ficheroEnBase.getCanonicalUri()));
    }

    public static <T extends Actualizable<T>> void unificarListasGenerico(
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
        listElementosEnBaseDatos.removeIf(
                elementoEnBase -> !mapElementosEnBaseDatos.containsKey(elementoEnBase.getUniqueKey()));
    }
}
