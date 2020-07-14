package org.komeiji.commands.miscellaneous;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.komeiji.resources.Functions;
import org.komeiji.resources.Safe;

import java.io.IOException;

class result {
    class header {
        String similarity;
        String thumbnail;
        int index_id;
        String index_name;
    }

    class data {
        String[] ext_urls;

    }
}

public class SourceFinder {
    public static void source(String url) throws IOException {
        String search = "https://saucenao.com/search.php?api_key=" + Safe.SauceNAOKey + "db=107221536&output_type=2&testmode=1&numres=16&url=" + url;
        System.out.println(search);
        String json = Functions.toString(search);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject results = gson.fromJson(json, JsonObject.class);

        for (JsonElement element : results.getAsJsonArray("results"))
            System.out.println(element);
    }
}
