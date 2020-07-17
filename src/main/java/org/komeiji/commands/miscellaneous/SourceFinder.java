package org.komeiji.commands.miscellaneous;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.komeiji.resources.Functions;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

class Tag {
    String id;
    String type;
    String name;
    String url;
    int count;
}

class Result {
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

class Doujin {
    private String id;
    private String media_id;

    static class Tag {
        String id;
        String type;
        String name;
        String url;
        int count;
    }

    private List<Tag> tags;

    public static class title {
        private String english;
        private String japanese;
        private String pretty;
    }

    public String getId() {
        return this.id;
    }

    public String getMediaId() {
        return this.media_id;
    }

    public List<Tag> getTags() {
        return this.tags;
    }
}

public class SourceFinder {
    static void g(Message m) {
        if (m.getContentDisplay().split(" ").length == 2)
            try {
                String code = m.getContentDisplay().split(" ")[1];

                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new InputStreamReader(new URL("https://nhentai.net/api/gallery/" + code).openStream()));
                Doujin doujin = gson.fromJson(reader, Doujin.class);

                String tags;
                String artists;

                

                for (Doujin.Tag tag : doujin.getTags()) {

                }

                EmbedBuilder suki = new EmbedBuilder();
                suki.setTitle("");
            } catch (NumberFormatException | MalformedURLException ex) {
                m.getChannel().sendMessage("You didn't write six numbers did you?").queue();
            } catch (IOException e) {
                e.printStackTrace();
            }
        else m.getChannel().sendMessage("`!g` and six numbers please.").queue();
    }

    static void source(Message m) throws IOException {
        m.getChannel().sendMessage("Nope, not implemented yet.").queue();

//        String url = null;
//        if (!m.getAttachments().isEmpty())
//            url = m.getAttachments().get(0).getUrl();
//        else if (m.getContentDisplay().split(" ").length == 2)
//            try {
//                new URL(m.getContentDisplay().split(" ")[1]);
//                url = m.getContentDisplay().split(" ")[1];
//            } catch (MalformedURLException ex) {
//                m.getChannel().sendMessage("Your url doesn't work.").queue();
//            }
//        else
//            m.getChannel().sendMessage("`!source` and then a url. Or attach an image or something.").queue();
//
//        String search = "https://saucenao.com/search.php?api_key=" + safe.SAUCENAOKEY + "db=107221536&output_type=2&testmode=1&numres=16&url=" + url;
//        System.out.println(search);
//        String json = Functions.toString(search);
//
//        Gson gson = new Gson();
//        JsonObject results = gson.fromJson(json, JsonObject.class);
//
//        for (JsonElement element : results.getAsJsonArray("results"))
//            System.out.println(element);
    }
}
