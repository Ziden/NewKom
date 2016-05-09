package devsBrsMarotos.DB;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.list.Compass.CompassInventory;
import devsBrsMarotos.mechanic.list.Compass.CompassLocation;
import devsBrsMarotos.util.LocUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author Carlos
 */
public class CompassDB {

    Cache<List<CompassLocation>> cache = new Cache<List<CompassLocation>>();

    public void init() {
        try {
            NewKom.database.pegaConexao().createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS compass (uuid VARCHAR(60),nome VARCHAR(40),location VARCHAR(200))");
            NewKom.database.pegaConexao().commit();
        } catch (SQLException ex) {
            Logger.getLogger(CompassDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean remove(Player p, String nome) {
        if (hasName(p.getUniqueId(), nome)) {
            try {
                NewKom.database.pegaConexao().createStatement().executeUpdate("DELETE FROM compass WHERE uuid ='" + p.getUniqueId() + "' and nome ='" + nome + "'");
                CompassLocation toremove = null;
                for (CompassLocation cl : cache.getCached(p.getUniqueId())) {
                    if (cl.nome.equalsIgnoreCase(nome)) {
                        toremove = cl;
                        break;
                    }
                }
                if (toremove != null) {
                    cache.getCached(p.getUniqueId()).remove(toremove);
                    if (CompassInventory.invs.hasCache(p.getUniqueId())) {
                        CompassInventory.enche(CompassInventory.invs.getCached(p.getUniqueId()), p);
                    }
                }
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(CompassDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;

    }

    public List<CompassLocation> getLocations(Player p) {
        if (!cache.hasCache(p.getUniqueId())) {
            load(p.getUniqueId());
        }
        return cache.getCached(p.getUniqueId());

    }

    public void addLocation(Player p, Location l, String nome) {
        if (hasName(p.getUniqueId(), nome)) {
            return;
        }
        try {
            NewKom.database.pegaConexao().createStatement().executeUpdate("INSERT INTO compass (uuid,location,nome) VALUES('" + p.getUniqueId() + "','" + LocUtils.loc2str(l) + "','" + nome + "')");
            NewKom.database.pegaConexao().commit();
            CompassLocation cl = new CompassLocation();
            cl.l = l;
            cl.nome = nome;
            cache.getCached(p.getUniqueId()).add(cl);
            if (CompassInventory.invs.hasCache(p.getUniqueId())) {
                CompassInventory.enche(CompassInventory.invs.getCached(p.getUniqueId()), p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CompassDB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean hasName(UUID uid, String name) {
        if (!cache.hasCache(uid)) {
            load(uid);
        }
        for (CompassLocation cl : cache.getCached(uid)) {
            if (cl.nome.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public void load(UUID uid) {

        try {
            ResultSet rs = NewKom.database.pegaConexao().createStatement().executeQuery("SELECT * FROM compass WHERE uuid = '" + uid.toString() + "'");
            while (rs.next()) {
                List<CompassLocation> list = null;
                if (cache.hasCache(uid)) {
                    list = cache.getCached(uid);
                } else {
                    list = new ArrayList<>();
                    cache.set(uid, list);
                }
                CompassLocation cl = new CompassLocation();
                cl.nome = rs.getString("nome");
                cl.l = LocUtils.str2loc(rs.getString("location"));
                list.add(cl);
            }
            if (!cache.hasCache(uid)) {
                cache.set(uid, new ArrayList());
            }
        } catch (SQLException ex) {
            Logger.getLogger(CompassDB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
