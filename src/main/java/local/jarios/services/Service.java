package local.jarios.services;

import local.jarios.entity.Estadistica;
import local.jarios.entity.FicheroGc;
import local.jarios.entity.Log;
import local.jarios.models.ParseoFicherosGc;

import java.util.List;

/**
 * Description: Importación de Ficheros Excel desde Internet
 * Author: Juan Antonio
 * Date: 04/06/2024
 * Team: Contratacion Electrónica
 */

public interface Service {

    void persistir(Log miLog);

    void persistir(List<FicheroGc> listFicherosGc);

    void persistir(ParseoFicherosGc parseoFicherosGc);

    void persistir(Estadistica estadistica);

    List<FicheroGc> getListFicherosGc();
}
