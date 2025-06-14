package local.jarios.email.config;

import local.jarios.email.exception.EmailException;
import local.jarios.email.utils.Constantes;
import local.jarios.encrypt.config.Decrypt;
import local.jarios.encrypt.exception.EncryptException;
import local.jarios.properties.config.PropertiesManager;
import local.jarios.properties.exception.PropertiesLoadException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase de configuración para el envío de correos SMTP.
 * <p>
 * Lee los parámetros de conexión desde un archivo `.properties`, descifrando
 * la contraseña si está en formato cifrado.
 * </p>
 *
 * Claves esperadas en el archivo de configuración:
 * <ul>
 *   <li><b>correo.smtp.host</b>: dirección del servidor SMTP</li>
 *   <li><b>correo.smtp.port</b>: puerto del servidor SMTP</li>
 *   <li><b>correo.smtp.usuario</b>: usuario de autenticación</li>
 *   <li><b>correo.smtp.clave</b>: clave del usuario (opcionalmente cifrada como ENC(...))</li>
 * </ul>
 *
 * @author Juan
 * @since 1.0
 */
@Getter
@Slf4j
public class CorreoConfig {



    private String host;
    private int port;
    private String user;
    private String password;

    /**
     * Carga la configuración de correo desde un archivo `.properties`.
     * Si la clave está cifrada en formato <code>ENC(valor)</code>,
     * se descifra con la clave maestra proporcionada.
     *
     * @param pathProperties ruta al archivo de propiedades
     * @param claveMaestra clave usada para descifrar la contraseña cifrada
     */
    public void cargarConfiguracion(String pathProperties, String claveMaestra) {
        log.debug("Cargando configuración de correo desde: {}", pathProperties);

        try {
            var propertyManager = PropertiesManager.getInstance();

            this.host = propertyManager.getProperty(Constantes.FICHERO_PROPERTIES, "mail.smtp.host");
            this.port = Integer.parseInt(propertyManager.getProperty(Constantes.FICHERO_PROPERTIES, "mail.smtp.port"));
            this.user = propertyManager.getProperty(Constantes.FICHERO_PROPERTIES, "mail.smtp.user");
            String claveCifrada = propertyManager.getProperty(Constantes.FICHERO_PROPERTIES, "mail.smtp.password");

            log.info("Configuración cargada: host={}, port={}, usuario={}", host, port, user);

            if (claveCifrada != null && claveCifrada.startsWith("ENC(") && claveCifrada.endsWith(")")) {
                log.debug("Clave cifrada detectada. Procediendo a descifrar...");
                claveCifrada = claveCifrada.substring(4, claveCifrada.length() - 1);
                this.password = Decrypt.decrypt(claveCifrada, claveMaestra);
                log.info("Clave descifrada correctamente.");
            } else {
                this.password = claveCifrada;
                log.info("Clave cargada sin cifrado.");
            }

        } catch (PropertiesLoadException e) {
            log.error("Error al cargar el archivo de propiedades: {}", e.getMessage(), e);
            throw new EmailException("No se pudo cargar el archivo de propiedades de configuración de correo", e);
        } catch (EncryptException e) {
            log.error("Error al descifrar la clave SMTP: {}", e.getMessage(), e);
            throw new EmailException("No se pudo descifrar la clave SMTP", e);
        } catch (NumberFormatException e) {
            log.error("Puerto SMTP inválido en el archivo de propiedades: {}", e.getMessage(), e);
            throw new EmailException("Puerto SMTP inválido en la configuración de correo", e);
        } catch (Exception e) {
            log.error("Error inesperado al cargar la configuración del correo: {}", e.getMessage(), e);
            throw new EmailException("Error inesperado al cargar configuración SMTP", e);
        }
    }
}

