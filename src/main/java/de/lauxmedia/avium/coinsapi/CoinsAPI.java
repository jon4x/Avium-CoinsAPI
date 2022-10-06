package de.lauxmedia.avium.coinsapi;

import org.bukkit.plugin.java.JavaPlugin;

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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void connectMySQL() {
        host = getConfig().getString("config.host");
        database = getConfig().getString("config.database");
        user = getConfig().getString("config.user");
        password = getConfig().getString("config.password");
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
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
