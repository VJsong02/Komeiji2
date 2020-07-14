package org.komeiji.main;

import org.komeiji.commands.miscellaneous.Miscellaneous;
import org.komeiji.commands.miscellaneous.SourceFinder;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Initialization {
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
