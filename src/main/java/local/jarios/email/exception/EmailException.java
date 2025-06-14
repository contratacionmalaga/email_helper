package local.jarios.email.exception;

/**
 * Description:
 * Author: juan
 * Date: 13/06/2025
 * Team:
 */
public class MailException extends RuntimeException {
    public MailException(String message) {
        super(message);
    }

    public MailException(String message, Throwable cause) {
        super(message, cause);
    }
}
