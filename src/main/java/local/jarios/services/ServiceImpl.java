package local.jarios.services;

import local.jarios.database.SessionFactoryProvider;
import local.jarios.entity.FicheroGcEntity;
import local.jarios.entity.LogEntity;
import local.jarios.exceptions.MiRepositoryException;
import local.jarios.exceptions.MiServiceException;
import local.jarios.exceptions.MiSessionFactoryProviderException;
import local.jarios.helpers.ExceptionHelper;
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
     * @param parseoFicherosGc Información con los datos del parseo de los ficheros existentes en el path
     * @throws MiServiceException Excepción en caso de error
     */
    @Override
    public void persistir (LogEntity logEntity, ParseoFicherosGc parseoFicherosGc) throws MiServiceException {

        ///
        Transaction transaction = null;

        /// Declaramos la sesión fuera del try-with-resources
        Session session = null;

        try {

            /// Obtengo el objeto Session
            session = transactionManager.getSession(sessionFactory);

            /// Inicio la transacción dentro de la Session
            transaction = transactionManager.beginTransaction(session);

            ///
            repository.persistir(session, logEntity, parseoFicherosGc);

            /// Finalizar la transacción
            transactionManager.commitTransaction(transaction);

            /// Cierro la sesión
            transactionManager.closeSession(session);

        } catch (MiRepositoryException ex) {

            ///
            if (transaction != null) {

                ///
                transactionManager.rollbackTransaction(transaction);

            }

            /// Registro la excepción
            ExceptionHelper.logException(ex);

            throw new MiServiceException(ex);

        } finally {

            /// Aquí asegúrate de que la sesión se cierre correctamente.
            transactionManager.closeSession(session);
        }
    }

    /**
     *
     * @return Lista de FicherosGc existentes en la base de datos
     * @throws MiServiceException Excepción en caso de error
     */
    @Override
    public List<FicheroGcEntity> getListFicherosGc () throws MiServiceException {

        List<FicheroGcEntity> listFicherosGc;

        try (
                /// Obtengo el objeto Session
                var session = transactionManager.getSession(sessionFactory)
        ){

            ///
            listFicherosGc = repository.getListFicherosGc(session);

        } catch (MiRepositoryException ex) {

            /// Registro la excepción
            ExceptionHelper.logException(ex);

            throw new MiServiceException(ex);

        }

        ///
        return listFicherosGc;
    }
}
