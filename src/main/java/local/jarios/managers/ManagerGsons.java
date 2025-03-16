package local.jarios.managers;

import com.google.gson.GsonBuilder;
import local.jarios.adapters.EstadisticaEntityAdapter;
import local.jarios.adapters.FicheroGcEntityAdapter;
import local.jarios.adapters.LogEntityAdapter;
import local.jarios.entity.Estadistica;
import local.jarios.entity.FicheroGc;
import local.jarios.entity.Log;

public class ManagerGsons {

    private ManagerGsons() {}

    public static String objectToJsonPretty(Object objeto) {

        return getJson (objeto);

    }

    private static String getJson (Object objeto) {

        var gsonBuilder = new GsonBuilder();

        gsonBuilder.setPrettyPrinting().disableHtmlEscaping();

        /// EstadisticaEntityAdapter
        gsonBuilder.registerTypeAdapter(Estadistica.class, new EstadisticaEntityAdapter());

        /// LogEntityAdapter
        gsonBuilder.registerTypeAdapter(Log.class, new LogEntityAdapter());

        /// HistoricoOcEntityAdapter
        gsonBuilder.registerTypeAdapter(FicheroGc.class, new FicheroGcEntityAdapter());

        ///
        var gson = gsonBuilder.create();

        ///
        return gson.toJson(objeto);
    }
}
