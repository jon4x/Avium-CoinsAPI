package de.lauxmedia.avium.coinsapi.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import de.lauxmedia.avium.coinsapi.CoinsAPI;
import org.bukkit.Bukkit;


public class MySQL {

    public static Connection connection;

    public static void connect() {
        try {
            connection = DriverManager.getConnection
                    ("jdbc:mysql://" + CoinsAPI.getInstance().getHost() + "/" + CoinsAPI.getInstance().getDatabase() + "?Autoreconnect=true",
                    CoinsAPI.getInstance().getUser(), CoinsAPI.getInstance().getPassword());

            createTable();
            Bukkit.getConsoleSender().sendMessage("MySQL wurde erfolgreich verbunden!");
            reconnector();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        if (isConnected())
            try {
                connection.close();
                Bukkit.getConsoleSender().sendMessage("MySQL wurde erfolgreich getrennt!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public static boolean isConnected() {
        return (connection != null);
    }

    public static void createTable() {
        try {
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS coins (uuid VARCHAR(100), coins INT(20))").executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void reconnector() {
        Bukkit.getScheduler().runTaskLaterAsynchronously(CoinsAPI.getInstance(), () -> {
            close();
            connect();
        }, 24000L);

}


}