package local.jarios.helpers;

import local.jarios.entity.FicheroGcEntity;
import local.jarios.entity.LogEntity;
import local.jarios.genericode.CodeList;
import local.jarios.mapper.MapperFicheroGcFromCodeList;
import local.jarios.mapper.MapperRegistroGcFromCodeList;
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
     *
     * @param file Fichero a analizar
     * @return boolean Indicando si el fichero es correcto o no
     */
    public static boolean esFicheroCorrecto(File file) {

        ///
        return file.exists() && file.canRead() && file.exists();
    }

    /**
     *
     * @param logEntity Objeto LogEntity
     * @param codeList Objeto CodeList a Parsear
     * @return ficheroGcEntity
     */
    public static FicheroGcEntity procesarCodeList (LogEntity logEntity, CodeList codeList) {

        /// OBTENGO EL OBJETO FICHEROGCENTITY A PARTIR DEL codeList
        var ficheroGcEntity = MapperFicheroGcFromCodeList.getFicheroGcFromCodeList(logEntity, codeList);

        /// Analizo si el valor que devuelvo es NULL
        if (ficheroGcEntity == null) {

            return null;
        }

        /// ASIGNO EL OBJETO LogEntity AL OBJETO FicheroGcEntity
        ficheroGcEntity.setLogEntity(logEntity);

        /// DEVUELVO EL OBJETO FicheroGcEntity
        return ficheroGcEntity;

    }

    /**
     *
     * @param logEntity Objeto LogEntity
     * @param listFiles Lista de Files
     * @return List<FicheroGcEntity>
     */
    public static ParseoFicherosGc procesarListaFicherosFromPath(LogEntity logEntity, File[] listFiles) {

        /// Creo un objeto del tipo ParseoFicherosGc que a su vez crea los objetos hijos --> NO SON NULOS
        ParseoFicherosGc parseoFicherosGc = new ParseoFicherosGc();

        /// Recorro la lista de ficheros
        for (File file : listFiles) {

            /// Analizo si el File es Correcto
            if (esFicheroCorrecto(file)) {

                /// OBTENGO EL OBJETO CodeList A PARTIR DEL File
                var codeList = CodeListHelper.getCodeListFromFile(file);

                /// Obtengo el objeto FicheroGcEntity a partir de un File
                var ficheroGcEntity = procesarCodeList(logEntity, codeList);

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
