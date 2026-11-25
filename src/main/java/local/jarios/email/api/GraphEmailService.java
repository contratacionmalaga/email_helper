package local.jarios.email.api;

import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.models.BodyType;
import com.microsoft.graph.models.EmailAddress;
import com.microsoft.graph.models.ItemBody;
import com.microsoft.graph.models.Message;
import com.microsoft.graph.models.Recipient;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import com.microsoft.graph.users.item.sendmail.SendMailPostRequestBody;
import com.microsoft.kiota.ApiException;
import com.microsoft.kiota.http.middleware.options.RetryHandlerOption;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import java.util.Arrays;
import java.util.Properties;
import local.jarios.email.exception.EmailException;
import local.jarios.email.model.EmailData;
import local.jarios.email.validator.EmailRequestValidator;

/** Envía mediante Microsoft Graph con las credenciales de una aplicación de Microsoft Entra. */
public final class GraphEmailService implements EmailService {
  private static final String TENANT_ID = "MICROSOFT_GRAPH_TENANT_ID";
  private static final String CLIENT_ID = "MICROSOFT_GRAPH_CLIENT_ID";
  private static final String CLIENT_SECRET = "MICROSOFT_GRAPH_CLIENT_SECRET";
  private static final String MAILBOX = "MAIL_MAILBOX";
  private static final String OUTBOUND_ENABLED = "MAIL_OUTBOUND_ENABLED";
  private static final String GRAPH_SCOPE = "https://graph.microsoft.com/.default";

  private GraphClient cachedClient;

  /** Lee las variables que el lanzador ha pasado al proceso Java. */
  public static Properties propertiesFromEnvironment() {
    Properties props = new Properties();
    for (String key :
        new String[] {TENANT_ID, CLIENT_ID, CLIENT_SECRET, MAILBOX, OUTBOUND_ENABLED}) {
      String value = System.getenv(key);
      if (value != null) {
        props.setProperty(key, value);
      }
    }
    return props;
  }

  @Override
  public void sendEmail(Properties props, EmailData data) {
    if (props == null || !"true".equalsIgnoreCase(props.getProperty(OUTBOUND_ENABLED, "").trim())) {
      throw new EmailException("Envío por Microsoft Graph deshabilitado: " + OUTBOUND_ENABLED);
    }
    EmailRequestValidator.validarMensaje(data);
    String mailbox = required(props, MAILBOX).trim();
    if (!mailbox.equalsIgnoreCase(data.from().trim())) {
      throw new EmailException("El remitente debe coincidir con " + MAILBOX + ".");
    }

    SendMailPostRequestBody request = createRequest(data);
    String tenantId = required(props, TENANT_ID).trim();
    String clientId = required(props, CLIENT_ID).trim();
    String clientSecret = required(props, CLIENT_SECRET);
    try {
      clientFor(tenantId, clientId, clientSecret)
          .users()
          .byUserId(mailbox)
          .sendMail()
          // Repetir un envío de resultado incierto puede generar un duplicado.
          .post(request, config -> config.options.add(new RetryHandlerOption(null, 0, 0)));
    } catch (ApiException ex) {
      throw new EmailException(
          "Microsoft Graph rechazó el envío (HTTP " + ex.getResponseStatusCode() + ").");
    } catch (RuntimeException ex) {
      throw new EmailException(
          "No se pudo autenticar o completar el envío mediante Microsoft Graph.");
    }
  }

  private synchronized GraphServiceClient clientFor(
      String tenantId, String clientId, String secret) {
    if (cachedClient == null
        || !cachedClient.tenantId.equals(tenantId)
        || !cachedClient.clientId.equals(clientId)
        || !cachedClient.secret.equals(secret)) {
      var credential =
          new ClientSecretCredentialBuilder()
              .tenantId(tenantId)
              .clientId(clientId)
              .clientSecret(secret)
              .build();
      cachedClient =
          new GraphClient(
              tenantId, clientId, secret, new GraphServiceClient(credential, GRAPH_SCOPE));
    }
    return cachedClient.client;
  }

  private static SendMailPostRequestBody createRequest(EmailData data) {
    try {
      var recipients =
          Arrays.stream(InternetAddress.parse(data.to()))
              .map(
                  address -> {
                    EmailAddress email = new EmailAddress();
                    email.setAddress(address.getAddress());
                    Recipient recipient = new Recipient();
                    recipient.setEmailAddress(email);
                    return recipient;
                  })
              .toList();
      if (recipients.isEmpty()) {
        throw new EmailException("El mensaje debe tener al menos un destinatario.");
      }
      ItemBody body = new ItemBody();
      body.setContentType(BodyType.Html);
      body.setContent(data.body());

      Message message = new Message();
      message.setSubject(data.subject());
      message.setBody(body);
      message.setToRecipients(recipients);

      SendMailPostRequestBody request = new SendMailPostRequestBody();
      request.setMessage(message);
      return request;
    } catch (AddressException ex) {
      throw new EmailException("No se pudieron interpretar los destinatarios.", ex);
    }
  }

  private static String required(Properties props, String key) {
    String value = props.getProperty(key);
    if (value == null || value.isBlank()) {
      throw new EmailException("Falta configurar " + key + ".");
    }
    return value;
  }

  private record GraphClient(
      String tenantId, String clientId, String secret, GraphServiceClient client) {
    @Override
    public String toString() {
      return "Cliente de Microsoft Graph";
    }
  }
}
