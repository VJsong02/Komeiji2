package org.komeiji.commands.miscellaneous;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import org.komeiji.main.DataSource;
import org.komeiji.main.Main;
import org.komeiji.resources.Functions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.komeiji.main.Main.prefix;
import static org.komeiji.main.Main.safe;

class Condition {
    private static final ArrayList<String> adverbs = new ArrayList<>(Arrays.asList("damn cold", "darn cold",
            "bone chilling", "glacial", "frigid", "freezing", "frosty", "pretty cold", "chilly", "brisk", "cool",
            "quite temperate", "rather mild", "pretty nice", "positively balmy", "extra warm", "kinda hot", "roasting",
            "scorching", "oven-like", "actually dangerous to go"));
    private List<Forecast> weather;
    private Main main;
    private long dt;

    static String comment(float temperature, boolean raining) {
        String out = "";
        if (temperature <= 8)
            out += "You should wear a coat, ";
        else if (temperature <= 15)
            out += "You'll want to wear a jacket, ";
        else if (temperature <= 18)
            out += "Maybe you should wear a hoodie, ";
        else if (temperature >= 30)
            out += "Going out isn't a particularly good idea, ";
        else
            out += "A plain t-shirt is enough, ";

        out += "it's " + adverbs.get((int) (temperature + 21) / 3) + " outside.";

        if (raining)
            out += "\n You should also bring an umbrella, it's raining and you'll catch a cold otherwise.";

        return out;
    }

    static void addUser(long id, String place) {
        String query = "INSERT INTO weather (userid, place) VALUES (?, ?) ON DUPLICATE KEY UPDATE place = ?";
        try (Connection c = DataSource.getConnection();
             PreparedStatement p = c.prepareStatement(query)) {
            p.setLong(1, id);
            p.setString(2, place);
            p.setString(3, place);
            p.execute();
        } catch (SQLException ex) {
            org.komeiji.main.Main.logger.error(query, ex);
        }
    }

    public Forecast getForecast() {
        return weather.get(0);
    }

    public Main getTemps() {
        return main;
    }

    public long getTime() {
        return dt;
    }

    static class Forecast {
        private int id;
        private String main;
        private String description;
        private String icon;

        public int getId() {
            return id;
        }

        public String getMain() {
            return main;
        }

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }

        public String getIconUrl() {
            return "http://openweathermap.org/img/wn/" + icon + ".png";
        }
    }

    static class Main {
        private float temp;
        private float feels_like;

        public float getTemp() {
            return temp;
        }

        public float feelsLike() {
            return feels_like;
        }
    }
}

public class Weather {
    public static HashMap<Long, String> users = new HashMap<>();

    static void weather(Message m) throws IOException {
        String location;
        if (m.getContentDisplay().contains(" ")) {
            location = m.getContentDisplay().substring(prefix.length() + 7).trim();
        } else if (users.containsKey(m.getAuthor().getIdLong()))
            location = users.get(m.getAuthor().getIdLong());
        else {
            m.getChannel().sendMessage("`!weather` and location. If you're homeless it's not my fault.").queue();
            return;
        }

        String url = "http://api.openweathermap.org/data/2.5/weather?q="
                + URLEncoder.encode(location, StandardCharsets.UTF_8) + "&appid=" + safe.OWMKEY + "&units=metric";

        try {
            String json = Functions.toString(url);
            Gson gson = new Gson();
            Condition condition = gson.fromJson(json, Condition.class);

            DateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm");
            String description = "It's " + Math.round(condition.getTemps().getTemp()) +
                    " degrees outside, and it feels like " + Math.round(condition.getTemps().feelsLike()) + " degrees.\n";
            String comment = Condition.comment(condition.getTemps().feelsLike(),
                    (2 <= condition.getForecast().getId() && condition.getForecast().getId() <= 5));

            EmbedBuilder suki = new EmbedBuilder().setColor(Main.clr);
            suki.setTitle(condition.getForecast().getDescription() + ".");
            suki.setThumbnail(condition.getForecast().getIconUrl());
            suki.setDescription(description + comment);
            suki.setFooter(df.format(condition.getTime() * 1000));
            m.getChannel().sendMessage(suki.build()).queue();

            users.put(m.getAuthor().getIdLong(), location);
            Condition.addUser(m.getAuthor().getIdLong(), location);
        } catch (FileNotFoundException ex) {
            m.getChannel().sendMessage("Can't find that place.").queue();
        } catch (IllegalStateException | JsonSyntaxException ex) {
            System.out.println(url);
            ex.printStackTrace();
        }
    }
}
