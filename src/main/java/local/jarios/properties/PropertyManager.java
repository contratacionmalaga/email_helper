package local.jarios.properties;

import local.jarios.enums.PropertyFile;
import local.jarios.utils.ConstantesGenerales;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class PropertyManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyManager.class);

    /// Lista de claves sensibles que no se deben mostrar
    private static final String[] SENSITIVE_KEYS = {
            PropertyConstantes.HIBERNATE_PRINCIPAL_USERNAME,
            PropertyConstantes.HIBERNATE_PRINCIPAL_PASSWORD,
            PropertyConstantes.EMAIL_USER,
            PropertyConstantes.EMAIL_PASSWORD
    };

    private static final String[] CONFIG_FILE = {
            PropertyFile.PROPERTY_CONFIG.getRuta(),
            PropertyFile.PROPERTY_EMAIL.getRuta(),
            PropertyFile.PROPERTY_HIBERNATE.getRuta()
    };

    /// Variable para almacenar las propiedades
    private final Map<String, String> configuraciones = new HashMap<>();

    /// Instancia única de la clase (Singleton)
    private static PropertyManager instance;

    /// Constructor que carga múltiples archivos de configuración
    public PropertyManager() {

        ///
        for (String filePath : CONFIG_FILE) {

            ///
            try (var inputStream = new FileInputStream(filePath)) {

                /// Carga las propiedades desde el archivo
                Properties properties = new Properties();
                properties.load(inputStream);

                /// Almacena las propiedades en el Map
                almacenarPropiedades(properties);

            } catch (IOException ex) {

                var mensaje = "Error al cargar el archivo de propiedades: " + filePath  + ". Error: " + ex.getMessage();
                LOGGER.error(mensaje);
                throw new PropertyFileException(mensaje, ex);

            }
        }
    }

    public Properties getProperties() {

        var properties = new Properties();

        /// Convertir el Map a Properties
        for (Map.Entry<String, String> entry : configuraciones.entrySet()) {
            properties.setProperty(entry.getKey(), entry.getValue());
        }

        return properties;
    }

    /// Almacena las propiedades en el mapa de configuraciones
    private void almacenarPropiedades(Properties properties) {
        properties.forEach((key, value) -> configuraciones.put(key.toString(), value.toString()));
    }

    ///
    ///     PATRÓN SINGLETON
    ///
    public static PropertyManager getInstance() {

        ///
        if (instance == null) {
            instance = new PropertyManager();
        }

        ///
        return instance;
    }

    /**
     *
     * @param propertyName Nombre de la propiedad que voy a obtener
     * @return String con el valor de la propiedad extraído del fichero de config
     */
    public String getProperty(String propertyName) {

        return configuraciones.getOrDefault(propertyName, "");
    }

    /**
     *
     */
    public void imprimirProperties () {

        /// Ordenar las claves al momento de imprimirlas (no modificamos el Map original)
        configuraciones.keySet().stream()
                .sorted()                           /// Ordenar alfabéticamente
                .forEach(key -> {
                    /// Solo imprimir si la clave no es sensible
                    if (!isSensitiveKey(key)) {
                        /// Imprimir la propiedad
                        imprimirPropiedad(key, configuraciones.get(key));
                    }
                });
    }

    /// Verificar si la clave es sensible
    private static boolean isSensitiveKey(String key) {

        ///
        for (String sensitiveKey : SENSITIVE_KEYS) {

            ///
            if (key.contains(sensitiveKey)) {

                ///
                return true;
            }
        }

        ///
        return false;
    }

    ///
    private static void imprimirPropiedad(Object key, Object value) {

        ///
        LOGGER.info("{}Propiedad leída: {} = {}", ConstantesGenerales.TABULADOR_1, key, value);
    }
}
