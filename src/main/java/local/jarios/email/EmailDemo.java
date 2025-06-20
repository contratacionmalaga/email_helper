package local.jarios.email;

import local.jarios.email.api.EmailService;
import local.jarios.email.api.EmailServiceImpl;
import local.jarios.email.exception.EmailServiceException;
import local.jarios.email.helper.EmailHelper;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * Clase principal para probar la API de envío de correos..
 */
@Slf4j
public class EmailDemo {

    /**
     * Mensaje que indica el inicio de la ejecución del programa.
     */
    public static final String INICIO = "**** Inicio del log";

    /**
     * Mensaje que indica el inicio de la ejecución del programa.
     */
    public static final String FINAL = "**** Final del log";

    /**
     * Mensaje que indica el inicio de la ejecución del programa.
     */
    public static final String FINAL_ERRONEO = "Error";

    /**
     * Mensaje que indica el inicio de la ejecución del programa.
     */
    public static final String FINAL_CORRECTO = "La ejecución del aplicativo ha finalizado correctamente";

    /**
     * Constructor por defecto.
     * Esta clase solo contiene el método main, no se debe instanciar.
     */
    private EmailDemo() {
        // Constructor vacío
    }

    /**
     * Método principal para ejecutar el proceso de cifrado y descifrado de datos.
     * Este método actúa como punto de entrada del programa.
     *
     * @param args Argumentos de línea de comandos (opcional).
     */
    public static void main(String[] args) {

        log.info(INICIO);

        try {

            Properties properties = new Properties();
            log.info("Creación del objeto Properties correctamente.");

            // Agrego las properties de
            properties.setProperty("mail.smtp.host", "correo.malaga.es");
            log.info("Asignación de propiedad 'mail.smtp.host' correctamente.");

            properties.setProperty("mail.smtp.auth", "true");
            log.info("Asignación de propiedad 'mail.smtp.auth' correctamente.");

            properties.setProperty("mail.smtp.port", "587");
            log.info("Asignación de propiedad 'mail.smtp.port' correctamente.");

            properties.setProperty("mail.smtp.socketFactory", "587");
            log.info("Asignación de propiedad 'mail.smtp.socketFactory' correctamente.");

            properties.setProperty("mail.smtp.checkserveridentity", "true");
            log.info("Asignación de propiedad 'mail.smtp.checkserveridentity' correctamente.");

            properties.setProperty("mail.smtp.protocols", "TLSv1.2");
            log.info("Asignación de propiedad 'mail.smtp.protocols' correctamente.");

            properties.setProperty("mail.smtp.trust", "correo.malaga.es");
            log.info("Asignación de propiedad 'mail.smtp.trust' correctamente.");

            properties.setProperty("mail.smtp.starttls.enable", "true");
            log.info("Asignación de propiedad 'mail.smtp.starttls.enable' correctamente.");

            properties.setProperty("mail.smtp.user", "incidenciascontratacion@malaga.es");
            log.info("Asignación de propiedad 'mail.smtp.user' correctamente.");

            properties.setProperty("mail.smtp.password", "BWFZHGPIJKXVNSLU");
            log.info("Asignación de propiedad 'mail.smtp.password' correctamente.");

            String from = "incidenciascontratacion@malaga.es";
            log.info("Asignación de la cuenta desde la que se enviará el correo.");

            String to = "jarios@malaga.es";
            log.info("Asignación las cuentas que recibirán el correo (lista separada por comas).");

            EmailService emailService = new EmailServiceImpl();
            log.info("Creación del servicio EmailService correctamente.");

            String asunto = EmailHelper.getAsunto("email_helper", "1.6.0", "localhost", true);
            log.info("Creación del asunto asociado al correo: {}.", asunto);

            String cuerpo =
                    EmailHelper.getCabeceraHtml() +
                    EmailHelper.getHead() +
                    EmailHelper.getCabeceraBody("Mensaje de prueba") +
                    EmailHelper.getInicioTable() +
                    EmailHelper.getFila("Key", "Value") +
                    EmailHelper.getPieTable() +
                    EmailHelper.getPieBody() +
                    EmailHelper.getPieHtml();

            emailService.enviarEmail(
                    properties,
                    from,
                    to,
                    asunto,
                    cuerpo);
            log.info("Correo enviado correctamente.");

            finalizar (FINAL_CORRECTO, 0);

        } catch (EmailServiceException e) {

            log.error("Error al obtener la versión del fichero.");
            finalizar (FINAL_ERRONEO, 1);

        }
    }

    /**
     * Finaliza la ejecución del programa mostrando un mensaje de log
     * y llamando a System.exit con el código proporcionado.
     *
     * @param mensaje  Mensaje que se mostrará en el log.
     * @param exitCode Código de salida del sistema:
     *                 0 para éxito, 1 para error. Otros valores también serán aceptados.
     */
    public static void finalizar(String mensaje, int exitCode) {
        if (exitCode == 0) {
            log.info(mensaje);
        } else {
            log.error("{} (Código de salida: {})", mensaje, exitCode);
        }

        log.info(FINAL); // Se asume que FINAL es una constante tipo String
        System.exit(exitCode);
    }
}
