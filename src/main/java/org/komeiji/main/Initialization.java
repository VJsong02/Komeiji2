package org.komeiji.main;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.komeiji.commands.miscellaneous.Miscellaneous;
import org.komeiji.commands.miscellaneous.SourceFinder;
import org.komeiji.resources.Safe;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Initialization {
    public static Safe readConfig() {
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader("./config.json"));
        } catch (FileNotFoundException ex) {
            Main.logger.error("Config file not found.");
            System.exit(-1);
        }
        return gson.fromJson(reader, Safe.class);
    }

    public static void addToCommands(Class<?> c) {
        for (Method m : c.getDeclaredMethods()) {
            m.setAccessible(true);
            CommandListener.commands.put(m.getName(), m);
        }
    }

    public static void loadCommands() {
        addToCommands(Miscellaneous.class);
        addToCommands(SourceFinder.class);
    }

    public static HashMap<String, Long> loadCustomCommands() throws SQLException {
        String query = "SELECT * FROM customcommands";
        HashMap<String, Long> commands = new HashMap<>();

        try (Connection c = DataSource.getConnection();
             PreparedStatement p = c.prepareStatement(query);
             ResultSet r = p.executeQuery()) {

            while (r.next())
                commands.put(r.getString("cmd"), r.getLong("userid"));
        }

        return commands;
    }

    public static ArrayList<String> loadGifCommands() throws SQLException {
        String query = "SELECT * FROM gifs";
        ArrayList<String> commands = new ArrayList<>();

        try (Connection c = DataSource.getConnection();
             PreparedStatement p = c.prepareStatement(query);
             ResultSet r = p.executeQuery()) {

            while (r.next())
                commands.add(r.getString("cmd"));
        }

        return commands;
    }
}
