package local.jarios.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Description:
 * Author: juan
 * Date: 04/02/2025
 * Team:
 */
public class FileHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileHelper.class);

    public static File[] getListaFicherosFromPath (String path) {

        File directorio = new File(path);

        if (!directorio.exists() || !directorio.isDirectory()) {

            ///
            LOGGER.warn("El path no existe o no es un directorio válido.");
            return new File[0];  // Retornar un array vacío si el directorio no es válido
        }

        /// Intentar obtener los archivos del directorio
        return directorio.listFiles();
    }
}
