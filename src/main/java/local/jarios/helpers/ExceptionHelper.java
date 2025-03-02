package local.jarios.helpers;

import local.jarios.utils.Mensajes;
import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 * Author: juan
 * Date: 04/02/2025
 * Team:
 */
@Slf4j
public class ExceptionHelper {

    private ExceptionHelper() { }

    /**
     * Método para registrar la excepción con la clase, método y mensaje de error
     * @param ex Excepción ocurrida
     */
    public static void logException(Exception ex) {

        /// Obtener la pila de ejecución
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        /// El primer elemento es el getStackTrace(), el segundo es el método actual
        String className = stackTrace[2].getClassName();        /// Nombre de la clase
        String methodName = stackTrace[2].getMethodName();      /// Nombre del método

        /// Registro la excepción
        log.error(Mensajes.EXCEPTION_ERROR, className, methodName, ex.getMessage());
    }
}
