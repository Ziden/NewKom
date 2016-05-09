/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.MobConfigs;

import devsBrsMarotos.DB.MobConfigDB;
import devsBrsMarotos.MetaShit;
import devsBrsMarotos.util.RegionUtils;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.util.MobUtils;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 *
 * @author vntgasl
 */
public class SpawnController {

    public static void spawna(Player p) {
        Location loc = getLocation(p);
        if (hasBlock(loc)) {
            System.out.println("DBG TEM BLOCO #1");
            loc = getLocation(p);
            if (hasBlock(loc)) {
                System.out.println("DBG TEM BLOCO #2");
                loc = getLocation(p);
                if (hasBlock(loc)) {
                    System.out.println("DBG TEM BLOCO NAO SPAWNA");
                    return;
                }
            }

        }
        if (!RegionUtils.canSpawnMob(p.getLocation())) {
            System.out.println("DBG NAO PODE SPAWNAR MOB NA REGION");
            return;
        }
        if (!MobConfigDB.cache.hasCache(RegionUtils.getRegionByLocation(p.getLocation()))) {
            System.out.println("DBG NAO TEM CONFIG EM CACHE " + RegionUtils.getRegionByLocation(p.getLocation()));
            return;
        }
        if (getMobsPerChunk(loc) > 3) {
            System.out.println("DBG MUITO MOB");
            return;
        }
        String mundo = p.getWorld().getName();
        String regiao = RegionUtils.getRegionByLocation(p.getLocation());
        p.sendMessage("" + RegionUtils.getRegionByLocation(p.getLocation()));
        for (int i = 0; i < MobConfigDB.cache.getCached(regiao).mobs.size(); i++) {
            EntityType mob = MobUtils.getEntityByName(MobConfigDB.cache.getCached(regiao).mobs.get(i).mob);
            LivingEntity bixo = (LivingEntity) Bukkit.getWorld(mundo).spawnCreature(loc, mob);
            MobConfig meta = new MobConfig();
            meta.nivel = MobConfigDB.cache.getCached(regiao).mobs.get(i).lv;
            meta.spawnPercentage = MobConfigDB.cache.getCached(regiao).mobs.get(i).percent;
            bixo.setCustomName(MobConfigDB.cache.getCached(regiao).mobs.get(i).cName);
            MetaShit.setMetaObject("MobConfig", bixo, meta);
        }
    }

    public static Location getLocation(Player p) {
        Random rnd = new Random();
        Location locPl = p.getLocation();
        Location locMob = new Location(p.getWorld(), locPl.getBlockX() + rnd.nextInt(11) - 5, locPl.getBlockY(), locPl.getBlockZ() + rnd.nextInt(11) - 5);
        return locMob;
    }

    public static boolean hasBlock(Location loc) {
        if (Bukkit.getWorld(loc.getWorld().getName()).getBlockAt(loc).getType() == Material.AIR
                && Bukkit.getWorld(loc.getWorld().getName()).getBlockAt(loc).getRelative(BlockFace.UP).getType() == Material.AIR
                && Bukkit.getWorld(loc.getWorld().getName()).getBlockAt(loc).getRelative(BlockFace.DOWN).getType() != Material.AIR) {
            return false;
        }
        return true;
    }

    public static int getMobsPerChunk(Location loc) {
        int i = 0;
        for (Entity et : Bukkit.getWorld(loc.getWorld().getName()).getChunkAt(loc).getEntities()) {
            if (et instanceof LivingEntity) {
                i++;
            }
        }
        return i;
    }
}
