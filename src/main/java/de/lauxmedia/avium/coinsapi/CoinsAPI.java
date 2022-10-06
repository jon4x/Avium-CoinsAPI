package de.lauxmedia.avium.coinsapi;

import de.lauxmedia.avium.coinsapi.mysql.MySQL;
import org.bukkit.plugin.java.JavaPlugin;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class CoinsAPI extends JavaPlugin {

    private String host;

    private String database;

    private String user;

    private String password;

    private static CoinsAPI instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        // load Config
        loadConfig();
        // connect MySQL
        connectMySQL();
        MySQL.connect();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        MySQL.close();
    }

    private void connectMySQL() {
        host = getConfig().getString("config.host");
        database = getConfig().getString("config.database");
        user = getConfig().getString("config.user");
        password = getConfig().getString("config.password");
    }

    private void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    // getCoins API function
    public int getCoins(String uuid) {
        try {
            PreparedStatement st = MySQL.getConnection().prepareStatement("SELECT * FROM coins WHERE uuid = ?");
            st.setString(1, uuid);
            ResultSet rs = st.executeQuery();
            if (rs.next())
                return rs.getInt("coins");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // setCoins API function
    public void setCoins(String uuid, int coins) {
        if (getCoins(uuid) == -1) {
            try {
                PreparedStatement st = MySQL.getConnection().prepareStatement("INSERT INTO coins (uuid,coins) VALUES (?, ?)");
                st.setString(1, uuid);
                st.setInt(2, coins);
                st.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                PreparedStatement st = MySQL.getConnection().prepareStatement("UPDATE coins SET coins = ? WHERE uuid = ?");
                st.setInt(1, coins);
                st.setString(2, uuid);
                st.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // addCoins API function
    public void addCoins(String uuid, int coins) {
        if (getCoins(uuid) != -1) {
            setCoins(uuid, getCoins(uuid) + coins);
        } else {
            setCoins(uuid, getCoins(uuid) + coins + 1);
        }
    }

    public void removeCoins(String uuid, int coins) {
        setCoins(uuid, getCoins(uuid) - coins);
    }

    public static CoinsAPI getInstance() {
        return instance;
    }

    public String getHost() {
        return host;
    }

    public String getDatabase() {
        return database;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
