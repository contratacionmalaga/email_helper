package local.jarios;

import local.jarios.entity.EstadisticaEntity;
import local.jarios.entity.FicheroGcEntity;
import local.jarios.entity.LogEntity;
import local.jarios.enums.TipoFinalEjecucion;
import local.jarios.exceptions.MiMailException;
import local.jarios.exceptions.MiServiceException;
import local.jarios.exceptions.MiSessionFactoryProviderException;
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

            log.info (Mensajes.PROPERTY_LOG);

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
            ///     CREO LA INSTANCIA DEL SERVICIO ENCARGADO DE INTERACTUAR CO LA BASE DE DATOS
            ///
            Service service = new ServiceImpl(propertyManager);

            /// 1. OBTENGO LA LISTA DE ficherosGc EXISTENTES EN LA BASE DE DATOS
            List<FicheroGcEntity> listFicherosGcEnBaseDatos = service.getListFicherosGc();

            /// 2- OBTENGO LA RUTA DE LOS FICHERO A PARSEAR
            var path = propertyManager.getProperty(PropertyConstantes.CONFIG_PATH);
            log.info(Mensajes.RUTA_FICHEROS, path);

            /// 3- LEO TODOS LOS FICHEROS DESDE LA RUTA
            File[] arrayFiles = FileHelper.getListaFicherosFromPath(path);

            /// 4.- ALMACENO EN ESTADISTICAS EL NÚMERO DE FICHEROS EXISTENTES EN LA RUTA
            var nFilesLeidos = arrayFiles.length;
            log.info (Mensajes.NUEMRO_FICHEROS_LEIDOS, nFilesLeidos, path);
            estadisticaEntity.setNTotalFicherosLeidos(nFilesLeidos);

            /// Para no gestionar listas nulas, creo la lista que se rellenará si el número de ficheros es mayor que 0
            ParseoFicherosGc parseoFicherosGc = new ParseoFicherosGc();

            /// Unicamente proceso la lista de ficheros si el número de ficheros que contiene el array es mayor que 0
            if (nFilesLeidos > 0) {

                /// Proceso la lista con los ficheros
                parseoFicherosGc = FileHelper.procesarListaFicherosFromPath(logEntity, arrayFiles);
            }

            ///
            ///     UNIFICO LAS LISTAS (la existente en base de datos y la que está pendiente de importar)
            ///
            ListHelper.unificarListas(logEntity, listFicherosGcEnBaseDatos, parseoFicherosGc.getListFicherosGc());

            /// ASIGNO LA LISTA DE FICHEROS (unificada) AL OBJETO logEntity
            logEntity.setFicherosGcEntity(listFicherosGcEnBaseDatos);

            ///
            ///     ESTABLEZCO ESTADÍSTICAS
            ///

            /// Número de ficheros procesados
            int nFicherosProcesados = parseoFicherosGc.getListFicherosGc().size();
            estadisticaEntity.setNTotalFicherosProcesados(nFicherosProcesados);
            log.info (Mensajes.NUEMRO_FICHEROS_PROCESADOS, nFicherosProcesados, path);

            /// Número total de RegistrosGc
            int nRegistrosGc = parseoFicherosGc.getMapRegistrosGcByFicheroGc().values().stream()
                    .mapToInt(List::size)  /// Convierte cada lista en su tamaño
                    .sum();                /// Suma los tamaños de todas las listas
            estadisticaEntity.setNRregistrosGc(nRegistrosGc);
            log.info (Mensajes.NUEMRO_REGISTROS_GC, nRegistrosGc);

            /// Establezco la fecha y hora final de la ejeucicón
            estadisticaEntity.setFechaHoraFinal(new Timestamp(System.currentTimeMillis()));

            /// Calculo el tiempo de ejecución con el formato deseado
            String duracion = ComunHelper.calcularTiempoEjecucion(
                    estadisticaEntity.getFechaHoraInicial(),
                    estadisticaEntity.getFechaHoraFinal());

            /// Asigno la duración al objeto EstadisticaEntity
            estadisticaEntity.setDuracion(duracion);

            /// Asigno las estadísticas al objeto LogEntity
            logEntity.setEstadisticaEntity(estadisticaEntity);

            ///
            ///     PERSISTENCIA EN LA BASE DE DATOS DEL OBJETO
            ///
            service.persistir(logEntity, parseoFicherosGc);

            /// Envío un correo con la información de la ejecución del aplicativo
            var miMail = new MiMail(propertyManager, estadisticaEntity);
            miMail.enviarEmail();

            /// Imprimir resumen
            log.info(Mensajes.RESUMEN_EJECUCION);
            ComunHelper.imprimir(logEntity);

            /// Finalizar el programa correctamente
            FinalDelPrograma.finalizar(TipoFinalEjecucion.CORRECTO);

        } catch (MiMailException | MiServiceException | MiSessionFactoryProviderException  ex) {

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
