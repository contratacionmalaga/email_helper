package local.jarios.email.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailDataTest {

    @Test
    void testEmailDataRecord() {
        String from = "sender@example.com";
        String to = "recipient@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        EmailData data = new EmailData(from, to, subject, body);

        assertEquals(from, data.from());
        assertEquals(to, data.to());
        assertEquals(subject, data.subject());
        assertEquals(body, data.body());
    }

    @Test
    void testEqualsAndHashCode() {
        EmailData data1 = new EmailData("a@a.com", "b@b.com", "Sub", "Body");
        EmailData data2 = new EmailData("a@a.com", "b@b.com", "Sub", "Body");
        EmailData data3 = new EmailData("x@x.com", "y@y.com", "Other", "Text");

        assertEquals(data1, data2);
        assertEquals(data1.hashCode(), data2.hashCode());
        assertNotEquals(data1, data3);
    }

    @Test
    void testToString() {
        EmailData data = new EmailData("a@a.com", "b@b.com", "Test", "Hola");
        String str = data.toString();
        assertTrue(str.contains("a@a.com"));
        assertTrue(str.contains("b@b.com"));
        assertTrue(str.contains("Test"));
        assertTrue(str.contains("Hola"));
    }
}
