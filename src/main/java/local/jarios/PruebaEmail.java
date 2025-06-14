package local.jarios;

import local.jarios.email.config.CorreoConfig;
import local.jarios.email.exception.EmailException;
import local.jarios.email.servicio.CorreoService;
import local.jarios.email.servicio.CorreoServiceImpl;
import local.jarios.email.utils.Constantes;
import local.jarios.properties.config.PropertiesManager;
import local.jarios.properties.exception.PropertiesLoadException;
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
public class PruebaEmail {

    public static void main(String[] args) {
        try {
            log.info("Iniciando prueba de envío de correo...");

            // Obtiene el singleton para propiedades
            var propertyManager = PropertiesManager.getInstance();

            // === Configuración inicial ===
            Set<String> clavesSensibles = Set.of("password");
            propertyManager.setSensitiveKeys(clavesSensibles);  // Ahora se aplica sobre la instancia

            // Listado de todos los ficheros cargados
            log.info("=== LISTA DE FICHEROS CARGADOS ===");
            propertyManager.getAllProperties().keySet().forEach(file ->
                    log.info("Fichero cargado: {}", file));

            // Obtener y mostrar las propiedades de un fichero específico
            log.info("=== PROPIEDADES DE TODOS LOS FICHEROS ===");
            propertyManager.printAllProperties();

            // Ruta al archivo properties
            String rutaProperties = "config";

            // Obtiene clave maestra desde variable de entorno
            String claveMaestra = "Malaga$$2025";

            // Carga configuración de correo
            CorreoConfig config = new CorreoConfig();

            try {
                config.cargarConfiguracion(rutaProperties, claveMaestra);
                log.debug("Configuración de correo cargada.");
                propertyManager.printProperties(Constantes.FICHERO_PROPERTIES);
            } catch (PropertiesLoadException ple) {
                log.error("No se pudo cargar la configuración del fichero properties: {}", ple.getMessage(), ple);
                // Aquí decides si quieres abortar o continuar con valores por defecto
                return; // Salimos porque la configuración es crítica
            }

            // Crea el servicio de correo
            CorreoService correoService = new CorreoServiceImpl(config);

            // Parámetros de correo
            String remitente = config.getUser(); // o un remitente específico
            List<String> destinatarios = List.of(propertyManager.getProperty("email", "mail.to"));
            String asunto = "Prueba de correo";
            String cuerpo = "Este es un correo enviado desde la app con clave cifrada";

            // Envía el correo
            try {
                correoService.enviarCorreo(remitente, destinatarios, asunto, cuerpo);
            } catch (EmailException ee) {
                log.error("Error al enviar el correo: {}", ee.getMessage(), ee);
            }

            log.info("Correo enviado correctamente.");

        } catch (Exception  e) {
            log.error("Error inesperado durante la prueba de envío de correo: {}", e.getMessage(), e);
        }
    }
}

