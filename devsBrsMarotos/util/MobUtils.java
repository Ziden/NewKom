/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.util;

import devsBrsMarotos.DB.Cache;
import devsBrsMarotos.DB.MobConfigDB;
import devsBrsMarotos.DB.Models.MobOnRegionModel;
import devsBrsMarotos.DB.Models.ModelMobConfig;
import org.bukkit.Location;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

/**
 *
 * @author ciro
 */
public class MobUtils {
    

    public static short getIdMob(String mob) {
        short idMob = 0;
        for (CreatureType cr : CreatureType.values()) {
            if (mob.equalsIgnoreCase(cr.getName())) {
                idMob = cr.getTypeId();
                break;
            }
        }
        return idMob;
    }

    public static EntityType getEntityByName(String name) {
        EntityType etty = null;
        for (EntityType entity : EntityType.values()) {
            if (name.equalsIgnoreCase(entity.getName())) {
                etty = entity;
                break;
            }
        }
        return etty;
    }

    public static void showMobNames(Player p) {
        StringBuilder mobs = new StringBuilder();
        for (CreatureType ct : CreatureType.values()) {
            mobs.append(" §2- §f" + ct.toString());
        }
        p.sendMessage("§3Available names:§f" + mobs);
    }
    
    public static void loadConfig(){
        for(MobOnRegionModel rg : MobConfigDB.cache.getCached()){
            int totPercent = 0;
            for(ModelMobConfig mb : rg.mobs){
                totPercent = mb.percent;
            }
            
        }
        
    }

}
