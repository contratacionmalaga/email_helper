package local.jarios.service;

import local.jarios.entity.*;
import local.jarios.exceptions.MiServiceException;
import local.jarios.models.DatosFicheroGc;
import local.jarios.properties.PropertyManager;

import java.util.Map;

/**
 * Description: Importación de Ficheros Excel desde Internet
 * Author: Juan Antonio
 * Date: 04/06/2024
 * Team: Contratacion Electrónica
 */

public interface Service {

    void saveLogEntityAndMap (
            LogEntity logEntity,
            Map<String, DatosFicheroGc> datosFicheroGcMap,
            PropertyManager propertyManager) throws MiServiceException;
}
