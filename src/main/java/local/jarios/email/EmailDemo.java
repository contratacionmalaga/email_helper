package local.jarios.email;

import local.jarios.email.api.EmailSender;
import local.jarios.email.api.EmailService;
import local.jarios.email.api.EmailServiceImpl;
import local.jarios.email.api.EmailSenderImpl;
import local.jarios.email.exception.EmailException;
import local.jarios.email.helper.ComunHelper;
import local.jarios.email.helper.EmailHelper;
import local.jarios.email.helper.TextHelper;
import local.jarios.email.model.EmailData;
import local.jarios.email.validator.EmailRequestValidator;
import lombok.extern.slf4j.Slf4j;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;

import static local.jarios.email.common.util.Constantes.*;

/**
 * Clase principal para ejecutar una demostración del envío de correos electrónicos
 * utilizando la API definida en el proyecto.
 * <p>
 * Este programa carga la configuración SMTP, construye el contenido del correo
 * y lo envía a través de {@link EmailService}.
 * </p>
 * <p>
 * Esta clase está diseñada únicamente como punto de entrada de prueba o demostración,
 * por lo que contiene un método {@code main} con ejecución secuencial.
 * </p>
 *
 * @author Juan
 * @version 1.0
 */
@Slf4j
public final class EmailDemo {

    /** Mensaje de log al inicio de la ejecución del programa. */
    private static final String INICIO = "**** Inicio del log";

    /** Mensaje de log al final de la ejecución del programa. */
    private static final String FINAL = "**** Final del log";

    /** Mensaje de log cuando la ejecución termina con error. */
    private static final String FINAL_ERRONEO = "Error";

    /** Mensaje de log cuando la ejecución finaliza correctamente. */
    private static final String FINAL_CORRECTO = "La ejecución del aplicativo ha finalizado correctamente";

    /**
     * Constructor privado para evitar la instanciación de esta clase de utilidad.
     */
    private EmailDemo() {
        // No instanciable
    }

    /**
     * Método principal que actúa como punto de entrada de la aplicación.
     * <p>
     * Carga la configuración SMTP, construye el contenido del email y
     * realiza el envío utilizando {@link EmailService}.
     * </p>
     *
     * @param args Argumentos de línea de comandos (no utilizados en esta implementación).
     */
    public static void main(String[] args) {

        log.info(INICIO);

        try {
            // Configuración del servidor SMTP
            Properties mailProps = cargarPropertiesSMTP();
            log.info("Properties cargadas correctamente.");

            // Construcción de los datos del correo
            EmailData emailData = construirEmailData(true);
            log.info("EmailData creado correctamente para estadísticas.");

            EmailRequestValidator.validarEmailRequest(mailProps, emailData);
            log.info("Properties e EmailData validados correctamente.");

            // Creación del servicio de correo con la implementación de envío SMTP
            EmailSender emailSender = new EmailSenderImpl();
            log.info("Creación del objeto EmailSender correctamente.");

            EmailService emailService = new EmailServiceImpl(emailSender);
            log.info("Creación del objeto EmailService correctamente.");

            // Envío del correo
            emailService.sendEmail(mailProps, emailData);
            log.info("Correo enviado correctamente.");

            // Construcción de los datos del correo
            emailData = construirEmailData(false);
            log.info("EmailData creado correctamente para excecpción.");

            // Envío del correo
            emailService.sendEmail(mailProps, emailData);
            log.info("Correo enviado correctamente.");

            finalizar(FINAL_CORRECTO, 0);

        } catch (EmailException ex) {

            log.error("Error en el envío del correo: {}", ex.getMessage(), ex);
            finalizar(FINAL_ERRONEO, 1);

        } catch (UnknownHostException ex) {

            finalizar(FINAL_ERRONEO, 1);

        }
    }

    /**
     * Carga las propiedades necesarias para configurar la conexión con el servidor SMTP.
     *
     * @return {@link Properties} con los parámetros necesarios para la autenticación y conexión.
     */
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
                SMTP_PASSWORD, "BWFZHGPIJKXVNSLU"
        );

        propMap.forEach((clave, valor) -> {
            props.setProperty(clave, valor);
            log.info("Asignación de propiedad '{}' correctamente.", clave);
        });

        return props;
    }

    /**
     * Construye el objeto {@link EmailData} con los datos necesarios para el correo de prueba.
     * <p>
     * Utiliza utilidades de {@link EmailHelper} para construir el asunto y el cuerpo del mensaje
     * en formato HTML.
     * </p>
     *
     * @return Objeto {@link EmailData} completamente inicializado.
     */
    private static EmailData construirEmailData(boolean estadistica) throws UnknownHostException {
        String from = "incidenciascontratacion@malaga.es";
        String to = "jarios@malaga.es";

        try {
            String hostname = ComunHelper.getHostName();
            String subject = EmailHelper.getAsunto("email_helper", "VERSION_PRUEBA", hostname, true);
            log.info("Creación del asunto asociado al correo: {}.", subject);

            String body;

            if (estadistica) {

                String[][] datos = new String[2][2];
                datos[0][0] = "Item [0]";
                datos[0][1] = "Valor [0]";
                datos[1][0] = "Item [1]";
                datos[1][1] = "Valor [1]";
                body = EmailHelper.getCuerpoEstadistica(datos);

            } else {

                String[] datos = new String[2];
                datos[0] = "Item [0]";
                datos[1] = "Item [1]";
                body = EmailHelper.getCuerpoExcepcion(datos);

            }

            log.info("Cuerpo del correo generado correctamente. Body: {}", TextHelper.recortar(body, TAMANO_MAXIMO));

            return new EmailData(from, to, subject, body);

        } catch (UnknownHostException ex) {

            log.info(ex.getMessage());
            throw new UnknownHostException(ex.getMessage());

        }
    }


    /**
     * Finaliza la ejecución del programa, mostrando el mensaje de log correspondiente
     * y realizando una salida del sistema con el código indicado.
     *
     * @param mensaje  Mensaje a registrar en el log.
     * @param exitCode Código de salida para {@code System.exit}.
     *                 <ul>
     *                   <li>{@code 0}: Ejecución correcta</li>
     *                   <li>{@code 1}: Error general</li>
     *                   <li>Otros valores: definidos por el usuario</li>
     *                 </ul>
     */
    private static void finalizar(String mensaje, int exitCode) {
        if (exitCode == 0) {
            log.info(mensaje);
        } else {
            log.error("{} (Código de salida: {})", mensaje, exitCode);
        }

        log.info(FINAL);
        System.exit(exitCode);
    }
}
