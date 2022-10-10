package de.lauxmedia.avium.coins.api;

import de.lauxmedia.avium.coins.mysql.MySQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class API {

    private static MySQL mySQLConnection;

    public API(MySQL mySQLConnection) {
        API.mySQLConnection = mySQLConnection;
    }

    public int getCoins(String uuid) {
        try {
            PreparedStatement st = mySQLConnection.getConnection().prepareStatement("SELECT * FROM coins WHERE uuid = ?");
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
                PreparedStatement st = mySQLConnection.getConnection().prepareStatement("INSERT INTO coins (uuid,coins) VALUES (?, ?)");
                st.setString(1, uuid);
                st.setInt(2, coins);
                st.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                PreparedStatement st = mySQLConnection.getConnection().prepareStatement("UPDATE coins SET coins = ? WHERE uuid = ?");
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

}
