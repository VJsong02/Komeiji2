package org.komeiji.commands.miscellaneous;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.entities.Message;
import org.komeiji.resources.Functions;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.komeiji.main.Main.safe;

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
        String url = null;
        if (!m.getAttachments().isEmpty())
            url = m.getAttachments().get(0).getUrl();
        else if (m.getContentDisplay().split(" ").length == 2)
            try {
                new URL(m.getContentDisplay().split(" ")[1]);
                url = m.getContentDisplay().split(" ")[1];
            } catch (MalformedURLException ex) {
                m.getChannel().sendMessage("Your url doesn't work.").queue();
            }
        else
            m.getChannel().sendMessage("`!source` and then a url. Or attach an image or something.").queue();

        String search = "https://saucenao.com/search.php?api_key=" + safe.SAUCENAOKEY + "db=107221536&output_type=2&testmode=1&numres=16&url=" + url;
        System.out.println(search);
        String json = Functions.toString(search);

        Gson gson = new Gson();
        JsonObject results = gson.fromJson(json, JsonObject.class);

        for (JsonElement element : results.getAsJsonArray("results"))
            System.out.println(element);
    }
}
