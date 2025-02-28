package local.jarios.enums;

import lombok.Getter;

/**
 * Description: Determina si el contrato es MAYOR o MENOR
 * Author: juan
 * Date: 03/03/2024
 * Team: Juan Antonio Ríos Peláez
 */
@Getter
public enum NombreConexion {
    ///
    PRINCIPAL(false);

    ///
    private final boolean esLectura;

    /**
     * Constructor del enum que indica si la conexión es ÚNICAMENTE de lectura
     * @param esLectura Indica si la conexión es ÚNICAMENTE de lectura
     */
    NombreConexion(boolean esLectura) {

        ///
        this.esLectura = esLectura;
    }

    // Método para obtener la conexión principal, sin que el valor 'esLectura' afecte el resultado
    public static NombreConexion getPrincipal() {
        return PRINCIPAL;  // Siempre devuelve PRINCIPAL, independientemente del esLectura
    }

    // Método para verificar si la conexión es de lectura
    public boolean isLectura() {
        return this.esLectura;
    }
}
