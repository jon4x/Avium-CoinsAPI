package de.lauxmedia.avium.coins;

import de.lauxmedia.avium.coins.api.API;
import de.lauxmedia.avium.coins.commands.CoinsCommand;
import de.lauxmedia.avium.coins.mysql.MySQL;
import org.bukkit.plugin.java.JavaPlugin;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class Coins extends JavaPlugin {

    private String host;

    private String database;

    private String user;

    private String password;

    private static Coins instance;

    private MySQL mySQLConnection;

    private static API api;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        // load Config
        loadConfig();
        // connect MySQL
        mySQLConnection = new MySQL(host, database, user, password);
        // create new API
        api = new API(mySQLConnection);
        // register Commands
        registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        mySQLConnection.close();
    }

    public void registerCommands() {
        getCommand("coins").setExecutor(new CoinsCommand());
    }

    private void loadConfig() {
        // set defaults
        getConfig().options().copyDefaults(true);
        saveConfig();
        // get Info from Config
        host = getConfig().getString("config.host");
        database = getConfig().getString("config.database");
        user = getConfig().getString("config.user");
        password = getConfig().getString("config.password");
    }

    public static Coins getInstance() {
        return instance;
    }

    public static API getApi() {
        return api;
    }
}
