package local.jarios.mapper;

import local.jarios.entity.*;
import local.jarios.genericode.CodeList;

/**
 * Description: Clase utilizada para la gestión de las entidades
 * Author: Juan Antonio
 * Date: 04/06/2024
 * Team: Juan Antonio
 */

public final class MapperToEntity {

    private MapperToEntity() {}

    public static FicheroGcEntity getFicheroGc (LogEntity logEntity, CodeList codeList) {

        String shortName =
                codeList.getIdentification() != null ?
                        codeList.getIdentification().getShortName() : null;

        String longName =
                codeList.getIdentification() != null ?
                        codeList.getIdentification().getLongName() : null;

        String version =
                codeList.getIdentification() != null ?
                        codeList.getIdentification().getVersion() : null;

        String canonicalUri =
                codeList.getIdentification() != null ?
                        codeList.getIdentification().getCanonicalUri() : null;

        String canonicalVersionUri =
                codeList.getIdentification() != null ?
                        codeList.getIdentification().getCanonicalVersionUri() : null;

        String locationUri =
                codeList.getIdentification() != null ?
                        codeList.getIdentification().getLocationUri() : null;

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
}
