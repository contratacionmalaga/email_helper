package local.jarios.repositorys;

import local.jarios.entity.FicheroGcEntity;
import local.jarios.entity.LogEntity;
import local.jarios.exceptions.MiRepositoryException;
import local.jarios.models.ParseoFicherosGc;
import org.hibernate.Session;

import java.util.List;

/**
 * Description: Importación de Ficheros Excel desde Internet
 * Author: Juan Antonio
 * Date: 04/06/2024
 * Team: Contratacion Electrónica
 */

public interface Repository {


    ///
    void persistir (Session session, LogEntity logEntity, ParseoFicherosGc parseoFicherosGc) throws MiRepositoryException;

    List<FicheroGcEntity> getListFicherosGc(Session session) throws MiRepositoryException;
}
