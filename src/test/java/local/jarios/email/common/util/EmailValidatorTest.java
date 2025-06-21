package local.jarios.email.common.util;

import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {

    @Test
    void testIsValidEmailRFC() {
        // Arrange
        String validEmail = "valid@example.com";
        String invalidEmail = "invalid-email";

        // Act & Assert
        assertTrue(EmailValidator.isValidEmailRFC(validEmail));
        assertFalse(EmailValidator.isValidEmailRFC(invalidEmail));
    }

    @Test
    void testGetInvalidEmailsRFC() {
        // Arrange
        String validEmail = "valid@example.com";
        String invalidEmail = "invalid-email";

        // Act
        List<String> invalidEmails = EmailValidator.getInvalidEmailsRFC(String.valueOf(Arrays.asList(validEmail, invalidEmail)));

        // Assert
        assertTrue(invalidEmails.contains(invalidEmail));
        assertFalse(invalidEmails.contains(validEmail));
    }
}
