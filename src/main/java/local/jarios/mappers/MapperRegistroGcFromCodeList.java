package local.jarios.mappers;

import local.jarios.models.RegistroGc;
import local.jarios.genericode.CodeList;
import local.jarios.genericode.Row;
import local.jarios.genericode.Value;
import local.jarios.utils.ConstantesGenerales;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: juan
 * Date: 04/02/2025
 * Team:
 */

@Slf4j
public final class MapperRegistroGcFromCodeList {

    /**
     * CONSTRUCTOR PRIVADO DE LA CLASE PUESTO QUE ESTA FINAL
     */
    private MapperRegistroGcFromCodeList() { }

    public static List<RegistroGc> getListRegistroGcFromCodeList(CodeList codeList) {

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
                            log.info(mensaje);
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
