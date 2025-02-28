package local.jarios;

import local.jarios.entity.*;
import local.jarios.exceptions.MiMailException;
import local.jarios.exceptions.MiManejadorDeExcepciones;
import local.jarios.exceptions.MiPropertyFileException;
import local.jarios.exceptions.MiServiceException;
import local.jarios.exceptions.MiSessionFactoryProviderException;
import local.jarios.models.DatosFicheroGc;
import local.jarios.models.MiMail;
import local.jarios.enums.TipoFinalEjecucion;
import local.jarios.helpers.*;
import local.jarios.mapper.MapperToEntity;
import local.jarios.properties.PropertyConstantes;
import local.jarios.properties.PropertyManager;
import local.jarios.service.Service;
import local.jarios.service.ServiceImpl;
import local.jarios.utils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: Importación de la información de diferentes ficheros de Excel a una base de datos PostgreSQL para
 * su posterior explotación.
 * Author: Juan Antonio
 * Date: 04/06/2024
 * Team: Juan Antonio
 */

public class ImportFromGc {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportFromGc.class);

    public static void main(String[] args) {


        ///
        try {

            LOGGER.info (Mensajes.PROPERTY_LOG);

            /// Cargo los ficheros properties utilizando el patrón SINGLETON
            var propertyManager = PropertyManager.getInstance();

            /// Imprimo el contenido de los ficheros asociados a la configuración Local
            propertyManager.imprimirMapProperties();

            /// ***** CREO EL OBJETO LogEntity
            var logEntity = new LogEntity();

            /// Creo el objeto EstadisticaEntity que se inicializa con el LogEntity anteriormente creado y con la
            ///         el valor Timestamp.valueOf(LocalDateTime.now()) para el campo fechaHoraInicial
            var estadisticaEntity = new EstadisticaEntity(logEntity);

            ///
            ///     INICIO DE LA IMPORTACIÓN DE LOS FICHEROS GC
            ///

            /// 1- OBTENGO LA RUTA DE LOS FICHEROS A PARSEAR
            var path = propertyManager.getProperty(PropertyConstantes.CONFIG_PATH);
            LOGGER.info(Mensajes.RUTA_FICHEROS, path);

            /// 2- LEO TODOS LOS FICHEROS DESDE LA RUTA
            File[] listaFicherosGC = FileHelper.getListaFicherosFromPath(path);
            LOGGER.info (Mensajes.LECTURA_FICHEROS, listaFicherosGC.length, path);

            /// Defino la lista que se asignará al objeto LogEntity
            List<FicheroGcEntity> listFicherosGcEntity = new ArrayList<>();

            /// Defino el Map que se utilizará para almacenar la información parseada
            Map<String, DatosFicheroGc> mapDatosFicherosGc = new HashMap<>();

            /// 3- RECORRO LA LISTA PARA PARSEAR LOS FICHEROS
            for (File file : listaFicherosGC) {

                /// Aumento el número de ficheros que son procesados
                estadisticaEntity.aumentarNumFicheros();

                /// 3.1- COMPRUEBO QUE EL FICHERO ES CORRECTO
                var ficheroCorrecto = file.exists() && file.canRead() && file.exists();
                LOGGER.info(Mensajes.VALIDEZ_FICHERO, ConstantesGenerales.TABULADOR_1, file, ficheroCorrecto);

                if (ficheroCorrecto) {
                    /// FICHERO CORRECTO

                    /// 3.1.1 - OBTENGO EL OBJETO CODELIST A PARTIR DEL FICHERO
                    var codeList = CodeListHelper.getCodeListFromFile(file);

                    /// 3.1.2 - OBTENGO EL OBJETO FICHEROGCENTITY
                    var ficheroGcEntity = MapperToEntity.getFicheroGc(logEntity, codeList);

                    /// 3.1.3 - ASIGNO EL logEntity AL FICHERO
                    ficheroGcEntity.setLogEntity(logEntity);

                    /// 3.1.4 - Asigno el objeto a la lista que posteriormente será añadida al LogEntity
                    listFicherosGcEntity.add(ficheroGcEntity);

                    /// 3.1.5 - CREO EL OBJETO DatosFicherosGc y le asigno los datos del Fichero que estoy procesando
                    var datosFicheroGc = new DatosFicheroGc();
                    datosFicheroGc.setFicheroGcEntity(ficheroGcEntity);
                    datosFicheroGc.setListRegistroGc(RenameGcHelper.getListTablaGcEntity(codeList));

                    ///
                    estadisticaEntity.aumentarNumRegistrosGc(RenameGcHelper.getListTablaGcEntity(codeList).size());

                    /// 3.1.6 - AÑADO AL MAP LA LISTA DE REGISTROS GC ASOCIADOS A LA ENTIDAD
                    mapDatosFicherosGc.put(ficheroGcEntity.getShortName(), datosFicheroGc);

                } else {

                    LOGGER.info(Mensajes.MENSAJE_FICHERO_NO_EXISTE, file.getName());

                }
            }

            /// ASIGNO LA LISTA DE FICHEROS AL OBJETO logEntity
            logEntity.setFicherosGcEntity(listFicherosGcEntity);

            ///
            ///     ESTABLEZCO LA FECHA Y HORA FINAL DE LA IMPORTACIÓN
            ///
            estadisticaEntity.setFechaHoraFinal(new Timestamp(System.currentTimeMillis()));
            estadisticaEntity.calcularTiempoEjecucion();

            /// Asigno las estadísticas al objeto LogEntity
            logEntity.setEstadisticaEntity(estadisticaEntity);

            ///
            ///     CREACIÓN DEL SERVICIO QUE SE ENCARGARÁ DE PERSISTIR LOS VALORES EN LA BASE DE DATOS
            ///
            Service service = new ServiceImpl(propertyManager);

            /// Método encargado de persistir la información en la base de datos
            service.saveLogEntityAndMap(logEntity, mapDatosFicherosGc, propertyManager);

            /// Envío un correo con la información de la ejecución del aplicativo
            var miMail = new MiMail(propertyManager, estadisticaEntity);
            miMail.enviarEmail();

            /// Imprimir resumen
            LOGGER.info(Mensajes.RESUMEN_EJECUCION);
            /// ComunHelper.imprimir(logEntity);

            /// Finalizar el programa correctamente
            FinalDelPrograma.finalizar(TipoFinalEjecucion.CORRECTO, ConstantesGenerales.CADENA_VACIA);

        } catch (MiMailException | MiServiceException | MiSessionFactoryProviderException | MiPropertyFileException ex) {

            /// Muestro en el log la información de la excepción
            MiManejadorDeExcepciones.exceptionToLog(ex.getMessage(), ex.getStackTrace());

            /// Finalizo la ejecución del programa
            FinalDelPrograma.finalizar(TipoFinalEjecucion.ERROR, ex.getMessage());

        }
    }
}
