package local.jarios.models;

import local.jarios.entity.Auditable;
import local.jarios.entity.FicheroGcEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Description: Importaciones de Ficheros Excel desde Internet
 * Author: Juan Antonio
 * Date: 04/06/2024
 * Team: Juan Antonio
 */

@Setter
@Getter
@NoArgsConstructor
public class DatosFicheroGc extends Auditable {


    private FicheroGcEntity ficheroGcEntity;
    private List<RegistroGc> listRegistroGc;

}
