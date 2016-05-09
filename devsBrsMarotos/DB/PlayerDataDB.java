/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.DB;

import devsBrsMarotos.DB.Models.PlayerData;
import devsBrsMarotos.NewKom;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author vntgasl
 */
public class PlayerDataDB {

    Cache<PlayerData> cache = new Cache<PlayerData>();

    public void init() {
        Connection conn;
        Statement st;
        try {
            conn = NewKom.database.pegaConexao();
            if (conn == null) {
                NewKom.log.log(Level.SEVERE,
                        "[KoM] CONXEAUMMM VEIO NUUUUL");
                NewKom.instancia.getServer().shutdown();
                return;
            }
            
            ////////////////////////
            // DEBUG ///////////////
            ////////////////////////
            st = conn.createStatement();
            st.executeUpdate("DROP TABLE PLAYERS");
            
            st = conn.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS PLAYERS (uuid varchar(50) PRIMARY KEY, name varchar(30), life INTEGER, lives INTEGER, karma INTEGER, fame INTEGER);");
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PlayerData getPlayerData(Player player) {
        PlayerData data = new PlayerData();
        if(cache.hasCache(player.getUniqueId())) {
            return cache.getCached(player.getUniqueId());
        }
        try {
            Connection conn = NewKom.database.pegaConexao();
            Statement est = conn.createStatement();
            ResultSet rs = est.executeQuery("select * from PLAYERS where uuid = '" + player.getUniqueId().toString() + "'");
            if (rs.next()) {
                data.life = rs.getInt("life");
                data.lives = rs.getInt("lives");
                data.karma = rs.getInt("karma");
                data.fame = rs.getInt("fame");
                cache.set(player.getUniqueId(), data);
            } else {
                updatePlayerData(player, data);
            }
            conn.commit();
        } catch (SQLException ex) {
            Database.DBError(ex);
        }
        return data;
    }

    public void updatePlayerData(Player player, PlayerData data) {
        try {
            Connection conn = NewKom.database.pegaConexao();
            Statement est = conn.createStatement();
            String sql = "insert into PLAYERS (uuid, name, life, lives, karma, fame)"
                   + " values "
                   + "('"+player.getUniqueId().toString()+"', '"+player.getName()+"', "+data.life+", "+data.lives+", "+data.karma+", "+data.fame+")"
                   + " ON DUPLICATE KEY UPDATE life = "+data.life+", lives = "+data.lives+", name = '"+player.getName()+"', karma = "+data.karma+", fame="+data.fame;
            est.executeUpdate(sql);
            cache.set(player.getUniqueId(), data);
            conn.commit();
        } catch (SQLException ex) {
            Database.DBError(ex);
        }
    }

}
