/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.DB;

import devsBrsMarotos.DB.Models.MobOnRegionModel;
import devsBrsMarotos.DB.Models.ModelMobConfig;
import devsBrsMarotos.DB.Models.Skills;
import devsBrsMarotos.NewKom;
import static devsBrsMarotos.NewKom.alertTag;
import devsBrsMarotos.mechanic.list.MobConfigs.MobInfo;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author ciro
 */
public class MobConfigDB {

    public static Cache<MobOnRegionModel> cache = new Cache<MobOnRegionModel>();

    public static void init() {
        Connection conn;
        Statement st;
        try {
            conn = NewKom.database.pegaConexao();
            if (conn == null) {
                NewKom.log.log(Level.SEVERE, "[KoM] CONEXAO VEIO NUUL");
                NewKom.instancia.getServer().shutdown();
                return;
            }
            st = conn.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS mobConfig (id INTEGER PRIMARY KEY AUTO_INCREMENT,nameRegion VARCHAR(50),world VARCHAR(50),mob VARCHAR(50),level INTEGER,percent INTEGER,customName VARCHAR(50))");
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadConfigs() {
        try {
            Connection conn = NewKom.database.pegaConexao();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM mobConfig");
            while (rs.next()) {
                MobOnRegionModel model = new MobOnRegionModel();
                ModelMobConfig mobConfig = new ModelMobConfig();
                model.regionName = rs.getString("nameRegion");
                mobConfig.cName = rs.getString("customName");
                mobConfig.lv = rs.getInt("level");
                mobConfig.mob = rs.getString("mob");
                mobConfig.percent = rs.getInt("percent");

                if (cache.hasCache(model.regionName)) {
                    cache.getCached(model.regionName).mobs.add(mobConfig);
                } else {
                    model.mobs.add(mobConfig);
                    cache.set(model.regionName, model);
                }
            }
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static MobOnRegionModel getMobs(String nameRegion) {
        MobOnRegionModel model = new MobOnRegionModel();
        try {
            Connection conn = NewKom.database.pegaConexao();
            if (conn == null) {
                NewKom.log.log(Level.SEVERE, "[KoM] CONEXAO VEIO NUUL");
                NewKom.instancia.getServer().shutdown();
            }
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM mobConfig WHERE nameRegion = '" + nameRegion + "'");
            while (rs.next()) {
                ModelMobConfig mobConfig = new ModelMobConfig();
                mobConfig.mob = rs.getString("mob");
                mobConfig.cName = rs.getString("customName");
                mobConfig.lv = rs.getInt("level");
                mobConfig.percent = rs.getInt("percent");
                model.mobs.add(mobConfig);
            }
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    public static boolean setMob(Player p, String nameRegion, String world, String mob, String lv, String percent, String name) {
        try {
            Connection conn = NewKom.database.pegaConexao();
            if (conn == null) {
                NewKom.log.log(Level.SEVERE, "[KoM] CONEXAO VEIO NUUL");
                NewKom.instancia.getServer().shutdown();
                return false;
            }
            Statement st = conn.createStatement();
            try {
                st.executeUpdate("INSERT INTO mobConfig (nameRegion,world,mob,level,percent,customName) VALUES ('" + nameRegion + "','" + world + "','" + mob + "','" + lv + "','" + percent + "','" + name + "')");
            } catch (Exception e) {
                e.printStackTrace();
                NewKom.log.log(Level.SEVERE, "[KoM] ERRO AO SALVAR MOB NO BANCO");
                return false;
            }
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            cache.set(nameRegion, getMobs(nameRegion));
        } catch (Exception e) {
            e.printStackTrace();
            NewKom.log.log(Level.SEVERE, "[KoM] ERRO AO SALVAR MOB NA LIST");
            return false;
        }
        return true;
    }

    public static boolean hasConfig(String nameRegion) {
        try {
            Connection conn = NewKom.database.pegaConexao();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM mobConfig WHERE nameRegion = '" + nameRegion + "'");
            if (rs.next()) {
                return true;
            }
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static boolean remConfigMob(String world, String nameRegion, String mob, String percent, String lv, String cName) {
        try {
            Connection conn = NewKom.database.pegaConexao();
            Statement st = conn.createStatement();
            st.executeUpdate("DELETE FROM mobConfig WHERE nameRegion = '" + nameRegion + "' and world='" + world + "' and mob='" + mob + "' and percent=" + percent + " and level=" + lv + " and customName='" + cName + "'");
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            NewKom.log.log(Level.SEVERE, "[KoM] NAO FOI POSSIVEL CONSULTAR CONFIG");
            return false;
        }
        try {
            cache.remove(nameRegion);
            cache.set(nameRegion, getMobs(nameRegion));
        } catch (Exception e) {
            e.printStackTrace();
            NewKom.log.log(Level.SEVERE, "[KoM] NAO FOI POSSIVEL REMOVER CONFIG DA LISTA");
            return false;
        }
        return true;
    }

    public static boolean remConfigAll(String world, String nameRegion) {
        try {
            Connection conn = NewKom.database.pegaConexao();
            Statement st = conn.createStatement();
            st.executeUpdate("DELETE FROM mobConfig WHERE nameRegion = '" + nameRegion + "' and world='" + world + "'");
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            NewKom.log.log(Level.SEVERE, "[KoM] NAO FOI POSSIVEL REMOVER CONFIG DO BANCO");
            return false;
        }
        try {
            cache.remove(nameRegion);
        } catch (Exception e) {
            e.printStackTrace();
            NewKom.log.log(Level.SEVERE, "[KoM] NAO FOI POSSIVEL REMOVER CONFIG DA LISTA");
            return false;
        }
        return true;
    }

}
