package local.jarios.email.model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class EmailDataTest {

    @Test
    void testEmailDataGetters() {
        // Arrange
        String from = "from@example.com";
        String to = "to@example.com";
        String subject = "Subject";
        String body = "Body";

        EmailData data = new EmailData(from, to, subject, body);

        // Act & Assert
        assertEquals(from, data.from());
        assertEquals(to, data.to());
        assertEquals(subject, data.subject());
        assertEquals(body, data.body());
    }

    @Test
    void testEmailDataSetters() {
        // Arrange
        EmailData data = new EmailData(
            "from@example.com",
                "to@example.com",
                "Subject",
                "Body");

        // Assert
        assertEquals("from@example.com", data.from());
        assertEquals("to@example.com", data.to());
        assertEquals("Subject", data.subject());
        assertEquals("Body", data.body());
    }
}
