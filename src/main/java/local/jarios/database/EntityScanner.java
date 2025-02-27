package local.jarios.database;

import jakarta.persistence.Entity;
import local.jarios.utils.ConstantesGenerales;
import local.jarios.utils.Mensajes;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;

import java.util.Collection;

/**
 * Description:
 * Author: juan
 * Date: 28/12/2024
 * Team:
 */
@Slf4j
public class EntityScanner {

    public void scanAndAddEntities(
            Configuration configuration,
            String packageName) {

        ///
        var reflections = new Reflections(packageName);

        ///
        Collection<Class<?>> entities = reflections.getTypesAnnotatedWith(Entity.class);

        log.info(Mensajes.ENTIDADES, entities.size(), packageName);

        ///
        for (Class<?> entityClass : entities) {

            ///
            configuration.addAnnotatedClass(entityClass);

            ///
            log.info("{}{}", ConstantesGenerales.TABULADOR_1, entityClass.getName());
        }
    }
}
