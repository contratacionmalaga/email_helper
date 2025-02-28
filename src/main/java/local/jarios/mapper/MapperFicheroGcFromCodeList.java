package local.jarios.mapper;

import local.jarios.entity.FicheroGcEntity;
import local.jarios.entity.LogEntity;
import local.jarios.genericode.CodeList;
import local.jarios.genericode.Identification;
import local.jarios.utils.ConstantesGenerales;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * Description: Clase utilizada para la gestión de las entidades
 * Author: Juan Antonio
 * Date: 04/06/2024
 * Team: Juan Antonio
 */

@Slf4j
public final class MapperFicheroGcFromCodeList {

    /**
     * CONSTRUCTOR PRIVADO DE LA CLASE PUESTO QUE ESTA FINAL
     */
    private MapperFicheroGcFromCodeList() {}

    public static FicheroGcEntity getFicheroGcFromCodeList(LogEntity logEntity, CodeList codeList) {
        /// Obtener la identificación del código, si es null devolver null
        var identification = codeList.getIdentification();
        if (identification == null) {
            return null;  /// Si identification es null, no se puede crear el FicheroGcEntity
        }

        /// Usar getOrEmpty para obtener valores, garantizando que no sean nulos, sino vacíos
        String shortName = getOrEmpty(identification, Identification::getShortName);
        String longName = getOrEmpty(identification, Identification::getLongName);
        String version = getOrEmpty(identification, Identification::getVersion);
        String canonicalUri = getOrEmpty(identification, Identification::getCanonicalUri);
        String canonicalVersionUri = getOrEmpty(identification, Identification::getCanonicalVersionUri);
        String locationUri = getOrEmpty(identification, Identification::getLocationUri);

        /// Crear y poblar la entidad FicheroGcEntity
        var ficheroGcEntity = new FicheroGcEntity();
        ficheroGcEntity.setLogEntity(logEntity);
        ficheroGcEntity.setShortName(shortName);
        ficheroGcEntity.setLongName(longName);
        ficheroGcEntity.setVersion(version);
        ficheroGcEntity.setCanonicalUri(canonicalUri);
        ficheroGcEntity.setCanonicalVersionUri(canonicalVersionUri);
        ficheroGcEntity.setLocationUri(locationUri);

        return ficheroGcEntity;
    }

    /// Método de utilidad para evitar repetición de código y garantizar que los valores no sean null
    private static String getOrEmpty(Identification identification, Function<Identification, String> getter) {
        String value = getter.apply(identification);
        return value != null ? value : ConstantesGenerales.CADENA_VACIA;  /// Nunca devolver null, siempre cadena vacía
    }
}
