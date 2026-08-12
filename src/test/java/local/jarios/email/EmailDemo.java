package local.jarios.email;

import local.jarios.email.api.EmailSender;
import local.jarios.email.api.EmailSenderImpl;
import local.jarios.email.api.EmailService;
import local.jarios.email.api.EmailServiceImpl;
import local.jarios.email.api.ErrorNotificationService;
import local.jarios.email.enums.TipoFinalEjecucion;
import local.jarios.email.exception.EmailException;
import local.jarios.email.helper.ComunHelper;
import local.jarios.email.helper.EmailHelper;
import local.jarios.email.helper.FinalDelProgramaHelper;
import local.jarios.email.model.EmailData;
import local.jarios.email.validator.EmailRequestValidator;
import lombok.extern.slf4j.Slf4j;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;

import static local.jarios.email.common.util.Constantes.SMTP_AUTH;
import static local.jarios.email.common.util.Constantes.SMTP_CHECKSERVERIDENTITY;
import static local.jarios.email.common.util.Constantes.SMTP_HOST;
import static local.jarios.email.common.util.Constantes.SMTP_PASSWORD;
import static local.jarios.email.common.util.Constantes.SMTP_PORT;
import static local.jarios.email.common.util.Constantes.SMTP_PROTOCOLS;
import static local.jarios.email.common.util.Constantes.SMTP_SOCKETFACTORY_PORT;
import static local.jarios.email.common.util.Constantes.SMTP_STARTTLS;
import static local.jarios.email.common.util.Constantes.SMTP_TRUST;
import static local.jarios.email.common.util.Constantes.SMTP_USER;

/**
 * Clase de demostración para el envío de correos electrónicos.
 *
 * @author Juan
 * @version 1.0
 */
@Slf4j
public final class EmailDemo {

    private EmailDemo() {
        // No instanciable
    }

    public static void main(String[] args) {

        imprimirTitulo("==== Inicio de la ejecución del Email Helper ====");

        try {
            Properties mailPropsValidas = cargarPropertiesSmtpValidas();
            log.info("Cargadas propiedades SMTP (válidas) correctamente.");

            EmailData emailDataOk = construirEmailData();
            log.info("Objeto EmailData con datos (válidos) construido correctamente.");

            validar(mailPropsValidas, emailDataOk);
            log.info("Validación correcta de propiedades y datos del email (válidos).");

            enviar(mailPropsValidas, emailDataOk);
            log.info("Enviado correctamente email con datos (válidos).");

            Properties mailPropsErroneas = cargarPropertiesSmtpErroneas();
            log.info("Cargadas propiedades SMTP (erróneas) correctamente.");

            EmailData emailDataError = construirEmailData();
            log.info("Objeto EmailData con datos (erróneas) construido correctamente.");

            validar(mailPropsErroneas, emailDataError);
            log.info("Validación correcta de propiedades y datos del email (erróneos).");

            enviar(mailPropsErroneas, emailDataError);
            log.info("Enviado correctamente email con datos (erróneos).");

            FinalDelProgramaHelper.finalizar(TipoFinalEjecucion.CORRECTO);

        } catch (Exception ex) {

            String contexto = switch (ex) {
                case EmailException ignored -> "Error de envío SMTP";
                case UnknownHostException ignored -> "Host SMTP no alcanzable";
                case RuntimeException ignored -> "Error inesperado durante el proceso";
                default -> "Error no controlado";
            };

            log.error("Error detectado en EmailDemo: {}", contexto);

            ErrorNotificationService notifier =
                new ErrorNotificationService(
                    new EmailServiceImpl(new EmailSenderImpl()),
                    getRequiredEnv("ERROR_EMAIL_FROM"),
                    getRequiredEnv("ERROR_EMAIL_TO")
                );

            notifier.notifyError(
                ex,
                contexto,
                cargarPropertiesSmtpValidas() // o SMTP alternativo
            );

            FinalDelProgramaHelper.finalizar(TipoFinalEjecucion.ERROR);
        }
    }

    /* ===================== */
    /* MÉTODOS PRIVADOS */
    /* ===================== */

    private static Properties cargarPropertiesSmtpComunes() {

        Properties props = new Properties();

        Map<String, String> propMap = Map.of(
            SMTP_HOST, getRequiredEnv("SMTP_HOST"),
            SMTP_AUTH, "true",
            SMTP_PORT, "587",
            SMTP_SOCKETFACTORY_PORT, "587",
            SMTP_CHECKSERVERIDENTITY, "true",
            SMTP_PROTOCOLS, "TLSv1.2",
            SMTP_TRUST, getRequiredEnv("SMTP_HOST"),
            SMTP_STARTTLS, "true",
            SMTP_USER, getRequiredEnv("SMTP_USER")
        );

        propMap.forEach(props::setProperty);

        log.debug("Propiedades SMTP inicializadas ({} claves).", propMap.size());
        return props;
    }

    private static Properties cargarPropertiesSmtpValidas() {

        Properties props = cargarPropertiesSmtpComunes();

        Map<String, String> propMap = Map.of(
            SMTP_PASSWORD, getRequiredEnv("SMTP_PASSWORD")
        );

        propMap.forEach(props::setProperty);

        log.debug("Propiedades SMTP (válidas) inicializadas ({} claves).", propMap.size());
        return props;
    }

    private static Properties cargarPropertiesSmtpErroneas() {

        Properties props = cargarPropertiesSmtpComunes();

        Map<String, String> propMap = Map.of(
            SMTP_PASSWORD, System.getenv().getOrDefault("SMTP_PASSWORD_INVALID", "PRUEBA")
        );

        propMap.forEach(props::setProperty);

        log.debug("Propiedades SMTP (erróneas) inicializadas ({} claves).", propMap.size());
        return props;
    }

    private static EmailData construirEmailData() throws UnknownHostException {

        String from = getRequiredEnv("EMAIL_FROM");
        log.debug("Variable from: {}", from);

        String to = getRequiredEnv("EMAIL_TO");
        log.debug("Variable to: {}", to);

        String hostname = ComunHelper.getHostName();
        log.debug("Variable hostname: {}", hostname);

        String subject = EmailHelper.getAsunto(
            "email_helper",
            "VERSION_PRUEBA",
            hostname,
            true);
        log.debug("Variable subject: {}", subject);

        String body = "Cuerpo del email";
        log.debug("Cuerpo del correo generado ({} caracteres).", body.length());

        return new EmailData(from, to, subject, body);
    }

    /**
     * Validación previa al envío del email.
     * @param props Propiedades a validar.
     * @param emailData Datos del email a validar.
     */
    private static void validar(Properties props, EmailData emailData) {
        EmailRequestValidator.validarEmailRequest(props, emailData);
        log.debug("Validación de Properties y EmailData completada.");
    }

    /**
     * Enviar el email.
     * @param props Propiedades asociadas
     * @param emailData Datos del email
     */
    private static void enviar(Properties props, EmailData emailData) {

        EmailSender emailSender = new EmailSenderImpl();
        EmailService emailService = new EmailServiceImpl(emailSender);

        emailService.sendEmail(props, emailData);
        log.debug("Correo enviado correctamente a {}", emailData.to());
    }

    /**
     * Muestra el valor de la cadena titulo en el log de una manera significativa
     * @param titulo Valor a mostrar
     */
    public static void imprimirTitulo(String titulo) {
        if (titulo != null && !titulo.isBlank()) {
            int longitudTitulo = titulo.length();
            String separador = "=".repeat(longitudTitulo);

            log.info(separador);
            log.info(titulo);
            log.info(separador);
        } else {
            log.warn("Título nulo o vacío en imprimirTitulo()");
        }
    }

    private static String getRequiredEnv(String name) {
        String value = System.getenv(name);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                "Debe configurar la variable de entorno obligatoria: " + name
            );
        }

        return value;
    }
}
