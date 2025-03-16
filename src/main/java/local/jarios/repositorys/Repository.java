package local.jarios.repositorys;

import local.jarios.entity.Estadistica;
import local.jarios.entity.FicheroGc;
import local.jarios.entity.Log;
import local.jarios.models.ParseoFicherosGc;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Description: Importación de Ficheros Excel desde Internet
 * Author: Juan Antonio
 * Date: 04/06/2024
 * Team: Contratacion Electrónica
 */

public interface Repository {


    void persistir (Session session, Transaction transaction, Log miLog);

    void persistir (Session session, Transaction transaction, List<FicheroGc> listFicherosGc);

    void persistir (Session session, Transaction transaction, Estadistica estadistica);
    ///
    void persistir (Session session, Transaction transaction, ParseoFicherosGc parseoFicherosGc);

    List<FicheroGc> getListFicherosGc(Session session);
}
