/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.util;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import devsBrsMarotos.NewKom;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author ciro
 */
public class RegionUtils {

    public static boolean isRegion(Player p, String nameRegion) {
        RegionManager regiao = NewKom.worldGuard.getRegionManager(p.getWorld());
        ProtectedRegion rg = regiao.getRegions().get(nameRegion);
        if (rg == null) {
            return false;
        }
        return true;
    }

    public static String getRegionByLocation(Location loc) {
        String region = null;
        RegionManager manager = NewKom.worldGuard.getRegionManager(loc.getWorld());
        for (ProtectedRegion rg : manager.getApplicableRegions(loc)) {
            region = rg.getId();
        }
        return region;
    }

    public static boolean canSpawnMob(Location loc) {
        RegionManager manager = NewKom.worldGuard.getRegionManager(loc.getWorld());
        ApplicableRegionSet set = manager.getApplicableRegions(loc);
        return set.allows(DefaultFlag.MOB_SPAWNING);
    }
}
