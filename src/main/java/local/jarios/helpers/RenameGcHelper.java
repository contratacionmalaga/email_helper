package local.jarios.helpers;

import local.jarios.models.RegistroGc;
import local.jarios.genericode.CodeList;
import local.jarios.genericode.Row;
import local.jarios.genericode.Value;
import local.jarios.utils.ConstantesGenerales;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: juan
 * Date: 04/02/2025
 * Team:
 */
public class RenameGcHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(RenameGcHelper.class);

    private RenameGcHelper() { }

    public static List<RegistroGc> getListTablaGcEntity (CodeList codeList) {

        ///
        List<RegistroGc> listaRegistrosGc = new ArrayList<>();

        ///
        var mensaje = "";

        if (!codeList.getSimpleCodeList().getRow().isEmpty()) {

            ///
            for (Row row : codeList.getSimpleCodeList().getRow()) {

                ///
                String code = null;
                String nombre = null;

                ///
                for (Value value : row.getValues()) {

                    ///
                    switch (value.getColumnRef()) {
                        case (ConstantesGenerales.VALUE_CODE) -> code = value.getSimpleValue();
                        case (ConstantesGenerales.VALUE_NOMBRE) -> nombre = value.getSimpleValue();
                        case (ConstantesGenerales.VALUE_NAME) -> { }
                        default -> {
                            mensaje = String.format(
                                    "NO SE RECOGE LA COLUMNA ColumnRef: (%s) CON VALOR SimpleValue: (%s)",
                                    value.getColumnRef(),
                                    value.getSimpleValue());
                            LOGGER.info(mensaje);
                        }
                    }
                }

                ///
                listaRegistrosGc.add(new RegistroGc(code, nombre));
            }
        }

        return listaRegistrosGc;
    }
}
