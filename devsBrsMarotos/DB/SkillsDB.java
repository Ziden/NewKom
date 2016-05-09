/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.DB;

import devsBrsMarotos.DB.Models.Skills;
import devsBrsMarotos.DB.Models.Skills.SkillInfo;
import devsBrsMarotos.DB.Models.Stage;
import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import org.bukkit.entity.Player;

/**
 *
 * @author vntgasl
 */
public class SkillsDB {

    Cache<Skills> cache = new Cache<Skills>();

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
            String columns = "";
            for (SkillEnum e : SkillEnum.values()) {
                columns += e.name() + "_xp" + " double DEFAULT 0,";
                columns += e.name() + "_lvl" + " double DEFAULT 1,";
                columns += e.name() + "_xptotal double DEFAULT 0,";
            }
            columns = columns.substring(0, columns.length() - 1);
            st.executeUpdate("CREATE TABLE IF NOT EXISTS SKILLS (uuid varchar(50) PRIMARY KEY, " + columns + ")");
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Skills getSkills(Player player) {
        Skills data = new Skills();
        if (cache.hasCache(player.getUniqueId())) {
            return cache.getCached(player.getUniqueId());
        }
        try {
            Connection conn = NewKom.database.pegaConexao();
            Statement est = conn.createStatement();
            ResultSet rs = est.executeQuery("select * from SKILLS where uuid = '" + player.getUniqueId().toString() + "'");
            if (rs.next()) {
                for (SkillEnum e : SkillEnum.values()) {
                    SkillInfo si = new Skills.SkillInfo();
                    double xp = rs.getDouble(e.name() + "_xp");
                    double lvl = rs.getDouble(e.name() + "_lvl");
                    double xptotal = rs.getDouble(e.name()+"_xptotal");
                    data.skills.put(e, new Skills.SkillInfo(lvl, xp, xptotal));
                }
            } else {
                for (SkillEnum e : SkillEnum.values()) {
                    data.skills.put(e, new SkillInfo());
                }
                est = conn.createStatement();

                String sql = "insert into SKILLS (uuid) values ('" + player.getUniqueId().toString() + "')";
                est.executeUpdate(sql);
            }
            cache.set(player.getUniqueId(), data);
        } catch (SQLException ex) {
            Database.DBError(ex);
        }
        return data;
    }

    public void updateSkills(Player p, Skills data) {
        try {
            Connection conn = NewKom.database.pegaConexao();
            Statement est = conn.createStatement();
            String sql = "update SKILLS set ";
            for (SkillEnum e : SkillEnum.values()) {
                sql += e.name() + "_xp=" + data.get(e).getXp() + ",";
                sql += e.name() + "_xptotal=" + data.get(e).getXpTotal() + ",";
                sql += e.name() + "_lvl=" + data.get(e).getLvl() + ",";
            }
            sql = sql.substring(0, sql.length() - 1);
            sql += " where uuid = '" + p.getUniqueId().toString() + "'";
            NewKom.log.info(sql);
            est.executeUpdate(sql);
            cache.set(p.getUniqueId(), data);
            conn.commit();
        } catch (SQLException ex) {
            Database.DBError(ex);
        }
    }

}
