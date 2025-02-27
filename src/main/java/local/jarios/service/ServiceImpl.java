package local.jarios.service;

import local.jarios.database.DatabaseConfig;
import local.jarios.database.SessionFactoryProvider;
import local.jarios.entity.LogEntity;
import local.jarios.exceptions.MiRepositoryException;
import local.jarios.exceptions.MiServiceException;
import local.jarios.exceptions.MiSessionFactoryProviderException;
import local.jarios.models.DatosFicheroGc;
import local.jarios.properties.PropertyManager;
import local.jarios.repository.Repository;
import local.jarios.repository.RepositoryImpl;
import local.jarios.repository.TransactionManager;
import local.jarios.utils.Mensajes;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Map;


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

    public ServiceImpl(
            DatabaseConfig databaseConfig,
            PropertyManager propertyManager) throws MiSessionFactoryProviderException {

        this.repository = new RepositoryImpl();
        this.transactionManager = new TransactionManager();
        var sessionFactoryProvider = new SessionFactoryProvider();
        this.sessionFactory = sessionFactoryProvider.getSessionFactory(databaseConfig, propertyManager);
    }

    @Override
    public void saveLogEntityAndMap (
            LogEntity logEntity,
            Map<String, DatosFicheroGc> datosFicheroGcMap,
            PropertyManager propertyManager) throws MiServiceException {

        ///
        Transaction transaction = null;

        try (
                /// Obtengo el objeto Session
                var session = transactionManager.getSession(sessionFactory)
        ){

            /// Inicio la transacción dentro de la Session
            transaction = transactionManager.beginTransaction(session);

            ///
            repository.saveLogEntityAndMap(session, logEntity, datosFicheroGcMap, propertyManager);

            /// Finalizar la transacción
            transactionManager.commitTransaction(transaction);


        } catch (MiRepositoryException ex) {

            ///
            if (transaction != null) {

                ///
                transactionManager.rollbackTransaction(transaction);

            }

            /// Registro la excepción
            log.error(Mensajes.EXCEPTION_ERROR_SERVICEIMPL_SAVE, ex.getMessage());

            throw new MiServiceException(ex);
        }
    }
}
