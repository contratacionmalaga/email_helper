package local.jarios.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MiError {

    private String mensaje;
    private String[] listaStackTraceElements;

}
