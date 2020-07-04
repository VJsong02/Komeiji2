import org.komeiji.main.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomFunctions {
    public static void insertCustomCommand(String name, String link, String author) throws SQLException {
        Connection c = DataSource.getConnection();
        String query = "INSERT INTO customcommands (cmd, link, author) VALUES (?, ?, ?)";
        PreparedStatement p = c.prepareStatement(query);
        p.setString(1, name);
        p.setString(2, link);
        p.setString(3, author);
        p.execute();
        c.close();
        p.close();
    }

    public static void removeCustomCommand(String name) throws SQLException {
        Connection c = DataSource.getConnection();
        String query = "DELETE FROM customcommands WHERE cmd = ?";
        PreparedStatement p = c.prepareStatement(query);
        p.setString(1, name);
        p.executeUpdate();
        c.close();
        p.close();
    }

    public static CustomMessage fetchCustomCommand(String name) throws SQLException {
        Connection c = DataSource.getConnection();
        String query = "SELECT * FROM customcommands WHERE cmd = ?";
        PreparedStatement p = c.prepareStatement(query);
        p.setString(1, name);
        ResultSet r = p.executeQuery();
        r.next();
        CustomMessage cm = new CustomMessage(r.getString("link"), r.getString("author"));
        c.close();
        p.close();
        r.close();
        return cm;
    }
}