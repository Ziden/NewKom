/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Dungeon;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import devsBrsMarotos.NewKom;
import java.util.Iterator;
import org.bukkit.Location;

/**
 *
 * @author vntgasl
 *
 */
public class DungeonManager {

    public static String getDungeonName(String regionName) {
        return regionName.replace("dungeon", "").replace("_", " ");
    }

    public static String getDungeon(Location l) {
        ApplicableRegionSet set = NewKom.worldGuard.getRegionManager(l.getWorld()).getApplicableRegions(l);
        if (set == null || set.size() == 0) {
            return null;
        } else {
            Iterator<ProtectedRegion> it = set.iterator();
            while (it.hasNext()) {
                ProtectedRegion regiao = it.next();
                if (regiao.getId().contains("dungeon")) {
                    return getDungeonName(regiao.getId());
                }
            }
            return null;
        }
    }

}
