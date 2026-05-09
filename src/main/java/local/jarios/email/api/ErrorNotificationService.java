package local.jarios.email.api;

import local.jarios.email.helper.ErrorEmailBuilder;
import local.jarios.email.model.EmailData;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Properties;

@Slf4j
public class ErrorNotificationService {

  private final EmailService emailService;
  private final String from;
  private final String to;

  public ErrorNotificationService(
      EmailService emailService,
      String from,
      String to
  ) {
    this.emailService = Objects.requireNonNull(emailService, "emailService");
    this.from = Objects.requireNonNull(from, "from");
    this.to = Objects.requireNonNull(to, "to");
  }

  public void notifyError(
      Throwable ex,
      String contexto,
      Properties props
  ) {
    try {
      EmailData errorEmail = ErrorEmailBuilder.build(
          ex,
          contexto,
          from,
          to
      );

      emailService.sendEmail(props, errorEmail);

      log.info("Correo de error enviado correctamente.");

    } catch (Exception notifyEx) {
      // MUY IMPORTANTE: nunca relanzar aquí
      log.error("No se pudo enviar el correo de error.", notifyEx);
    }
  }
}
