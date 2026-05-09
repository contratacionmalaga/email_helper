package local.jarios.email.validator;

import local.jarios.email.exception.EmailException;
import local.jarios.email.model.EmailData;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static local.jarios.email.common.util.Constantes.SMTP_AUTH;
import static local.jarios.email.common.util.Constantes.SMTP_HOST;
import static local.jarios.email.common.util.Constantes.SMTP_PASSWORD;
import static local.jarios.email.common.util.Constantes.SMTP_PORT;
import static local.jarios.email.common.util.Constantes.SMTP_STARTTLS;
import static local.jarios.email.common.util.Constantes.SMTP_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailRequestValidatorTest {

    @Test
    void shouldValidateCompleteEmailRequest() {
        assertThatCode(() -> EmailRequestValidator.validarEmailRequest(
            validSmtpProperties(),
            validEmailData()
        )).doesNotThrowAnyException();
    }

    @Test
    void shouldRejectNullSmtpProperties() {
        assertThatThrownBy(() -> EmailRequestValidator.validarEmailRequest(
            null,
            validEmailData()
        ))
            .isInstanceOf(EmailException.class)
            .hasMessage("Las propiedades SMTP son obligatorias.");
    }

    @Test
    void shouldRejectNullEmailData() {
        assertThatThrownBy(() -> EmailRequestValidator.validarEmailRequest(
            validSmtpProperties(),
            null
        ))
            .isInstanceOf(EmailException.class)
            .hasMessage("Los datos del email son obligatorios.");
    }

    @Test
    void shouldRejectNullBodyWithDomainException() {
        EmailData data = new EmailData(
            "from@example.com",
            "to@example.com",
            "Subject",
            null
        );

        assertThatThrownBy(() -> EmailRequestValidator.validarEmailRequest(
            validSmtpProperties(),
            data
        ))
            .isInstanceOf(EmailException.class)
            .hasMessage("El cuerpo del mensaje ('body') es obligatorio.");
    }

    @Test
    void shouldRejectBlankRequiredFields() {
        EmailData data = new EmailData(
            "from@example.com",
            " ",
            "Subject",
            "Body"
        );

        assertThatThrownBy(() -> EmailRequestValidator.validarEmailRequest(
            validSmtpProperties(),
            data
        ))
            .isInstanceOf(EmailException.class)
            .hasMessage("El destinatario ('to') es obligatorio.");
    }

    @Test
    void shouldRejectInvalidSenderAddress() {
        EmailData data = new EmailData(
            "not-an-email",
            "to@example.com",
            "Subject",
            "Body"
        );

        assertThatThrownBy(() -> EmailRequestValidator.validarEmailRequest(
            validSmtpProperties(),
            data
        ))
            .isInstanceOf(EmailException.class)
            .hasMessage("Email 'from' inválido: not-an-email");
    }

    @Test
    void shouldReportInvalidRecipientAddresses() {
        assertThat(EmailRequestValidator.getInvalidEmailsRFC(
            "one@example.com, invalid-address, two@example.com"
        )).containsExactly("invalid-address");
    }

    @Test
    void shouldRejectMissingSmtpProperty() {
        Properties props = validSmtpProperties();
        props.remove(SMTP_PASSWORD);

        assertThatThrownBy(() -> EmailRequestValidator.validarEmailRequest(
            props,
            validEmailData()
        ))
            .isInstanceOf(EmailException.class)
            .hasMessage("Falta la propiedad SMTP obligatoria: " + SMTP_PASSWORD);
    }

    private static EmailData validEmailData() {
        return new EmailData(
            "from@example.com",
            "to@example.com",
            "Subject",
            "Body"
        );
    }

    private static Properties validSmtpProperties() {
        Properties props = new Properties();

        props.setProperty(SMTP_USER, "user@example.com");
        props.setProperty(SMTP_PASSWORD, "secret");
        props.setProperty(SMTP_AUTH, "true");
        props.setProperty(SMTP_STARTTLS, "true");
        props.setProperty(SMTP_HOST, "smtp.example.com");
        props.setProperty(SMTP_PORT, "587");

        return props;
    }
}
