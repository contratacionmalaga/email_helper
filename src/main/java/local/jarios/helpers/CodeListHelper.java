package local.jarios.helpers;

import local.jarios.entity.FicheroGcEntity;
import local.jarios.entity.LogEntity;
import local.jarios.exceptions.MiParseException;
import local.jarios.genericode.CodeList;
import local.jarios.mappers.MapperFicheroGcFromCodeList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;

/**
 * Description:
 * Author: juan
 * Date: 04/02/2025
 * Team:
 */
public class CodeListHelper {

    private CodeListHelper() { }

    /**
     *
     * @param file Fichero que voy a parsear
     * @return Devuelvo un objeto del tipo CodeList
     * @throws MiParseException Excepción en caso de ocurrir algún error
     */
    public static CodeList getCodeListFromFile (File file) throws MiParseException {


        try {

            /// Parseo el contenido del fichero con JAXB
            var jc = JAXBContext.newInstance(CodeList.class);

            ///
            var atomUnMarshaller = jc.createUnmarshaller();

            ///
            return (CodeList) atomUnMarshaller.unmarshal(file);

        } catch (JAXBException ex) {

            ///
            throw new MiParseException(ex.getMessage(), ex);

        }
    }

    /**
     *
     * @param logEntity Objeto LogEntity
     * @param codeList Objeto CodeList con la información que voy a mapear a un FicheroGc -> NINGÚN campo es NULL
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
}
