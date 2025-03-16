package local.jarios.interfaces;

import local.jarios.entity.Log;

import java.util.UUID;

/**
 * Description:
 * Author: juan
 * Date: 01/03/2025
 * Team:
 */

/// Interfaz genérica para asegurar que los objetos tengan los métodos necesarios
public interface Actualizable<T> {

    /// Método que devuelve el valor único del objeto, usado como clave
    String getUniqueKey();

    /// Método que devuelvel el identificador del registro
    UUID getId();

    /// Método para actualizar el objeto con los valores de otro
    void actualizarCon(T otro);

    /// Método para aasignar el valor del objeto LogEntity del que hereda
    void setLogEntity(Log logEntity);
}
