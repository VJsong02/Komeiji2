package org.komeiji.resources.CommandResources;

import org.komeiji.main.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GifFunctions {
    public static GifMessage fetchGifMessage(String name) throws SQLException {
        try (Connection c = DataSource.getConnection();
             PreparedStatement p = c.prepareStatement("SELECT * FROM gifs WHERE cmd = ?")) {
            p.setString(1, name);
            ResultSet r = p.executeQuery();
            r.next();
            return new GifMessage(r.getString("cmd"), r.getString("message"), r.getString("links").split(" "), r.getBoolean("requiresmention"), r.getBoolean("isnsfw"));
        }
    }
}
