/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.DB;

import devsBrsMarotos.NewKom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.logging.Level;
import org.bukkit.Bukkit;

/**
 *
 * @author vntgasl
 */
public class Database {

    public StageDB stages;
    public PlayerDataDB playerData;
    public SkillsDB skills;
    public EnchantTablesDB ench;
    public CompassDB compass;
    public KillsDB evilKills;

    public void inicializa() {
        Connection conn;
        Statement st;
        try {
            conn = pegaConexao();
            if (conn == null) {
                return;
            }
            conn.commit();
            stages = new StageDB();
            stages.init();
            playerData = new PlayerDataDB();
            playerData.init();
            skills = new SkillsDB();
            skills.init();
            ench = new EnchantTablesDB();
            ench.init();
            compass = new CompassDB();
            compass.init();
            evilKills = new KillsDB();
            evilKills.init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection connection;
    public static WeakHashMap<String, int[]> bufferDeNiveis = new WeakHashMap<String, int[]>();

    private Statement sst;
    private Statement est;

    public static void DBError(Exception e) {
        e.printStackTrace();
        NewKom.log.info("ERRO BANCO: " + e.getMessage());
        // Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop Restarting KoM !");
    }

    public synchronized Connection pegaConexao() {
        if (connection == null) {
            connection = createConnection();
            try {
                connection.setAutoCommit(false);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return connection;
    }

    private Connection createConnection() {
        try {
            connStr = "jdbc:mysql://127.0.0.1:3306/NewKom?autoReconnect=true";
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
            connection = DriverManager.getConnection(connStr, "root", "komzika");
            // connection = DriverManager.getConnection(connStr, "root", "mamao");
            return connection;
        } catch (ClassNotFoundException e) {
            NewKom.log.log(Level.SEVERE, "[KoMLevel] nao achei a lib", e);
            return null;
        } catch (SQLException e) {
            NewKom.log.log(Level.SEVERE, "[KoMLevel]", e);
        }
        return null;
    }

    public void fechaConexao() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            NewKom.log.log(Level.SEVERE, "[KoMLevel]", e);
        }
    }

    public static String connStr = "jdbc:mysql://127.0.0.1:3306/NewKom?autoReconnect=true";

}
