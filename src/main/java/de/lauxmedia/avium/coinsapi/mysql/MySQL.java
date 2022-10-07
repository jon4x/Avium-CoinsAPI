package de.lauxmedia.avium.coinsapi.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import de.lauxmedia.avium.coinsapi.CoinsAPI;
import org.bukkit.Bukkit;


public class MySQL {

    private static Connection connection;

    public static void connect() {
        try {
            connection = DriverManager.getConnection
                    ("jdbc:mysql://" + CoinsAPI.getInstance().getHost() + "/" + CoinsAPI.getInstance().getDatabase() + "?Autoreconnect=true",
                    CoinsAPI.getInstance().getUser(), CoinsAPI.getInstance().getPassword());

            createTable();
            Bukkit.getConsoleSender().sendMessage("MySQL wurde erfolgreich verbunden!");
            reConnector();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        if (isConnected())
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public static boolean isConnected() {
        return (connection != null);
    }

    private static void createTable() {
        try {
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS coins (uuid VARCHAR(100), coins INT(20))").executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void reConnector() {
        Bukkit.getScheduler().runTaskLaterAsynchronously(CoinsAPI.getInstance(), () -> {
            if (!isConnected()) {
                connect();
            }
        }, 24000L);
    }

    public static Connection getConnection() {
        return connection;
    }
}
