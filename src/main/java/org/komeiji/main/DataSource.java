package org.komeiji.main;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.komeiji.resources.Safe;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {
    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds = new HikariDataSource(config);

    static {
        config.setJdbcUrl(Safe.url);
        config.setUsername(Safe.dbUsername);
        config.setPassword(Safe.dbPassword);
        config.setMaximumPoolSize(10);
        config.setIdleTimeout(10000L);
        config.setLeakDetectionThreshold(10000L);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}