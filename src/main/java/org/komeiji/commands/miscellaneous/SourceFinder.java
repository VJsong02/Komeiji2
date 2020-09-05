package org.komeiji.commands.miscellaneous;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import org.komeiji.main.Main;
import org.komeiji.resources.Functions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import static org.komeiji.main.Main.prefix;
import static org.komeiji.main.Main.safe;

class Results {
    private List<Result> results;

    public List<Result> getResults() {
        return this.results;
    }

    static class Result {
        private Header header;
        private Data data;

        public Header getHeader() {
            return header;
        }

        public Data getData() {
            return data;
        }

        public String getArtist() {
            if (this.data.member_name != null)
                return this.data.member_name;
            else return this.data.creator;
        }

        static class Header {
            private String similarity;
            private String thumbnail;
            private int index_id;
            private String index_name;

            public String getSimilarity() {
                return similarity;
            }

            public String getThumbnail() {
                return thumbnail;
            }

            public int getIndex_id() {
                return index_id;
            }

            public String getIndex_name() {
                return index_name;
            }
        }

        static class Data {
            private List<String> ext_urls;

            private int danbooru_id;
            private int gelbooru_id;
            private String source;
            private String creator;
            private String member_name;

            public List<String> getUrls() {
                return ext_urls;
            }

            public int getDanbooruId() {
                return this.danbooru_id;
            }

            public int getGelbooruId() {
                return this.gelbooru_id;
            }

            public String getSource() {
                return this.source;
            }

            public String getMemberName() {
                return this.member_name;
            }

            public String getCreator() {
                return this.creator;
            }
        }
    }
}

class Doujin {
    private String id;
    private String media_id;
    private Title title;
    private List<Tag> tags;
    private String error;

    public Title getTitle() {
        return this.title;
    }

    public boolean exists() {
        return !error.isEmpty();
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

    static class Tag {
        private String id;
        private String type;
        private String name;
        private String url;
        private int count;

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        public int getCount() {
            return count;
        }
    }

    static class Title {
        private String english;
        private String japanese;
        private String pretty;

        public String getEnglish() {
            return english;
        }

        public String getJapanese() {
            return japanese;
        }

        public String getPretty() {
            return pretty;
        }
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

                HashMap<String, StringBuilder> tags = new HashMap<>() {
                    {
                        put("parody", new StringBuilder());
                        put("character", new StringBuilder());
                        put("tag", new StringBuilder());
                        put("artist", new StringBuilder());
                        put("language", new StringBuilder());
                    }
                };

                for (Doujin.Tag tag : doujin.getTags())
                    if (tags.containsKey(tag.getType()))
                        tags.get(tag.getType()).append(tag.getName()).append(", ");

                EmbedBuilder suki = new EmbedBuilder().setColor(Main.clr);

                suki.setTitle("*" + doujin.getTitle().getPretty() + "*", "https://nhentai.net/g/" + doujin.getId());
                if (!tags.get("parody").toString().isEmpty())
                    suki.addField("Parodies", tags.get("parody").toString().replaceAll(", $", ""), true);
                if (!tags.get("character").toString().isEmpty())
                    suki.addField("Characters", tags.get("character").toString().replaceAll(", $", ""), true);
                if (!tags.get("tag").toString().isEmpty())
                    suki.addField("Tags", tags.get("tag").toString().replaceAll(", $", ""), false);
                if (!tags.get("artist").toString().isEmpty())
                    suki.addField("Artists", tags.get("artist").toString().replaceAll(", $", ""), true);
                if (!tags.get("language").toString().isEmpty())
                    suki.addField("Languages", tags.get("language").toString().replaceAll(", $", ""), true);

                if (m.getTextChannel().isNSFW())
                    suki.setThumbnail("https://t.nhentai.net/galleries/" + doujin.getMediaId() + "/cover.jpg");

                m.getChannel().sendMessage(suki.build()).queue();
            } catch (NumberFormatException | MalformedURLException ex) {
                m.getChannel().sendMessage("You didn't write six numbers did you?").queue();
            } catch (FileNotFoundException ex) {
                m.getChannel().sendMessage("In your dreams.").queue();
            } catch (IOException e) {
                e.printStackTrace();
            }
        else m.getChannel().sendMessage(prefix + "`g` and six numbers please.").queue();
    }

    static void source(Message m) throws IOException {
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
        else if (!m.getChannel().getHistory().retrievePast(2).complete().get(1).getAttachments().isEmpty()) {
            try {
                String prev = m.getChannel().getHistory().retrievePast(2).complete().get(1)
                        .getAttachments().get(0).getUrl();
                new URL(prev);
                url = prev;
                System.out.println(url);
            } catch (MalformedURLException ignored) {
            }
        } else
            m.getChannel().sendMessage("`!source` and then a url. Or attach an image or something.").queue();

        String search = "https://saucenao.com/search.php?api_key=" + safe.SAUCENAOKEY
                + "&dbmask=" + safe.DBMASK + "&output_type=2&numres=5&url=" + url;
        String json = Functions.toString(search);

        try {
            Gson gson = new Gson();
            Results results = gson.fromJson(json, Results.class);
            Results.Result result = results.getResults().get(0);

            if (Float.parseFloat(result.getHeader().getSimilarity()) > 50) {
                EmbedBuilder suki = new EmbedBuilder().setColor(Main.clr);
                suki.setTitle("Source found!", result.getData().getUrls().get(0));
                suki.setImage(result.getHeader().getThumbnail());
                suki.addField("Artist", result.getArtist(), false);
                suki.setFooter("Results provided by SauceNAO");

                m.getChannel().sendMessage(suki.build()).queue();
            } else
                m.getChannel().sendMessage(new EmbedBuilder().setColor(Main.clr).setImage("https://cdn.discordapp.com/attachments/269116788250247181/427875418721484800/source_pls____by_carmelo12341-dad3xtu.png").build()).queue();

        } catch (IllegalStateException | JsonSyntaxException ex) {
            System.out.println(search);
            ex.printStackTrace();
        }
    }
}
