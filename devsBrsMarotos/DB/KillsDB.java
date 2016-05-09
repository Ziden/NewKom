/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.DB;

import devsBrsMarotos.DB.Models.PlayerData;
import devsBrsMarotos.DB.Models.Stage;
import devsBrsMarotos.NewKom;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import org.bukkit.entity.Player;

/**
 *
 * @author vntgasl
 *
 */

/*

 TO control players kills

 */
public class KillsDB {

    Cache<Integer> cache = new Cache<Integer>();

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
            st = conn.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS PKS (uuid varchar(50) PRIMARY KEY, kills integer)");
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getKills(Player player) {
        if (cache.hasCache(player.getUniqueId())) {
            return cache.getCached(player.getUniqueId());
        }
        try {
            Connection conn = NewKom.database.pegaConexao();
            Statement est = conn.createStatement();
            ResultSet rs = est.executeQuery("select kills from PKS where uuid = '" + player.getUniqueId().toString() + "'");
            if (rs.next()) {
                rs.getInt("kills");
            }

        } catch (SQLException ex) {
            Database.DBError(ex);
        }
        return 0;
    }

    public void setKills(Player player, int kills) {
        try {
            Connection conn = NewKom.database.pegaConexao();
            Statement est = conn.createStatement();
            String sql = "insert into STAGES (uuid, kills)"
                    + " values "
                    + "('" + player.getUniqueId().toString() + "', '" + kills + "')";
            sql += " ON DUPLICATE KEY UPDATE kills =" + kills;
            est.executeUpdate(sql);
            cache.set(player.getUniqueId(), kills);
            conn.commit();
        } catch (SQLException ex) {
            Database.DBError(ex);
        }
    }

}
