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

    private static String CONFIG_PREFIJO = "";

    private static String CONFIG_ESQUEMA = "";

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

        ///
        cargarVariables(propertyManager);

        ///
        try {

            ///
            session.persist(logEntity);
            log.info("Merge del objeto LogEntity");

            ///
            for (Map.Entry<String, DatosFicheroGc> entry : datosFicheroGcMap.entrySet()) {

                ///
                String nombreTablaSinEsquema = CONFIG_PREFIJO + entry.getKey().toLowerCase();

                String nombreTablaConEsquema = CONFIG_ESQUEMA + "." + nombreTablaSinEsquema;

                ///
                if (tablaExiste(session, nombreTablaSinEsquema)) {

                    ///
                    var dropSql = "DROP TABLE " + nombreTablaConEsquema;

                    ///
                    session.createNativeQuery(dropSql).executeUpdate();
                    log.info("{}Borrada la tabla: {}", ConstantesGenerales.TABULADOR_1, nombreTablaConEsquema);

                }

                ///
                crearTabla(session, nombreTablaConEsquema);
                log.info("{}Creada la tabla: {}", ConstantesGenerales.TABULADOR_1, nombreTablaConEsquema);

                ///
                insertarRegistrosEnTabla(session, nombreTablaConEsquema, entry.getValue().getListRegistroGc());
                log.info("Insertados {} registros en la tabla: {}", entry.getValue().getListRegistroGc().size(), nombreTablaConEsquema);
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
     * Crear la tabla si no existe.
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

    private static void cargarVariables(PropertyManager propertyManager) {

        ///
        CONFIG_PREFIJO = propertyManager.getProperty(PropertyConstantes.CONFIG_PREFIJO);

        ///
        CONFIG_ESQUEMA = propertyManager.getProperty(PropertyConstantes.CONFIG_ESQUEMA);
    }
}
