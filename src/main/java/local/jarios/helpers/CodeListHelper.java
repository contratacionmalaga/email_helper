package local.jarios.helpers;

import local.jarios.genericode.CodeList;

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

    public static CodeList getCodeListFromFile (File file) throws MiParserException {


        try {

            /// Parseo el contenido del fichero con JAXB
            var jc = JAXBContext.newInstance(CodeList.class);

            ///
            var atomUnMarshaller = jc.createUnmarshaller();

            ///
            return (CodeList) atomUnMarshaller.unmarshal(file);

        } catch (JAXBException ex) {

            ///
            throw new MiParserException(ex.getMessage(), ex);

        }
    }
}
