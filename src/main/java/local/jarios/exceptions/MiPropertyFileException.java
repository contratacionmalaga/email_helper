package local.jarios.exceptions;

/**
 * Description:
 * Author: juan
 * Date: 28/12/2024
 * Team:
 */
public class MiPropertyFileException extends RuntimeException {

    /// Constructor con mensaje de error y causa
    public MiPropertyFileException(String message, Throwable ex) {

        super(message, ex);
    }
}
