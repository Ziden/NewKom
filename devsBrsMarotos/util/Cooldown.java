/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.util;

import devsBrsMarotos.MetaShit;
import devsBrsMarotos.NewKom;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.metadata.Metadatable;

/**
 *
 * @author User
 */
public class Cooldown {

    private long time;

    public Cooldown(int millis) {
        this.time = System.currentTimeMillis() + millis;
    }

    public boolean isOver() {
        return System.currentTimeMillis() > time;
    }

    public static String locToString(Location l) {
        return l.getWorld().getName() + "." + l.getBlockX() + "." + l.getBlockY() + "." + l.getBlockZ();
    }

    public static void addHarvestGlobalCooldown(Block b, int millis) {
        Cooldown.setMetaCooldown(b, "harvested", millis);
    }

    public static void addHarvestPersonalCooldown(Player p, Block b, int millis) {
        Cooldown.setMetaCooldown(p, Cooldown.locToString(b.getLocation()), millis);
    }

    public static Location stringToLoc(String s) {
        String[] split = s.split(".");
        return new Location(Bukkit.getWorld(split[0]), Double.valueOf(split[1]), Double.valueOf(split[2]), Double.valueOf(split[3]));
    }

    public static void setMetaCooldown(Metadatable e, String cdName, int millis) {
        MetaShit.setMetaObject("cooldown-" + cdName, e, new Cooldown(millis));
    }

    public static boolean isHarvestCooldown(Block b) {
        return Cooldown.isCooldown(b, "harvested");
    }

    public static boolean isPersonalHarvestCooldown(Block b, Player p) {
        return Cooldown.isCooldown(p, Cooldown.locToString(b.getLocation()));
    }

    public static boolean isCooldown(Metadatable e, String cdName) {
        if (!e.hasMetadata("cooldown-" + cdName)) {
            return false;
        }
        return !((Cooldown) MetaShit.getMetaObject("cooldown-" + cdName, e)).isOver();
    }

    public static void removeCooldown(Metadatable e, String cdname) {
        if (isCooldown(e, cdname)) {
            e.removeMetadata("cooldown-" + cdname, NewKom.instancia);
        }
    }

}
