package de.lauxmedia.avium.coins.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import de.lauxmedia.avium.coins.Coins;
import org.bukkit.Bukkit;


public class MySQL {

    private static Connection connection;

    private static String host;
    private static String database;
    private static String user;
    private static String password;

    public MySQL(String host, String database, String user, String password) {
        MySQL.host = host;
        MySQL.database = database;
        MySQL.user = user;
        MySQL.password = password;
        connect();
    }


    public void connect() {
        try {
            // create connection
            connection = DriverManager.getConnection
                    ("jdbc:mysql://" + host + "/" + database + "?Autoreconnect=true", user, password);
            // create Table if not exists
            createTable();
            // Log Message
            Bukkit.getLogger().info("MySQL connected successfully!");
            // start re-Connector task
            reConnector();
        } catch (SQLException e) {
            Bukkit.getLogger().info("MySQL connection failed!");
            e.printStackTrace();
        }
    }

    public void close() {
        if (isConnected())
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public boolean isConnected() {
        return (connection != null);
    }

    public void createTable() {
        try {
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS coins (uuid VARCHAR(100), coins INT(255))").executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // check connection all 15 minutes
    public void reConnector() {
        Bukkit.getScheduler().runTaskLaterAsynchronously(Coins.getInstance(), () -> {
            if (!isConnected()) {
                close();
                connect();
            }
        }, 18000L);
    }

    public Connection getConnection() {
        return connection;
    }
}
