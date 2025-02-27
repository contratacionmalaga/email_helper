package local.jarios.exceptions;

/**
 * Description:
 * Author: juan
 * Date: 28/12/2024
 * Team:
 */
public class MiServiceException extends Exception {

    public MiServiceException(MiRepositoryException ex) {

        super(ex);
    }
}
