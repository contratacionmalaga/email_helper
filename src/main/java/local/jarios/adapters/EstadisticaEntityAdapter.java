package local.jarios.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import local.jarios.entity.EstadisticaEntity;

import java.lang.reflect.Type;

/**
 * Description: Juan
 * Author: juan
 * Date: 09/07/2024
 * Team: Juan
 */
public record EstadisticaEntityAdapter() implements JsonSerializer<EstadisticaEntity> {

    @Override
    public JsonElement serialize(
            EstadisticaEntity estadisticaEntity,
            Type typeOfSrc,
            JsonSerializationContext context) {

        ///
        var jsonObject = new JsonObject();

        ///
        jsonObject.addProperty(
                "Id", String.valueOf(estadisticaEntity.getId()));

        ///
        jsonObject.addProperty(
                "nTotalFicheros", String.valueOf(estadisticaEntity.getNTotalFicheros()));

        ///
        jsonObject.addProperty(
                "nTotalProcesados", String.valueOf(estadisticaEntity.getNTotalProcesados()));

        ///
        jsonObject.addProperty(
                "nRregistrosGc", String.valueOf(estadisticaEntity.getNRregistrosGc()));

        ///
        jsonObject.addProperty(
                "fechaHoraInicial", String.valueOf(estadisticaEntity.getFechaHoraInicial()));

        ///
        jsonObject.addProperty(
                "fechaHoraFinal", String.valueOf(estadisticaEntity.getFechaHoraFinal()));

        ///
        jsonObject.addProperty(
                "duracion", estadisticaEntity.getDuracion());

        ///
        return jsonObject;
    }
}
