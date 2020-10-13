package org.komeiji.resources.CommandResources;

import org.komeiji.main.DataSource;

import java.sql.*;

public class CustomFunctions {
    public static void insertCustomCommand(String name, String link, String author, Long userid) throws SQLException {
        String query = "INSERT INTO customcommands (cmd, link, author, userid, date) VALUES (?, ?, ?, ?, ?)";

        try (Connection c = DataSource.getConnection();
             PreparedStatement p = c.prepareStatement(query)) {
            p.setString(1, name);
            p.setString(2, link);
            p.setString(3, author);
            p.setLong(4, userid);
            p.setTimestamp(5, new Timestamp(new java.util.Date().getTime()));
            p.execute();
        }
    }

    public static void removeCustomCommand(String name) throws SQLException {
        String query = "DELETE FROM customcommands WHERE cmd = ?";

        try (Connection c = DataSource.getConnection();
             PreparedStatement p = c.prepareStatement(query)) {
            p.setString(1, name);
            p.executeUpdate();
        }
    }

    public static CustomMessage fetchCustomCommand(String name) throws SQLException {
        String query = "SELECT * FROM customcommands WHERE cmd = ?";
        try (Connection c = DataSource.getConnection()) {
            PreparedStatement p = c.prepareStatement(query);
            p.setString(1, name);
            ResultSet r = p.executeQuery();
            r.next();
            CustomMessage cm = new CustomMessage(r.getString("link"),
                    r.getDate("date"),
                    r.getString("author"),
                    r.getLong("userid"));
            p.close();
            r.close();
            return cm;
        }
    }
}