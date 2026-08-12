package local.jarios.email.api;

import jakarta.mail.Message;
import jakarta.mail.Session;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailServiceImplTest {

    @Test
    void shouldBuildMimeMessageAndDelegateToSender() throws Exception {
        CapturingEmailSender sender = new CapturingEmailSender();
        EmailService service = new EmailServiceImpl(sender);
        EmailData data = validEmailData();

        service.sendEmail(validSmtpProperties(), data);

        assertThat(sender.session).isNotNull();
        assertThat(sender.message.getFrom()[0].toString()).isEqualTo(data.from());
        assertThat(sender.message.getRecipients(Message.RecipientType.TO)[0].toString())
            .isEqualTo(data.to());
        assertThat(sender.message.getSubject()).isEqualTo(data.subject());
        assertThat(sender.message.getContentType()).contains("text/html");
        assertThat(sender.message.getContent().toString()).contains(data.body());
    }

    @Test
    void shouldPropagateDomainExceptionFromSender() {
        EmailException failure = new EmailException("SMTP down");
        EmailService service = new EmailServiceImpl((session, message) -> {
            throw failure;
        });

        assertThatThrownBy(() -> service.sendEmail(validSmtpProperties(), validEmailData()))
            .isSameAs(failure);
    }

    @Test
    void shouldWrapUnexpectedRuntimeExceptionFromSender() {
        RuntimeException failure = new RuntimeException("boom");
        EmailService service = new EmailServiceImpl((session, message) -> {
            throw failure;
        });

        assertThatThrownBy(() -> service.sendEmail(validSmtpProperties(), validEmailData()))
            .isInstanceOf(EmailException.class)
            .hasMessage("Error inesperado durante el envío del correo electrónico.")
            .hasCause(failure);
    }

    @Test
    void shouldKeepValidationExceptionWhenPropertiesAreNull() {
        EmailService service = new EmailServiceImpl(new CapturingEmailSender());

        assertThatThrownBy(() -> service.sendEmail(null, validEmailData()))
            .isInstanceOf(EmailException.class)
            .hasMessage("Las propiedades SMTP son obligatorias.");
    }

    private static EmailData validEmailData() {
        return new EmailData(
            "from@example.com",
            "to@example.com",
            "Subject",
            "<p>Body</p>"
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

    private static final class CapturingEmailSender implements EmailSender {

        private Session session;
        private Message message;

        @Override
        public void send(Session session, Message message) {
            this.session = session;
            this.message = message;
        }
    }
}
