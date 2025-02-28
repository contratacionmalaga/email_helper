package local.jarios.database;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.Configuration;

import java.util.Properties;


/**
 * Description:
 * Author: juan
 * Date: 28/12/2024
 * Team:
 */
@Slf4j
public class HibernateConfigurer {


    /**
     * Constructor que recibe Properties
     */
    public HibernateConfigurer() { }

    /**
     * Método que devuelve una configuración de Hibernate
     * @return Configuracion asociada a la conexión definida en el parámetro anterior
     */
    public Configuration buildConfiguration(Properties hibernateProperties) {
        Configuration configuration = new Configuration();

        configuration.setProperties(hibernateProperties);
        return configuration;
    }

}
