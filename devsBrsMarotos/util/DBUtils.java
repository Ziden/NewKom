/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author vntgasl
 */
public class DBUtils {
    
    public static int getLastId(Connection cn) {
        try {
            ResultSet rss = cn.createStatement().executeQuery("select LAST_INSERT_ID() as id_historico");
            rss.next();
            int gameId = rss.getInt("id_historico");
            return gameId;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

   

    public static int getNumberOfRows(Connection cn, String table) {
        try {
            ResultSet rss = cn.createStatement().executeQuery("SELECT COUNT(*) AS col FROM "+table+"");
            rss.next();
            int gameId = rss.getInt("col");
            return gameId;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

}
