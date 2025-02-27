package local.jarios.managers;

import com.google.gson.GsonBuilder;
import local.jarios.adapters.EstadisticaEntityAdapter;
import local.jarios.adapters.FicheroGcEntityAdapter;
import local.jarios.adapters.LogEntityAdapter;
import local.jarios.entity.EstadisticaEntity;
import local.jarios.entity.FicheroGcEntity;
import local.jarios.entity.LogEntity;

public class ManagerGsons {

    private ManagerGsons() {}

    public static String objectToJsonPretty(Object objeto) {

        return getJson (objeto);

    }

    private static String getJson (Object objeto) {

        var gsonBuilder = new GsonBuilder();

        gsonBuilder.setPrettyPrinting().disableHtmlEscaping();

        /// EstadisticaEntityAdapter
        gsonBuilder.registerTypeAdapter(EstadisticaEntity.class, new EstadisticaEntityAdapter());

        /// LogEntityAdapter
        gsonBuilder.registerTypeAdapter(LogEntity.class, new LogEntityAdapter());

        /// HistoricoOcEntityAdapter
        gsonBuilder.registerTypeAdapter(FicheroGcEntity.class, new FicheroGcEntityAdapter());

        ///
        var gson = gsonBuilder.create();

        ///
        return gson.toJson(objeto);
    }
}
