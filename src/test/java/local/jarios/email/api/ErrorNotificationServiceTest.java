package local.jarios.email.api;

import local.jarios.email.exception.EmailException;
import local.jarios.email.model.EmailData;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class ErrorNotificationServiceTest {

    @Test
    void shouldBuildErrorEmailWithConfiguredSenderAndRecipient() {
        CapturingEmailService emailService = new CapturingEmailService();
        ErrorNotificationService service = new ErrorNotificationService(
            emailService,
            "errors@example.com",
            "ops@example.com"
        );

        Properties props = new Properties();
        RuntimeException error = new RuntimeException("boom");

        service.notifyError(error, "batch process", props);

        assertThat(emailService.props).isSameAs(props);
        assertThat(emailService.emailData.from()).isEqualTo("errors@example.com");
        assertThat(emailService.emailData.to()).isEqualTo("ops@example.com");
        assertThat(emailService.emailData.subject())
            .isEqualTo("❌ ERROR en servicio de envío de email");
        assertThat(emailService.emailData.body()).contains("batch process");
    }

    @Test
    void shouldNotPropagateNotificationErrors() {
        ErrorNotificationService service = new ErrorNotificationService(
            (props, data) -> {
                throw new EmailException("SMTP unavailable");
            },
            "errors@example.com",
            "ops@example.com"
        );

        assertThatCode(() -> service.notifyError(
            new RuntimeException("boom"),
            "batch process",
            new Properties()
        )).doesNotThrowAnyException();
    }

    private static final class CapturingEmailService implements EmailService {

        private Properties props;
        private EmailData emailData;

        @Override
        public void sendEmail(Properties props, EmailData data) throws EmailException {
            this.props = props;
            this.emailData = data;
        }
    }
}
