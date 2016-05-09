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

public class StageDB {

    Cache<Stage> cache = new Cache<Stage>();

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
            st.executeUpdate("CREATE TABLE IF NOT EXISTS STAGES (uuid varchar(50), stage varchar(100))");
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Stage getPlayerStage(Player player) {
        Stage data = new Stage();
        if (cache.hasCache(player.getUniqueId())) {
         
            return cache.getCached(player.getUniqueId());
        }
        try {
            Connection conn = NewKom.database.pegaConexao();
            Statement est = conn.createStatement();
            ResultSet rs = est.executeQuery("select stage from STAGES where uuid = '" + player.getUniqueId().toString() + "'");
            while (rs.next()) {
                data.stagesCompleted.add(rs.getString("stage"));
            }
            cache.set(player.getUniqueId(), data);
        } catch (SQLException ex) {
            Database.DBError(ex);
        }
        return data;
    }

    public void addCompletedStage(Player player, String stage) {
        try {
            Connection conn = NewKom.database.pegaConexao();
            Statement est = conn.createStatement();
            String sql = "insert into STAGES (uuid, stage)"
                    + " values "
                    + "('" + player.getUniqueId().toString() + "', '" + stage + "')";
                 
           est.executeUpdate(sql);
           Stage actualStages = getPlayerStage(player);
           actualStages.stagesCompleted.add(stage);
           cache.set(player.getUniqueId(), actualStages);
           conn.commit();
        } catch (SQLException ex) {
            Database.DBError(ex);
        }
    }

}
