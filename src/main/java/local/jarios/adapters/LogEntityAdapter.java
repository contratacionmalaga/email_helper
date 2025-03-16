package local.jarios.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import local.jarios.entity.Log;

import java.lang.reflect.Type;

/**
 * Description: Juan
 * Author: juan
 * Date: 09/07/2024
 * Team: Juan
 */
public record LogEntityAdapter() implements JsonSerializer<Log> {

    @Override
    public JsonElement serialize(Log logEntity, Type typeOfSrc, JsonSerializationContext context) {

        /// Crear el objeto principal que será "Log"
        JsonObject jsonObject = new JsonObject();

        /// Crear un objeto para el contenido de "Log"
        JsonObject logContent = new JsonObject();

        ///
        logContent.addProperty("Id", String.valueOf(logEntity.getId()));

        ///
        if (logEntity.getEstadistica() != null) {

            ///
            logContent.add("Estadistica", context.serialize(logEntity.getEstadistica()));

        }

        ///
        if (!logEntity.getFicherosGc().isEmpty()) {

            ///
            logContent.add("FicheroGc", context.serialize(logEntity.getFicherosGc()));

        }

        /// Agregar el objeto "Log" que contendrá todos los datos anteriores
        jsonObject.add("Log", logContent);

        ///
        return jsonObject;
    }
}
