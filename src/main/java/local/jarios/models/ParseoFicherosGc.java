package local.jarios.models;

import local.jarios.entity.Auditable;
import local.jarios.entity.FicheroGc;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: Importaciones de Ficheros Excel desde Internet
 * Author: Juan Antonio
 * Date: 04/06/2024
 * Team: Juan Antonio
 */

@Setter
@Getter
public class ParseoFicherosGc extends Auditable {

    private List<FicheroGc> listFicherosGc;
    private Map<String, List<RegistroGc>> mapRegistrosGcByFicheroGc;

    public ParseoFicherosGc() {
        this.listFicherosGc = new ArrayList<>();
        this.mapRegistrosGcByFicheroGc = new HashMap<>();
    }
}
