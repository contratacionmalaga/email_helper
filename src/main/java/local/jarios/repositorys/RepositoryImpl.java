package local.jarios.repositorys;

import local.jarios.entity.FicheroGcEntity;
import local.jarios.entity.LogEntity;
import local.jarios.exceptions.MiRepositoryException;
import local.jarios.helpers.ExceptionHelper;
import local.jarios.interfaces.Actualizable;
import local.jarios.models.ParseoFicherosGc;
import local.jarios.models.RegistroGc;
import local.jarios.properties.PropertyConstantes;
import local.jarios.properties.PropertyManager;
import local.jarios.utils.ConstantesGenerales;
import local.jarios.utils.Mensajes;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;
import java.util.Map;

/**
 * Description: Importación de Ficheros Excel desde Internet
 * Author: Juan Antonio
 * Date: 04/06/2024
 * Team: Contratacion Electrónica
 */
@Slf4j
public class RepositoryImpl implements Repository {

    public RepositoryImpl() {/* CONSTRUCTOR VACÍO */}

    /**
     *
     * @param session Configuración de la sesión actual con la base de datos
     * @param logEntity Identificador de la ejecución del programa
     * @param parseoFicherosGc Objeto que contiene el parseo de los ficheros
     */
    @Override
    public void persistir(
            Session session,
            LogEntity logEntity,
            ParseoFicherosGc parseoFicherosGc) throws MiRepositoryException {

        PropertyManager propertyManager = PropertyManager.getInstance();

        ///
        try {

            ///
            session.persist(logEntity);

            ///
            grabarLista(session, logEntity.getFicherosGcEntity());

            ///
            for (Map.Entry<String, List<RegistroGc>> entry : parseoFicherosGc.getMapRegistrosGcByFicheroGc().entrySet()) {

                ///
                String CONFIG_PREFIJO = propertyManager.getProperty(PropertyConstantes.CONFIG_PREFIJO);
                String nombreTablaSinEsquema = CONFIG_PREFIJO + entry.getKey().toLowerCase();

                String CONFIG_ESQUEMA = propertyManager.getProperty(PropertyConstantes.CONFIG_ESQUEMA);
                String nombreTablaConEsquema = CONFIG_ESQUEMA + "." + nombreTablaSinEsquema;

                ///
                if (tablaExiste(session, nombreTablaSinEsquema)) {

                    ///
                    var dropSql = "DROP TABLE " + nombreTablaConEsquema;

                    ///
                    session.createNativeQuery(dropSql).executeUpdate();
                    log.info(Mensajes.DROP_TABLE, ConstantesGenerales.TABULADOR_1, nombreTablaConEsquema);

                }

                ///
                crearTabla(session, nombreTablaConEsquema);
                log.info(Mensajes.CREATE_TABLE, ConstantesGenerales.TABULADOR_1, nombreTablaConEsquema);

                ///
                insertarRegistrosEnTabla(session, nombreTablaConEsquema, entry.getValue());
                log.info(Mensajes.INSERT_RECORDS, ConstantesGenerales.TABULADOR_2, entry.getValue().size(), nombreTablaConEsquema);

            }

        } catch (HibernateException ex) {

            /// Registro la excepción
            ExceptionHelper.logException(ex);

            /// Devuelvo la excepción
            throw new MiRepositoryException(ex);
        }
    }

    private static <T extends Actualizable<T>> void grabarLista(Session session, List<T> lista) throws HibernateException {

        for (T registro : lista) {
            if (registro.getId() > 0) {
                session.merge(registro);
            } else {
                session.persist(registro);
            }
        }
    }

    /**
     *
     * @param session Sessión establecida con la base de datos
     * @param nombreTablaSinEsquema Nombre de la tabla incluido el esquema
     * @return boolean Indicando si la tabla existe en la base de datos o no
     */
    private boolean tablaExiste(Session session, String nombreTablaSinEsquema) {

        /// SQL nativo para verificar la existencia de la tabla
        String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = :nombreTablaSinEsquema";

        ///
        Long count = (Long) session.createNativeQuery(sql)
                .setParameter("nombreTablaSinEsquema", nombreTablaSinEsquema)
                .getSingleResult();

        ///
        return count > 0;
    }

    /**
     *
     * @param session Sessión establecida con la base de datos
     * @param nombreTablaConEsquema Nombre de la tabla incluido el esquema
     */
    private void crearTabla(Session session, String nombreTablaConEsquema) {

        /// SQL nativo para crear la tabla
        String createTableSql = "CREATE TABLE IF NOT EXISTS " + nombreTablaConEsquema + " (" +
                "id SERIAL PRIMARY KEY, " +
                "code VARCHAR(50) NOT NULL, " +
                "nombre VARCHAR(500)" +
                ")";

        /// Crear la tabla si no existe
        session.createNativeQuery(createTableSql).executeUpdate();
    }

    /**
     *
     * @param session Sessión establecida con la base de datos
     * @param tableName Tabla en la que se realizará la inserción de los datos (Inserción ÚNICA!!!)
     * @param listRegistroGc Lista de registros que se insertarán en una única vez
     */
    private void insertarRegistrosEnTabla(
            Session session,
            String tableName,
            List<RegistroGc> listRegistroGc) {

        /// Usar StringBuilder para construir la consulta de inserción
        StringBuilder insertSql = new StringBuilder("INSERT INTO " + tableName + " (code, nombre) VALUES ");

        /// Crear los valores para insertar
        for (int i = 0; i < listRegistroGc.size(); i++) {
            RegistroGc registro = listRegistroGc.get(i);

            /// Escapar comillas simples en los valores de texto
            String code = registro.getCode().replace("'", "''");      /// Escapar comillas simples en 'code'
            String nombre = registro.getNombre().replace("'", "''");  /// Escapar comillas simples en 'nombre'

            /// Agregar los valores para cada fila
            insertSql.append("(")
                    .append("'").append(code).append("'").append(", ")
                    .append("'").append(nombre).append("'").append(")");

            /// Agregar una coma si no es el último registro
            if (i < listRegistroGc.size() - 1) {
                insertSql.append(", ");
            }
        }

        /// Ejecutar la consulta
        session.createNativeQuery(insertSql.toString()).executeUpdate();
    }

    /**
     * Devuelve la lista de FicherosGc existente en la base de datos
     * @param session Sessión establecida con la base de datos
     * @return Lista de FicherosGc desde la base de datos
     * @throws MiRepositoryException Excepción en caso de error
     */
    public List<FicheroGcEntity> getListFicherosGc(Session session) throws MiRepositoryException {

        ///
        String jpql = "SELECT f FROM FicheroGcEntity f";

        try {

            return session.createQuery(jpql, FicheroGcEntity.class).getResultList();

        } catch (HibernateException ex) {

            /// Registro la excepción
            ExceptionHelper.logException(ex);

            /// Devuelvo la excepción
            throw new MiRepositoryException(ex);
        }
    }
}
