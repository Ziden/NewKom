package devsBrsMarotos.DB;

import devsBrsMarotos.DB.Models.EnchantmentTable;
import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.list.Skills.SkillList.Enchanting.EnchantingListener;
import devsBrsMarotos.mechanic.list.Skills.SkillList.Enchanting.SelectEnchantmentsMenu;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;

/**
 *
 * @author Carlos
 */
public class EnchantTablesDB {

    Cache<EnchantmentTable> cache = new Cache<EnchantmentTable>();

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

            st.executeUpdate("CREATE TABLE IF NOT EXISTS enchantment (location VARCHAR(200) PRIMARY KEY,enchants TEXT)");
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create(Block b) {
        Connection conn;
        Statement st;
        try {
            conn = NewKom.database.pegaConexao();

            st = conn.createStatement();

            st.executeUpdate("INSERT INTO enchantment (location,enchants) VALUES('" + fromLocation(b.getLocation()) + "','" + convertToString(getDefault()) + "')");
            cache.set(b, getDefault());
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public EnchantmentTable getEnchantment(Block b) {
        if (cache.hasCache(b)) {
            return cache.getCached(b);
        }
        try {
            Connection conn = NewKom.database.pegaConexao();
            Statement est = conn.createStatement();
            ResultSet rs = est.executeQuery("select * from enchantment where location ='" + fromLocation(b.getLocation()) + "'");

            if (rs.next()) {
                EnchantmentTable et = convertFromString(rs.getString("enchants"));

                cache.set(b, et);
                return et;
            } else {
                create(b);
                return getDefault();
            }

        } catch (SQLException ex) {
            Database.DBError(ex);
        }
        return null;

    }

    public void update(Block b, EnchantmentTable tb) {
        try {
            Connection conn = NewKom.database.pegaConexao();
            Statement est = conn.createStatement();
            est.executeUpdate("UPDATE enchantment set enchants = '" + convertToString(tb) + "' where location ='" + fromLocation(b.getLocation()) + "'");
            conn.commit();
        } catch (SQLException ex) {
            Database.DBError(ex);
        }
    }

    public EnchantmentTable getDefault() {
        EnchantmentTable tb = new EnchantmentTable();
        for (Enchantment de : EnchantingListener.defaults) {
            tb.enchants.put(de, true);
        }
        return tb;
    }

    public String convertToString(EnchantmentTable table) {
        String s = "";
        boolean b = false;
        for (Enchantment ench : table.enchants.keySet()) {
            if (!b) {
                b = true;
            } else {
                s += ":";
            }
            s += ench.getName() + "," + table.enchants.get(ench);

        }
        return s;
    }

    public EnchantmentTable convertFromString(String s) {
        EnchantmentTable et = new EnchantmentTable();
        for (String st : s.split(":")) {
            String[] st1 = st.split(",");
            for (Enchantment ench : Enchantment.values()) {
                if (ench.getName().equals(st1[0])) {
                    et.enchants.put(ench, Boolean.valueOf(st1[1]));
                }
            }
        }
        return et;
    }

    public static Location fromString(String s) {
        String[] split = s.split(":");
        return new Location(Bukkit.getWorld(split[0]), Integer.valueOf(split[1]), Integer.valueOf(split[2]), Integer.valueOf(split[3]));
    }

    public static String fromLocation(Location s) {
        return s.getWorld().getName() + ":" + s.getBlockX() + ":" + s.getBlockY() + ":" + s.getBlockZ();
    }

    public void delete(Block block) {
        try {
            Connection conn = NewKom.database.pegaConexao();
            Statement est = conn.createStatement();
            est.executeUpdate("DELETE from enchantment where location ='" + fromLocation(block.getLocation()) + "'");
            conn.commit();
            cache.remove(block);
            Inventory i = SelectEnchantmentsMenu.invs.remove(block);
            for (HumanEntity we : i.getViewers()) {
                we.closeInventory();
            }
        } catch (SQLException ex) {
            Database.DBError(ex);
        }
    }
}
