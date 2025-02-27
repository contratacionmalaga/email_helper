package local.jarios.repository;

import jakarta.transaction.Transactional;
import local.jarios.entity.LogEntity;
import local.jarios.exceptions.MiRepositoryException;
import local.jarios.models.DatosFicheroGc;
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
     */
    @Override
    @Transactional
    public void saveLogEntityAndMap(
            Session session,
            LogEntity logEntity,
            Map<String, DatosFicheroGc> datosFicheroGcMap,
            PropertyManager propertyManager) throws MiRepositoryException  {

        try {

            ///
            session.persist(logEntity);
            log.info("Merge del objeto LogEntity");

            ///
            for (Map.Entry<String, DatosFicheroGc> entry : datosFicheroGcMap.entrySet()) {

                ///
                var prefijo = propertyManager.getProperty(PropertyConstantes.CONFIG_PREFIJO);

                ///
                var esquema = propertyManager.getProperty(PropertyConstantes.CONFIG_ESQUEMA);

                ///
                String nombreTabla = prefijo + entry.getKey().toLowerCase();

                String nombreTablaEsquema = esquema + "." + nombreTabla;

                ///
                if (tablaExiste(session, nombreTabla)) {

                    ///
                    var dropSql = "DROP TABLE " + esquema + "." + nombreTabla;

                    ///
                    session.createNativeQuery(dropSql).executeUpdate();
                    log.info("{}Borrada la tabla: {}", ConstantesGenerales.TABULADOR_1, nombreTablaEsquema);

                }

                ///
                crearTabla(session, nombreTablaEsquema);
                log.info("{}Creada la tabla: {}", ConstantesGenerales.TABULADOR_1, nombreTablaEsquema);

                ///
                insertarRegistrosEnTabla(session, nombreTablaEsquema, entry.getValue().getListRegistroGc());
                log.info("Insertados {} registros en la tabla: {}", entry.getValue().getListRegistroGc().size(), nombreTablaEsquema);
            }

        } catch (HibernateException ex) {

            /// Registro la excepción
            log.error(Mensajes.EXCEPTION_ERROR_REPOSITORYIMPL_SAVE, ex.getMessage());

            /// Devuelvo la excepción
            throw new MiRepositoryException(ex);
        }
    }

    /**
     * Verificar si la tabla existe en la base de datos.
     */
    private boolean tablaExiste(Session session, String nombreTabla) {

        /// SQL nativo para verificar la existencia de la tabla
        String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = :nombreTabla";

        ///
        Long count = (Long) session.createNativeQuery(sql)
                .setParameter("nombreTabla", nombreTabla)
                .getSingleResult();

        ///
        return count > 0;
    }

    /**
     * Crear la tabla si no existe.
     */
    private void crearTabla(Session session, String nombreTabla) {

        /// SQL nativo para crear la tabla
        String createTableSql = "CREATE TABLE IF NOT EXISTS " + nombreTabla + " (" +
                "id SERIAL PRIMARY KEY, " +
                "code VARCHAR(50) NOT NULL, " +
                "nombre VARCHAR(500)" +
                ")";

        /// Crear la tabla si no existe
        session.createNativeQuery(createTableSql).executeUpdate();
    }

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
            String code = registro.getCode().replace("'", "''");  // Escapar comillas simples en 'code'
            String nombre = registro.getNombre().replace("'", "''");  // Escapar comillas simples en 'nombre'

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
}
