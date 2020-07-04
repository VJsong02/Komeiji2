package org.komeiji.resources.UserResources;

import org.komeiji.main.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UsrFunctions {
    public static boolean blacklisted(String id) {
        try {
            Usr user = fetchUsr(id);
            return user.isBlacklisted().booleanValue();
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean admin(String id) {
        try {
            Usr user = fetchUsr(id);
            return user.isAdmin().booleanValue();
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean evaler(String id) {
        try {
            Usr user = fetchUsr(id);
            return user.isEvaler().booleanValue();
        } catch (SQLException e) {
            return false;
        }
    }

    public static long timeout(String id) {
        try {
            Usr user = fetchUsr(id);
            return user.getTimeout().longValue();
        } catch (SQLException e) {
            return 0L;
        }
    }

    public static String home(String id) {
        try {
            Usr user = fetchUsr(id);
            return user.getHome();
        } catch (SQLException e) {
            return null;
        }
    }

    public static void insertUsr(Usr user) throws SQLException {
        Connection c = DataSource.getConnection();
        String query = "INSERT INTO users (userid, admin, blacklisted, evaler, timeout, home) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement p = c.prepareStatement(query);
        p.setString(1, user.getId());
        p.setBoolean(2, (user.isAdmin() != null));
        p.setBoolean(3, (user.isBlacklisted() != null));
        p.setBoolean(4, (user.isEvaler() != null));
        p.setLong(5, (user.getTimeout() == null) ? 0L : user.getTimeout().longValue());
        p.setString(6, (user.getHome() == null) ? "" : user.getHome());
        System.out.println(p.toString());
        p.executeUpdate();
        p.close();
        c.close();
    }

    public static Usr fetchUsr(String id) throws SQLException {
        Connection c = DataSource.getConnection();
        String query = "SELECT * FROM users WHERE userid = ?";
        PreparedStatement p = c.prepareStatement(query);
        p.setString(1, id);
        ResultSet r = p.executeQuery();
        Usr user = null;
        if (r.next())
            user = new Usr(r.getString("userid"), Boolean.valueOf(r.getBoolean("admin")), Boolean.valueOf(r.getBoolean("blacklisted")), Boolean.valueOf(r.getBoolean("evaler")), Long.valueOf(r.getLong("timeout")), r.getString("home"));
        c.close();
        p.close();
        r.close();
        return user;
    }

    public static ArrayList<Usr> fetchUserList() throws SQLException {
        Connection c = DataSource.getConnection();
        String query = "SELECT * FROM users";
        PreparedStatement p = c.prepareStatement(query);
        ResultSet r = p.executeQuery();
        ArrayList<Usr> users = new ArrayList<>();
        while (r.next())
            users.add(new Usr(r.getString("userid"), Boolean.valueOf(r.getBoolean("admin")), Boolean.valueOf(r.getBoolean("blacklisted")),
                    Boolean.valueOf(r.getBoolean("evaler")), Long.valueOf(r.getLong("timeout")), r.getString("home")));
        c.close();
        p.close();
        r.close();
        return users;
    }

    public static void modUsr(String id, Usr in) throws SQLException {
        if (fetchUsr(id) != null) {
            Usr user = fetchUsr(id);
            user = new Usr(id, (in.isAdmin() == null) ? user.isAdmin() : in.isAdmin(), (in.isBlacklisted() == null) ? user.isBlacklisted() : in.isBlacklisted(), (in.isEvaler() == null) ? user.isEvaler() : in.isEvaler(), (in.getTimeout() == null) ? user.getTimeout() : in.getTimeout(), (in.getHome() == null) ? user.getHome() : in.getHome());
            Connection c = DataSource.getConnection();
            String query = "UPDATE users SET admin = ?,blacklisted = ?,evaler = ?,timeout = ?,home = ? WHERE userid = ?";
            PreparedStatement p = c.prepareStatement(query);
            p.setBoolean(1, user.isAdmin().booleanValue());
            p.setBoolean(2, user.isBlacklisted().booleanValue());
            p.setBoolean(3, user.isEvaler().booleanValue());
            p.setLong(4, user.getTimeout().longValue());
            p.setString(5, user.getHome());
            p.setString(6, id);
            p.executeUpdate();
            p.close();
            c.close();
        } else {
            insertUsr(in);
        }
    }
}
