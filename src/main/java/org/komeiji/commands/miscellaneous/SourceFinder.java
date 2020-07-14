package org.komeiji.commands.miscellaneous;

import net.dv8tion.jda.api.entities.Message;

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
    public static void source(Message m) throws IOException {
//        String search = "https://saucenao.com/search.php?api_key=" + safe.get("SauceNAOKey") + "db=107221536&output_type=2&testmode=1&numres=16&url=" + url;
//        System.out.println(search);
//        String json = Functions.toString(search);
//
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        JsonObject results = gson.fromJson(json, JsonObject.class);
//
//        for (JsonElement element : results.getAsJsonArray("results"))
//            System.out.println(element);
    }
}
