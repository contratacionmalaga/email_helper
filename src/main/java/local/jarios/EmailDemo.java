package local.jarios;

import local.jarios.email.config.EmailConfig;
import local.jarios.email.dominio.EmailMensaje;
import local.jarios.email.exception.EmailException;
import local.jarios.email.servicio.EmailServiceImpl;
import local.jarios.email.utils.Constantes;
import local.jarios.properties.config.PropertiesManager;
import local.jarios.properties.exception.PropertiesLoadException;
import local.jarios.versionfrommanifest.service.VersionFromManifestServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

/**
 * Clase principal para probar el envío de correos electrónicos.
 * <p>
 * Carga la configuración desde un fichero properties, crea el servicio de correo
 * y envía un mensaje de prueba a una lista de destinatarios.
 * </p>
 *
 * <p>Recuerda nunca guardar claves en código, se recomienda obtener la clave maestra
 * desde una variable de entorno u otro mecanismo seguro.</p>
 *
 * <p>Autor: Juan Antonio</p>
 * <p>Fecha: 04/06/2024</p>
 */
@Slf4j
public class EmailDemo {

    /**
     * Clave maestra
     */
    private final static String CLAVE_MAESTRA = "Malaga$$2025";

    /**
     * Clave maestra
     */
    private final static String ASUNTO = "Asunto de ejemplo";

    /**
     * Clave maestra
     */
    private final static String MENSAJE = "Cuerpo de ejemplo";


    /**
     * Mensaje que indica el inicio de la ejecución del programa.
     */
    public static final String INICIO = "**** Inicio del log";

    /**
     * Mensaje que indica el inicio de la ejecución del programa.
     */
    public static final String FINAL = "**** Final del log";


    /**
     * Constructor vacío
     */
    private EmailDemo() {
        // Constructor vacío
    }

    /**
     * Demostración del módulo
     */
    public static void main() {

        log.info(INICIO);

        try {

            // Obtiene el singleton para propiedades
            var propertiesManager = PropertiesManager.getInstance();
            log.info("Creado el objeto PropertiesManager correctamente.");

            var versionFromManifestService = new VersionFromManifestServiceImpl();
            log.info("Creado el objeto VersionFromManifestService correctamente.");

            String appName = propertiesManager.getProperty(Constantes.CONFIG_PROPERTIES, "config.name");
            log.info("AppName: {}", appName);

            String appVersion = versionFromManifestService.getVersion(EmailDemo.class);
            log.info("AppVersion: {}", appVersion);

            // === Configuración inicial ===
            Set<String> clavesSensibles = Set.of("password");
            propertiesManager.setSensitiveKeys(clavesSensibles);  // Ahora se aplica sobre la instancia
            log.info("Establezco el conjunto de claves Sensibles: {}", clavesSensibles);

            // Listado de todos los ficheros cargados
            log.info("=== LISTA DE FICHEROS CARGADOS ===");
            propertiesManager.getAllProperties().keySet().forEach(file -> log.info("Fichero cargado: {}", file));

            // Obtener y mostrar las propiedades de un fichero específico
            log.info("=== PROPIEDADES DE TODOS LOS FICHEROS ===");
            propertiesManager.printAllProperties();

            // Carga configuración de correo
            EmailConfig emailConfig = new EmailConfig();
            log.info("Creado el objeto EmailConfig correctamente.");


            try {
                emailConfig.cargarConfiguracion(Constantes.EMAIL_PROPERTIES, CLAVE_MAESTRA);
                log.debug("Configuración de correo cargada correctamente.");
                propertiesManager.printProperties(Constantes.EMAIL_PROPERTIES);
            } catch (PropertiesLoadException ple) {
                log.error("No se pudo cargar la configuración del fichero properties: {}", ple.getMessage(), ple);
                // Aquí decides si quieres abortar o continuar con valores por defecto
                return; // Salimos porque la configuración es crítica
            }

            // Crea el servicio de correo
            var emailService = new EmailServiceImpl(emailConfig);
            log.info("Creado el objeto EmailService correctamente.");

            String asuntoFinal = String.format("[%s - v%s] %s", appName, appVersion, ASUNTO);

            // Crea el objeto EmailMensaje
            var emailMensaje = new EmailMensaje(
                    emailConfig.getUser(),
                    List.of(propertiesManager.getProperty(Constantes.EMAIL_PROPERTIES, "mail.to")),
                    asuntoFinal,
                    MENSAJE);
            log.info("Creado el objeto EmailMensaje correctamente.");

            // Envía el correo
            try {
                emailService.enviarCorreo(emailMensaje);
            } catch (EmailException ee) {
                log.error("Error al enviar el correo: {}", ee.getMessage(), ee);
            }

            log.info("Correo enviado correctamente.");

        } catch (PropertiesLoadException e) {

            log.error("Error al obtener la versión del fichero.");
            throw e; // <- Repropagar al consumidor del módulo

        } catch (EmailException e) {

            log.error("Error al intentar enviar el correo.");
            throw e; // <- Repropagar al consumidor del módulo

        } finally {

            log.info(FINAL);
        }
    }
}

