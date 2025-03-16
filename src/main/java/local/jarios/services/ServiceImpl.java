package local.jarios.services;

import local.jarios.database.SessionFactoryProvider;
import local.jarios.entity.Estadistica;
import local.jarios.entity.FicheroGc;
import local.jarios.entity.Log;
import local.jarios.exceptions.MiSessionFactoryProviderException;
import local.jarios.models.ParseoFicherosGc;
import local.jarios.properties.PropertyManager;
import local.jarios.repositorys.Repository;
import local.jarios.repositorys.RepositoryImpl;
import local.jarios.repositorys.TransactionManager;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Description: LogEntity
 * Author: Juan Antonio
 * Date: 04/06/2024
 * Team: Juan Antonio
 */

@Slf4j
public class ServiceImpl implements Service {

    private final Repository repository;
    private final TransactionManager transactionManager;
    private final SessionFactory sessionFactory;

    /**
     *
     * @param propertyManager Objeto con las propiedades establecidas en los ficheros
     * @throws MiSessionFactoryProviderException Excepción en caso de error
     */
    public ServiceImpl(PropertyManager propertyManager) throws MiSessionFactoryProviderException {

        this.repository = new RepositoryImpl();
        this.transactionManager = new TransactionManager();
        var sessionFactoryProvider = new SessionFactoryProvider();
        this.sessionFactory = sessionFactoryProvider.getSessionFactory(propertyManager.getHibernateProperties());
    }

    /**
     *
     * @param logEntity Objeto Logentity que será persistido
     */
    @Override
    public void persistir (Log logEntity)  {

        /// Obtengo el objeto Session
        Session session = transactionManager.getSession(sessionFactory);

        /// Inicio la transacción dentro de la Session
        Transaction transaction = transactionManager.beginTransaction(session);

        ///
        repository.persistir(session, transaction, logEntity);

        /// Finalizar la transacción
        transactionManager.commitTransaction(transaction);

        /// Cierro la sesión
        transactionManager.closeSession(session);

        /// Aquí asegúrate de que la sesión se cierre correctamente.
        transactionManager.closeSession(session);
    }

    /**
     *
     * @param listFicherosGc Objeto Logentity que será persistido
     */
    @Override
    public void persistir (List<FicheroGc> listFicherosGc)  {

        /// Obtengo el objeto Session
        Session session = transactionManager.getSession(sessionFactory);

        /// Inicio la transacción dentro de la Session
        Transaction transaction = transactionManager.beginTransaction(session);

        ///
        repository.persistir(session, transaction, listFicherosGc);

        /// Finalizar la transacción
        transactionManager.commitTransaction(transaction);

        /// Cierro la sesión
        transactionManager.closeSession(session);

        /// Aquí asegúrate de que la sesión se cierre correctamente.
        transactionManager.closeSession(session);
    }

    /**
     *
     * @param parseoFicherosGc Información con los datos del parseo de los ficheros existentes en el path
     */
    @Override
    public void persistir (ParseoFicherosGc parseoFicherosGc)  {

        /// Obtengo el objeto Session
        Session session = transactionManager.getSession(sessionFactory);

        /// Inicio la transacción dentro de la Session
        Transaction transaction = transactionManager.beginTransaction(session);

        ///
        repository.persistir(session, transaction, parseoFicherosGc);

        /// Finalizar la transacción
        transactionManager.commitTransaction(transaction);

        /// Cierro la sesión
        transactionManager.closeSession(session);

        /// Aquí asegúrate de que la sesión se cierre correctamente.
        transactionManager.closeSession(session);

    }

    /**
     *
     * @param estadistica Objeto Logentity que será persistido
     */
    @Override
    public void persistir (Estadistica estadistica)  {

        /// Obtengo el objeto Session
        Session session = transactionManager.getSession(sessionFactory);

        /// Inicio la transacción dentro de la Session
        Transaction transaction = transactionManager.beginTransaction(session);

        ///
        repository.persistir(session, transaction, estadistica);

        /// Finalizar la transacción
        transactionManager.commitTransaction(transaction);

        /// Cierro la sesión
        transactionManager.closeSession(session);

        /// Aquí asegúrate de que la sesión se cierre correctamente.
        transactionManager.closeSession(session);
    }

    /**
     *
     * @return Lista de FicherosGc existentes en la base de datos
     */
    @Override
    public List<FicheroGc> getListFicherosGc () {

        /// Obtengo el objeto Session
        Session session = transactionManager.getSession(sessionFactory);

        List<FicheroGc> listFicherosGc = repository.getListFicherosGc(session);

        /// Aquí asegúrate de que la sesión se cierre correctamente.
        transactionManager.closeSession(session);

        ///
        return listFicherosGc;
    }
}
