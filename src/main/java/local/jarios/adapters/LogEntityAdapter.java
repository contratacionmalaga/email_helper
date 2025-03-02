package local.jarios.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import local.jarios.entity.LogEntity;

import java.lang.reflect.Type;

/**
 * Description: Juan
 * Author: juan
 * Date: 09/07/2024
 * Team: Juan
 */
public record LogEntityAdapter() implements JsonSerializer<LogEntity> {

    @Override
    public JsonElement serialize(LogEntity logEntity, Type typeOfSrc, JsonSerializationContext context) {

        /// Crear el objeto principal que será "Log"
        JsonObject jsonObject = new JsonObject();

        /// Crear un objeto para el contenido de "Log"
        JsonObject logContent = new JsonObject();

        ///
        logContent.addProperty("Id", String.valueOf(logEntity.getId()));

        ///
        if (logEntity.getEstadisticaEntity() != null) {

            ///
            logContent.add("Estadistica", context.serialize(logEntity.getEstadisticaEntity()));

        }

        ///
        if (!logEntity.getFicherosGcEntity().isEmpty()) {

            ///
            logContent.add("FicheroGc", context.serialize(logEntity.getFicherosGcEntity()));

        }

        /// Agregar el objeto "Log" que contendrá todos los datos anteriores
        jsonObject.add("Log", logContent);

        ///
        return jsonObject;
    }
}
