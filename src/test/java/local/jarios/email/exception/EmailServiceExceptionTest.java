package local.jarios.email.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailServiceExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String msg = "Error de envío";
        EmailException exception = new EmailException(msg);

        assertEquals(msg, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String msg = "Error de conexión SMTP";
        Throwable cause = new RuntimeException("Timeout");
        EmailException exception = new EmailException(msg, cause);

        assertEquals(msg, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
