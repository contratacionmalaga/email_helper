package local.jarios.enums;

import lombok.Getter;

/**
 * Description: Determina si el contrato es MAYOR o MENOR
 * Author: juan
 * Date: 03/03/2024
 * Team: Juan Antonio Ríos Peláez
 */
@Getter
public enum PropertyFile {

    ///
    PROPERTY_CONFIG("config/config.properties"),

    ///
    PROPERTY_HIBERNATE("config/hibernate.properties"),

    ///
    PROPERTY_EMAIL("config/email.properties");

    ///
    private final String ruta;

    /// Constructor del enum para asignar la ruta
    PropertyFile(String ruta) {

        ///
        this.ruta = ruta;
    }
}
