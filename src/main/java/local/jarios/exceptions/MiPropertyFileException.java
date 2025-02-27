package local.jarios.exceptions;

import java.io.IOException;

/**
 * Description:
 * Author: juan
 * Date: 28/12/2024
 * Team:
 */
public class MiPropertyFileException extends Exception {

    /// Constructor con mensaje de error y causa
    public MiPropertyFileException(IOException ex) {

        super(ex);
    }
}
