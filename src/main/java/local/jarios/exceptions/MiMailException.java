package local.jarios.exceptions;

import jakarta.mail.MessagingException;

/**
 * Description:
 * Author: juan
 * Date: 28/12/2024
 * Team:
 */
public class MiMailException extends Exception {

    public MiMailException(MessagingException ex) {

        super(ex);
    }
}
