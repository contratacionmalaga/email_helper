package local.jarios.email.exception;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class EmailServiceExceptionTest {

    @Test
    void testEmailServiceExceptionMessage() {
        // Arrange
        String message = "Test exception message";

        // Act
        EmailServiceException exception = new EmailServiceException(message);

        // Assert
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testEmailServiceExceptionCause() {
        // Arrange
        Throwable cause = new Throwable("Test cause");

        // Act
        EmailServiceException exception = new EmailServiceException("Test exception message", cause);

        // Assert
        assertEquals(cause, exception.getCause());
    }
}
