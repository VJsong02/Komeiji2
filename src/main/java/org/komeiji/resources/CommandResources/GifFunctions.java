package org.komeiji.resources.CommandResources;

import org.komeiji.main.DataSource;
import org.komeiji.resources.Functions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GifFunctions {
    public static GifMessage fetchGifMessage(String name) throws SQLException {
        Connection c = DataSource.getConnection();
        String query = "SELECT * FROM gifs WHERE cmd = ?";
        PreparedStatement p = c.prepareStatement(query);
        p.setString(1, name);
        ResultSet r = p.executeQuery();
        r.next();
        GifMessage g = new GifMessage(r.getString("cmd"), r.getString("message"), Functions.getArgs(r.getString("links")), r.getBoolean("requiresmention"), r.getBoolean("isnsfw"));
        c.close();
        p.close();
        r.close();
        return g;
    }
}
