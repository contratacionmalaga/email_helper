package local.jarios.email;

import local.jarios.email.api.EmailSender;
import local.jarios.email.api.EmailSenderImpl;
import local.jarios.email.api.EmailService;
import local.jarios.email.api.EmailServiceImpl;
import local.jarios.email.enums.TipoFinalEjecucion;
import local.jarios.email.exception.EmailException;
import local.jarios.email.helper.ComunHelper;
import local.jarios.email.helper.EmailHelper;
import local.jarios.email.helper.FinalDelProgramaHelper;
import local.jarios.email.helper.TextHelper;
import local.jarios.email.model.EmailData;
import local.jarios.email.validator.EmailRequestValidator;
import lombok.extern.slf4j.Slf4j;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;

import static local.jarios.email.common.util.Constantes.*;

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

        log.info("==== Inicio de la ejecución del Email Helper ====");

        try {
            Properties mailProps = cargarPropertiesSMTP();
            log.info("Configuración SMTP cargada correctamente.");

            EmailData emailDataOk = construirEmailData(true);
            validar(mailProps, emailDataOk);
            enviar(mailProps, emailDataOk);

            EmailData emailDataError = construirEmailData(false);
            validar(mailProps, emailDataError);
            enviar(mailProps, emailDataError);

            FinalDelProgramaHelper.finalizar(TipoFinalEjecucion.CORRECTO);

        } catch (EmailException | UnknownHostException ex) {
            log.error("Error durante la ejecución del Email Helper.", ex);
            FinalDelProgramaHelper.finalizar(TipoFinalEjecucion.ERROR);
        }

        log.info("==== Fin de la ejecución del Email Helper ====");
    }

    /* ===================== */
    /* MÉTODOS PRIVADOS */
    /* ===================== */

    private static Properties cargarPropertiesSMTP() {

        Properties props = new Properties();

        Map<String, String> propMap = Map.of(
            SMTP_HOST, "correo.malaga.es",
            SMTP_AUTH, "true",
            SMTP_PORT, "587",
            SMTP_SOCKETFACTORY_PORT, "587",
            SMTP_CHECKSERVERIDENTITY, "true",
            SMTP_PROTOCOLS, "TLSv1.2",
            SMTP_TRUST, "correo.malaga.es",
            SMTP_STARTTLS_ENABLE, "true",
            SMTP_USER, "incidenciascontratacion@malaga.es",
            SMTP_PASSWORD, "******" // ⚠ nunca loguear la real
        );

        propMap.forEach(props::setProperty);

        log.debug("Propiedades SMTP inicializadas ({} claves).", propMap.size());
        return props;
    }

    private static EmailData construirEmailData(boolean estadistica) throws UnknownHostException {

        String from = "incidenciascontratacion@malaga.es";
        String to = "jarios@malaga.es";

        String hostname = ComunHelper.getHostName();
        String subject = EmailHelper.getAsunto("email_helper", "VERSION_PRUEBA", hostname, true);

        log.info("Asunto del correo generado: {}", subject);

        String body;

        if (estadistica) {
            body = EmailHelper.getCuerpoEstadistica(new String[][]{
                {"Item [0]", "Valor [0]"},
                {"Item [1]", "Valor [1]"}
            });
        } else {
            body = EmailHelper.getCuerpoExcepcion(new String[]{
                "Item [0]",
                "Item [1]"
            });
        }

        log.debug("Cuerpo del correo generado ({} caracteres).", body.length());
        log.trace("Preview del cuerpo: {}", TextHelper.recortar(body, TAMANO_MAXIMO));

        return new EmailData(from, to, subject, body);
    }

    private static void validar(Properties props, EmailData emailData) {
        EmailRequestValidator.validarEmailRequest(props, emailData);
        log.info("Validación de Properties y EmailData completada.");
    }

    private static void enviar(Properties props, EmailData emailData) {

        EmailSender emailSender = new EmailSenderImpl();
        EmailService emailService = new EmailServiceImpl(emailSender);

        emailService.sendEmail(props, emailData);
        log.info("Correo enviado correctamente a {}", emailData.to());
    }
}
