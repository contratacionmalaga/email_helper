package local.jarios.helpers;

import local.jarios.entity.Log;
import local.jarios.mappers.MapperRegistroGcFromCodeList;
import local.jarios.models.ParseoFicherosGc;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * Description:
 * Author: juan
 * Date: 04/02/2025
 * Team:
 */

@Slf4j
public final class FileHelper {

    /**
     * CONSTRUCTOR PRIVADO DE LA CLASE PUESTO QUE ESTA FINAL
     */
    private FileHelper() { }

    /**
     * Método que devuelve un Array con los ficheros que se encuentran en una ruta
     * @param path Ruta desde la que se obtendrán todos los ficheros
     * @return Array con los Ficheros
     */
    public static File[] getListaFicherosFromPath (String path) {

        File directorio = new File(path);

        if (!directorio.exists() || !directorio.isDirectory()) {

            ///
            log.warn("El path no existe o no es un directorio válido.");
            return new File[0];  // Retornar un array vacío si el directorio no es válido
        }

        /// Intentar obtener los archivos del directorio
        return directorio.listFiles();
    }

    /**
     * Método que determina si un fichero es correcto
     * @param file Fichero a analizar
     * @return boolean Indicando si el fichero es correcto o no
     */
    public static boolean esFicheroCorrecto(File file) {

        ///
        return file.exists() && file.canRead() && file.exists();
    }

    /**
     * Método utilizado para procesar la lista de ficheros y devolver el objeto ParseoFicherosGc para después contrastar
     *      la información con la existente en la base de datos, unificar ambas fuentes de información y grabar el
     *      resultado en la base de datos
     * @param logEntity Objeto LogEntity
     * @param listFiles Lista de Files
     * @return List<FicheroGcEntity>
     */
    public static ParseoFicherosGc procesarListaFicherosFromPath(Log logEntity, File[] listFiles) {

        /// Creo un objeto del tipo ParseoFicherosGc que a su vez crea los objetos hijos --> NO SON NULOS
        ParseoFicherosGc parseoFicherosGc = new ParseoFicherosGc();

        /// Recorro la lista de ficheros
        for (File file : listFiles) {

            /// Analizo si el File es Correcto
            if (esFicheroCorrecto(file)) {

                /// OBTENGO EL OBJETO CodeList A PARTIR DEL File
                var codeList = CodeListHelper.getCodeListFromFile(file);

                /// Obtengo el objeto FicheroGcEntity a partir de un File
                var ficheroGcEntity = CodeListHelper.procesarCodeList(logEntity, codeList);

                ///  Únicamente si el objeto FicheroGcEntity no es NULL lo añado a la lista
                if (ficheroGcEntity != null) {

                    /// Añado un nuevo registro a la lista de FicherosGcEntity
                    parseoFicherosGc.getListFicherosGc().add(ficheroGcEntity);

                    /// Añado una nueva entrada <FicheroGcEntity.getShortName, List<RegistroGc>> al Map
                    parseoFicherosGc
                            .getMapRegistrosGcByFicheroGc()
                            .put(
                                    ficheroGcEntity.getShortName(),
                                    MapperRegistroGcFromCodeList.getListRegistroGcFromCodeList(codeList));
                }
            }
        }

        /// Devuelvo la lista
        return parseoFicherosGc;

    }
}
