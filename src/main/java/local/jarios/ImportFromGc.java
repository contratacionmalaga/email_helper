package local.jarios;

import local.jarios.entity.Estadistica;
import local.jarios.entity.FicheroGc;
import local.jarios.entity.Log;
import local.jarios.enums.TipoFinalEjecucion;
import local.jarios.exceptions.MiMailException;
import local.jarios.exceptions.MiSessionFactoryProviderException;
import local.jarios.exceptions.MiUnknownHostException;
import local.jarios.helpers.ComunHelper;
import local.jarios.helpers.FileHelper;
import local.jarios.helpers.ListHelper;
import local.jarios.models.MiMail;
import local.jarios.models.ParseoFicherosGc;
import local.jarios.properties.PropertyConstantes;
import local.jarios.properties.PropertyManager;
import local.jarios.services.Service;
import local.jarios.services.ServiceImpl;
import local.jarios.utils.ConstantesGenerales;
import local.jarios.utils.FinalDelPrograma;
import local.jarios.utils.Mensajes;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Description: Importación de la información de diferentes ficheros de Excel a una base de datos PostgreSQL para
 * su posterior explotación.
 * Author: Juan Antonio
 * Date: 04/06/2024
 * Team: Juan Antonio
 */

@Slf4j
public class ImportFromGc {

    public static void main(String[] args) {

        ///
        try {

            ///
            log.info(Mensajes.INICIO);

            /// Cargo los ficheros properties utilizando el patrón SINGLETON
            PropertyManager propertyManager = PropertyManager.getInstance();
            log.info(Mensajes.PROPERTY_LOG);

            /// Imprimo el contenido de los ficheros asociados a la configuración Local
            if (log.isDebugEnabled()) {
                propertyManager.imprimirMapProperties();
            }

            /// Imprimo la versión del aplicativo desde el fichero de release
            log.info(Mensajes.VERSION_APP, propertyManager.getProperty(PropertyConstantes.SCM_TAG));

            /// ***** CREO EL OBJETO LogEntity
            Log miLog = new Log();
            log.info(Mensajes.LOG_CREACION, miLog);

            /// Creo el objeto EstadisticaEntity que se inicializa con el LogEntity anteriormente creado y con la
            ///         el valor Timestamp.valueOf(LocalDateTime.now()) para el campo fechaHoraInicial
            var estadistica = new Estadistica(miLog);
            log.info(Mensajes.ESTADISTICA_CREACION);

            ///
            ///     CREO LA INSTANCIA DEL SERVICIO ENCARGADO DE INTERACTUAR CO LA BASE DE DATOS
            ///
            log.info(Mensajes.SERVICE_CREACION_INICIO);
            Service service = new ServiceImpl(propertyManager);
            log.info(Mensajes.SERVICE_CREACION_CREADO);

            ///
            ///     INICIO DEL PARSEO DE LOS FICHEROS GC
            ///

            /// Establecer fecha de inicio en el objeto Estadistica
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
            estadistica.setFechaHoraInicialParseo(timestamp);
            log.info(Mensajes.ASIGN_FECHA_HORA_INICIAL_PARSEO_TO_ESTADISTICA, timestamp);

            /// 1. OBTENGO LA LISTA DE ficherosGc EXISTENTES EN LA BASE DE DATOS
            List<FicheroGc> listFicherosGcEnBaseDatos = service.getListFicherosGc();

            /// 2- OBTENGO LA RUTA DE LOS FICHERO A PARSEAR
            var path = propertyManager.getProperty(PropertyConstantes.CONFIG_PATH);
            log.info(Mensajes.RUTA_FICHEROS, path);

            /// 3- LEO TODOS LOS FICHEROS DESDE LA RUTA
            File[] arrayFiles = FileHelper.getListaFicherosFromPath(path);

            /// 4.- ALMACENO EN ESTADISTICAS EL NÚMERO DE FICHEROS EXISTENTES EN LA RUTA
            var nFilesLeidos = arrayFiles.length;
            log.info (Mensajes.NUEMRO_FICHEROS_LEIDOS, nFilesLeidos, path);
            estadistica.setNTotalFicherosLeidos(nFilesLeidos);

            /// Para no gestionar listas nulas, creo la lista que se rellenará si el número de ficheros es mayor que 0
            ParseoFicherosGc parseoFicherosGc = new ParseoFicherosGc();

            /// Unicamente proceso la lista de ficheros si el número de ficheros que contiene el array es mayor que 0
            if (nFilesLeidos > 0) {

                /// Proceso la lista con los ficheros
                parseoFicherosGc = FileHelper.procesarListaFicherosFromPath(miLog, arrayFiles);
            }

            ///
            ///     UNIFICO LAS LISTAS (la existente en base de datos y la que está pendiente de importar)
            ///
            ListHelper.unificarListas(miLog, listFicherosGcEnBaseDatos, parseoFicherosGc.getListFicherosGc());

            /// ASIGNO LA LISTA DE FICHEROS (unificada) AL OBJETO logEntity
            miLog.setFicherosGc(listFicherosGcEnBaseDatos);

            ///
            ///     OBTENGO LAS ESTADÍSTICAS DE DURACIÓN DEL PARSEO
            ///

            timestamp = Timestamp.valueOf(LocalDateTime.now());
            estadistica.setFechaHoraInicialParseo(timestamp);
            log.info(Mensajes.ASIGN_FECHA_HORA_FINAL_PARSEO_TO_ESTADISTICA, timestamp);

            /// Número de ficheros procesados
            int nFicherosProcesados = parseoFicherosGc.getListFicherosGc().size();
            estadistica.setNTotalFicherosProcesados(nFicherosProcesados);
            log.info (Mensajes.NUEMRO_FICHEROS_PROCESADOS, nFicherosProcesados, path);

            /// Número total de RegistrosGc
            int nRegistrosGc = parseoFicherosGc.getMapRegistrosGcByFicheroGc().values().stream()
                    .mapToInt(List::size)  /// Convierte cada lista en su tamaño
                    .sum();                /// Suma los tamaños de todas las listas
            estadistica.setNRregistrosGc(nRegistrosGc);
            log.info (Mensajes.NUEMRO_REGISTROS_GC, nRegistrosGc);

            /// Establezco la fecha y hora final de la ejeucicón
            estadistica.setFechaHoraFinalParseo(new Timestamp(System.currentTimeMillis()));

            /// Calculo el tiempo de ejecución con el formato deseado
            String duracion = ComunHelper.calcularTiempoEjecucion(
                    estadistica.getFechaHoraInicialParseo(),
                    estadistica.getFechaHoraFinalParseo());

            /// Asigno la duración al objeto EstadisticaEntity
            estadistica.setDuracionParseo(duracion);

            ///
            ///     FINAL DEL PARSEO DE LOS FICHEROS GC
            ///

            ///
            ///     INICIO PERSISTENCIA EN LA BASE DE DATOS DE LOS OBJETOS
            ///

            /// Establecer fecha de inicio de la persistencia en base de datos
            timestamp = Timestamp.valueOf(LocalDateTime.now());
            estadistica.setFechaHoraInicialBaseDatos(timestamp);
            log.info(Mensajes.ASIGN_FECHA_HORA_INICIAL_BASE_DATOS_TO_ESTADISTICA, timestamp);

            /// Persisto el objeto Log
            service.persistir(miLog);
            log.info(Mensajes.PERSISTIDO_LOG);

            /// Persisto el objeto List<FicherosGc>
            service.persistir(listFicherosGcEnBaseDatos);
            log.info(Mensajes.PERSISTIDO_FICHEROS_GC);

            /// Persisto el objeto ParseoFicherosGc
            service.persistir(parseoFicherosGc);
            log.info(Mensajes.PERSISTIDO_PARSEO_FICHEROS_GC);

            /// Establecer fecha final de la persistencia en base de datos
            timestamp = Timestamp.valueOf(LocalDateTime.now());
            estadistica.setFechaHoraFinalBaseDatos(timestamp);
            log.info(Mensajes.ASIGN_FECHA_HORA_FINAL_BASE_DATOS_TO_ESTADISTICA, timestamp);

            ///
            ///     OBTENGO LAS ESTADÍSTICAS DE DURACIÓN
            ///

            /// Calculo el tiempo de ejecución con el formato deseado
            duracion = ComunHelper.calcularTiempoEjecucion(
                    estadistica.getFechaHoraInicialBaseDatos(),
                    estadistica.getFechaHoraFinalBaseDatos());

            /// Asigno la duración al objeto Estadistica
            estadistica.setDuracionBaseDatos(duracion);
            log.info(Mensajes.ASIGN_DURACION_BASE_DATOS, duracion);

            /// Persisto las estadisticas en la base de datos
            service.persistir(estadistica);
            log.info(Mensajes.PERSISTIDO_ESTADISTICA);

            ///
            ///     FINAL PERSISTENCIA EN LA BASE DE DATOS DE LOS OBJETOS
            ///

            /// Creación del objeto Mail a partir de las Estadísticas para el envío de la información
            MiMail miMail = new MiMail(propertyManager, estadistica);
            log.info(Mensajes.MAIL_CREACION);

            /// Envío un correo con la información de la ejecución del aplicativo
            miMail.enviarEmail();
            log.info(Mensajes.MAIL_ENVIADO);

            /// Finalizar el programa correctamente
            FinalDelPrograma.finalizar(TipoFinalEjecucion.CORRECTO);

        } catch (MiMailException | MiUnknownHostException | MiSessionFactoryProviderException ex) {

            /// Registro la excepción
            log.error(Mensajes.EXCEPTION);
            log.error(Mensajes.EXCEPTION_MENSAJE, ConstantesGenerales.TABULADOR_1, ex.getMessage());
            log.error(Mensajes.EXCEPTION_STACK_TRACE, ConstantesGenerales.TABULADOR_1);

            ///
            for (StackTraceElement stackTraceElement : ex.getStackTrace()) {
                log.error("{}{}", ConstantesGenerales.TABULADOR_2, stackTraceElement.toString());
            }

            /// Finalizo la ejecución del programa
            FinalDelPrograma.finalizar(TipoFinalEjecucion.ERROR);

        }
    }
}
