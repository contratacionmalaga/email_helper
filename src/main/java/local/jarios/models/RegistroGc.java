package local.jarios.models;

import local.jarios.entity.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Description: Importaciones de Ficheros Excel desde Internet
 * Author: Juan Antonio
 * Date: 04/06/2024
 * Team: Juan Antonio
 */

@Setter
@Getter
@NoArgsConstructor
public class RegistroGc extends Auditable {

    private String code;
    private String nombre;

    public RegistroGc(String code, String nombre) {

        this.code = code;
        this.nombre = nombre;
    }

    @Override
    public String toString() {

        return "(" + code + "," + nombre + ")";
    }
}
