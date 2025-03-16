package local.jarios.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import local.jarios.entity.Estadistica;

import java.lang.reflect.Type;

/**
 * Description: Juan
 * Author: juan
 * Date: 09/07/2024
 * Team: Juan
 */
public record EstadisticaEntityAdapter() implements JsonSerializer<Estadistica> {

    @Override
    public JsonElement serialize(
            Estadistica estadistica,
            Type typeOfSrc,
            JsonSerializationContext context) {

        ///
        var jsonObject = new JsonObject();

        ///
        jsonObject.addProperty(
                "Id", String.valueOf(estadistica.getId()));

        ///
        jsonObject.addProperty(
                "nTotalFicherosLeidos", String.valueOf(estadistica.getNTotalFicherosLeidos()));

        ///
        jsonObject.addProperty(
                "nTotalFicherosProcesados", String.valueOf(estadistica.getNTotalFicherosProcesados()));

        ///
        jsonObject.addProperty(
                "nRregistrosGc", String.valueOf(estadistica.getNRregistrosGc()));

        jsonObject.addProperty(
                "duraciónParseo",
                estadistica.getDuracionParseo());

        jsonObject.addProperty(
                "duraciónPersistenciaEnBaseDatos",
                estadistica.getDuracionBaseDatos());

        ///
        return jsonObject;
    }
}
