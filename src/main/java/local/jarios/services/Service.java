package local.jarios.services;

import local.jarios.entity.FicheroGcEntity;
import local.jarios.entity.LogEntity;
import local.jarios.exceptions.MiServiceException;
import local.jarios.models.ParseoFicherosGc;

import java.util.List;

/**
 * Description: Importación de Ficheros Excel desde Internet
 * Author: Juan Antonio
 * Date: 04/06/2024
 * Team: Contratacion Electrónica
 */

public interface Service {

    void persistir(LogEntity logEntity, ParseoFicherosGc parseoFicherosGc) throws MiServiceException;

    List<FicheroGcEntity> getListFicherosGc() throws MiServiceException;
}
