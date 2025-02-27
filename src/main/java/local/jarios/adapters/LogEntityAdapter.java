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

        ///
        var jsonObject = new JsonObject();

        ///
        jsonObject.addProperty("Id", String.valueOf(logEntity.getId()));

        ///
        if (logEntity.getEstadisticaEntity() != null) {

            ///
            jsonObject.add(
                    "Estadistica",
                    context.serialize(logEntity.getEstadisticaEntity()));

        }

        ///
        if (!logEntity.getFicherosGcEntity().isEmpty()) {

            ///
            jsonObject.add(
                    "FicheroGc",
                    context.serialize(logEntity.getFicherosGcEntity()));

        }

        ///
        return jsonObject;
    }
}
