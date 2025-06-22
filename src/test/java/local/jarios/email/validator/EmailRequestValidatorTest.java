package local.jarios.email.validator;

import local.jarios.email.exception.EmailException;
import local.jarios.email.model.EmailData;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static local.jarios.email.common.util.Constantes.*;
import static org.junit.jupiter.api.Assertions.*;

class EmailRequestValidatorTest {

    private Properties buildValidProperties() {
        Properties props = new Properties();
        props.setProperty(SMTP_USER, "user@example.com");
        props.setProperty(SMTP_PASSWORD, "securePassword");
        props.setProperty(SMTP_AUTH, "true");
        props.setProperty(SMTP_STARTTLS, "true");
        props.setProperty(SMTP_HOST, "smtp.example.com");
        props.setProperty(SMTP_PORT, "587");
        return props;
    }

    private EmailData buildValidEmailData() {
        return new EmailData("sender@example.com", "recipient@example.com", "Subject", "Message body");
    }

    @Test
    void shouldThrowWhenPropsIsNull() {
        assertThrows(EmailException.class, () ->
                EmailRequestValidator.validarEmailRequest(null, buildValidEmailData()));
    }

    @Test
    void shouldThrowWhenEmailDataIsNull() {
        assertThrows(EmailException.class, () ->
                EmailRequestValidator.validarEmailRequest(buildValidProperties(), null));
    }

    @Test
    void shouldThrowWhenFromIsBlank() {
        EmailData data = new EmailData("  ", "to@example.com", "sub", "body");
        assertThrows(EmailException.class, () ->
                EmailRequestValidator.validarEmailRequest(buildValidProperties(), data));
    }

    @Test
    void shouldThrowWhenToIsInvalid() {
        EmailData data = new EmailData("from@example.com", "invalid-email", "sub", "body");
        assertThrows(EmailException.class, () ->
                EmailRequestValidator.validarEmailRequest(buildValidProperties(), data));
    }

    @Test
    void shouldThrowWhenMissingSmtpProperty() {
        Properties props = buildValidProperties();
        props.remove(SMTP_PORT); // Remove a required property
        assertThrows(EmailException.class, () ->
                EmailRequestValidator.validarEmailRequest(props, buildValidEmailData()));
    }

    @Test
    void shouldPassValidationWithValidInput() {
        assertDoesNotThrow(() ->
                EmailRequestValidator.validarEmailRequest(buildValidProperties(), buildValidEmailData()));
    }

    @Test
    void shouldDetectMultipleInvalidToEmails() {
        String invalidList = "good@example.com, bad-email, ,another@valid.com, @bad.com";
        var invalids = EmailRequestValidator.getInvalidEmailsRFC(invalidList);
        assertEquals(2, invalids.size());
        assertTrue(invalids.contains("bad-email"));
        assertTrue(invalids.contains("@bad.com"));
    }
}
