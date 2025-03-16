package local.jarios.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import local.jarios.entity.FicheroGc;

import java.lang.reflect.Type;

/**
 * Description: Juan
 * Author: juan
 * Date: 09/07/2024
 * Team: Juan
 */
public record FicheroGcEntityAdapter() implements JsonSerializer<FicheroGc> {

    @Override
    public JsonElement serialize(
            FicheroGc ficheroGcEntity,
            Type typeOfSrc,
            JsonSerializationContext context) {

        ///
        var jsonObject = new JsonObject();

        ///
        jsonObject.addProperty(
                "Id", String.valueOf(ficheroGcEntity.getId()));

        ///
        jsonObject.addProperty(
                "shortName", String.valueOf(ficheroGcEntity.getCanonicalUri()));

        ///
        jsonObject.addProperty(
                "longName", String.valueOf(ficheroGcEntity.getLongName()));

        ///
        jsonObject.addProperty(
                "version", String.valueOf(ficheroGcEntity.getVersion()));

        ///
        jsonObject.addProperty(
                "canonicalUri", String.valueOf(ficheroGcEntity.getCanonicalUri()));

        ///
        jsonObject.addProperty(
                "canonicalVersionUri", String.valueOf(ficheroGcEntity.getCanonicalVersionUri()));

        ///
        jsonObject.addProperty(
                "locationUri", String.valueOf(ficheroGcEntity.getLocationUri()));

        ///
        return jsonObject;
    }
}
