package local.jarios;

import local.jarios.email.dominio.EmailMensaje;
import local.jarios.email.exception.EmailServiceException;
import local.jarios.email.servicio.EmailServiceImpl;
import local.jarios.email.utils.Constantes;
import local.jarios.properties.config.PropertiesManager;
import local.jarios.properties.exception.PropertiesLoadException;
import local.jarios.versionfrommanifest.exception.VersionFromManifestException;
import local.jarios.versionfrommanifest.service.VersionFromManifestServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Properties;
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

            // Obtener la instancia singleton
            PropertiesManager propertiesManager = PropertiesManager.getInstance();
            log.info("Instancia PropertiesManager obtenida correctamente.");

            // === Configuración inicial ===
            Set<String> clavesSensibles = Set.of("password");
            propertiesManager.setSensitiveKeys(clavesSensibles);  // Ahora se aplica sobre la instancia
            log.info("Establezco el conjunto de claves Sensibles: {}", clavesSensibles);

            // Cargar todas las propiedades desde el directorio de configuración
            propertiesManager.loadAllProperties(Constantes.CONFIG_DIR);
            log.info("Ficheros .properties cargados desde /{} correctamente", Constantes.CONFIG_DIR);

            var versionFromManifestService = new VersionFromManifestServiceImpl();
            log.info("Creado el objeto VersionFromManifestService correctamente.");

            String appName = propertiesManager.getProperty(Constantes.APP_PROPERTIES, "app.name");
            log.info("AppName: {}", appName);

            String appDescripcion = propertiesManager.getProperty(Constantes.APP_PROPERTIES, "app.descripcion");
            log.info("AppDescripcion: {}", appDescripcion);

            String appVersion = versionFromManifestService.getVersion(PropertiesDemo.class);
            log.info("AppVersion: {}", appVersion);

            String asuntoFinal = String.format("[%s - v%s] %s", appName, appVersion, ASUNTO);

            // Crea el objeto EmailMensaje
            var emailMensaje = new EmailMensaje(
                    propertiesManager.getProperty(Constantes.EMAIL_PROPERTIES, "mail.from"),
                    List.of(propertiesManager.getProperty(Constantes.EMAIL_PROPERTIES, "mail.to")),
                    asuntoFinal,
                    MENSAJE);
            log.info("Creado el objeto EmailMensaje correctamente.");

            // Creo el objeto EmailService
            var emailService = new EmailServiceImpl();

            //
            Properties properties = propertiesManager.getProperties(Constantes.EMAIL_PROPERTIES);
            if (log.isDebugEnabled()) {
                printProperties(properties);
            }

            //
            String user = propertiesManager.getProperty(Constantes.EMAIL_PROPERTIES, Constantes.KEY_USER);
            log.debug("User: {}", user);

            String password = propertiesManager.getProperty(Constantes.EMAIL_PROPERTIES, Constantes.KEY_PASSWORD);
            log.debug("Password: {}", password);

            // Envío el correo
            emailService.enviarEmail(properties, user, password, emailMensaje);
            log.info("Correo enviado correctamente.");

        } catch (EmailServiceException e) {

            log.error("Error al enviar el correo: {}", e.getMessage(), e);

        } catch (VersionFromManifestException e) {

            log.error("Error al obtener la versión del fichero JAR.", e);

        } catch (PropertiesLoadException e) {

            log.error("Error al cargar los ficheros properties.", e);

        } finally {

            log.info(FINAL);
        }
    }

    public static void printProperties(Properties props) {
        props.forEach((key, value) -> {
            log.info("{} = {}", key, value);
        });
    }
}

