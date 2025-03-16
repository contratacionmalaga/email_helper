package local.jarios.properties;

import local.jarios.enums.PropertyFile;
import local.jarios.enums.TipoFinalEjecucion;
import local.jarios.utils.ConstantesGenerales;
import local.jarios.utils.FinalDelPrograma;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.JdbcSettings;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Slf4j
public final class PropertyManager {

    /// Lista de claves sensibles que no se deben mostrar
    private static final String[] SENSITIVE_KEYS = {
            JdbcSettings.JAKARTA_JDBC_USER,
            JdbcSettings.JAKARTA_JDBC_PASSWORD,
            PropertyConstantes.EMAIL_USER,
            PropertyConstantes.EMAIL_PASSWORD
    };

    /// Instancia única de la clase (Singleton)
    private static PropertyManager instance;

    /// Variable para almacenar las propiedades
    private final Map<String, String> mapProperties = new HashMap<>();

    /// Variables que almacenarán las propiedades
    @Getter
    private final Properties configProperties = new Properties();
    @Getter
    private final Properties hibernateProperties = new Properties();
    @Getter
    private final Properties mailProperties = new Properties();
    @Getter
    private final Properties releaseProperties = new Properties();

    /**
     * Constructor privado para evitar la creación de instancias fuera de la clase.
     */
    private PropertyManager() {

        /// Obtener las rutas de todos los archivos de configuración desde el enum
        List<String> filePaths = PropertyFile.getAllFilePaths();

        /// Cargar las propiedades desde los archivos especificados en el enum
        Map<String, Properties> propertyFilesMap = new HashMap<>();

        ///
        propertyFilesMap.put(PropertyFile.PROPERTY_CONFIG.getRuta(), configProperties);
        propertyFilesMap.put(PropertyFile.PROPERTY_HIBERNATE.getRuta(), hibernateProperties);
        propertyFilesMap.put(PropertyFile.PROPERTY_MAIL.getRuta(), mailProperties);
        propertyFilesMap.put(PropertyFile.PROPERTY_RELEASE.getRuta(), releaseProperties);

        ///
        for (String filePath : filePaths) {

            ///
            Properties properties = propertyFilesMap.get(filePath);

            ///
            if (properties != null) {

                ///
                cargarArchivoPropiedades(properties, filePath);

            } else {

                if (log.isDebugEnabled()) {
                    log.warn("El fichero de propiedades {} no tiene properties.", filePath);
                }
            }
        }
    }

    /**
     * Método que carga un archivo de propiedades y las almacena en el mapa de configuraciones.
     *
     * @param properties El objeto Properties donde se cargarán las propiedades
     * @param filePath   Ruta del archivo de propiedades
     */
    private void cargarArchivoPropiedades(Properties properties, String filePath) {

        ///
        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            /// Carga las propiedades desde el archivo
            properties.load(inputStream);
            /// Almacena las propiedades en el Map
            almacenarPropiedades(properties);
        } catch (IOException ex) {
            /// Registro la excepción con información adicional
            log.error("Error al cargar el archivo de propiedades: {}. Detalles: {}", filePath, ex.getMessage());
            /// Finaliza la ejecución del programa
            FinalDelPrograma.finalizar(TipoFinalEjecucion.ERROR);
        }
    }

    /**
     * Almacena las propiedades en el mapa de configuraciones.
     *
     * @param properties El objeto Properties que contiene las propiedades a almacenar
     */
    private void almacenarPropiedades(Properties properties) {
        properties.forEach((key, value) -> mapProperties.put(key.toString(), value.toString()));
    }

    /**
     * Obtiene la instancia Singleton de la clase.
     *
     * @return La instancia única de PropertyManager
     */
    public static synchronized PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }

    /**
     * Obtiene el valor de una propiedad.
     *
     * @param propertyName Nombre de la propiedad que se desea obtener
     * @return El valor de la propiedad, o un mensaje indicando que no se encontró
     */
    public String getProperty(String propertyName) {
        return mapProperties.get(propertyName);
    }

    /**
     * Imprime las propiedades de forma ordenada, excluyendo las sensibles.
     */
    public void imprimirMapProperties() {
        /// Ordenar las claves al momento de imprimirlas (no modificamos el Map original)
        mapProperties.keySet().stream()
                .sorted()
                .forEach((String key) -> {
                    /// Solo imprimir si la clave no es sensible
                    if (!isSensitiveKey(key)) {
                        /// Imprimir la propiedad
                        imprimirPropiedad(key, mapProperties.get(key));
                    }
                });
    }

    /**
     * Verifica si la clave es sensible.
     *
     * @param key Clave a verificar
     * @return true si la clave es sensible, false en caso contrario
     */
    private static boolean isSensitiveKey(String key) {
        /// Comparar la clave con las claves sensibles
        for (String sensitiveKey : SENSITIVE_KEYS) {
            if (key.equals(sensitiveKey)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Imprime en el log el nombre de una propiedad y su valor.
     *
     * @param key   Nombre de la propiedad
     * @param value Valor de la propiedad
     */
    private static void imprimirPropiedad(Object key, Object value) {
        log.info("{}Propiedad leída: {} = {}", ConstantesGenerales.TABULADOR_1, key, value);
    }
}
