/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.KarmaFameCriminal;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.util.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 *
 * @author vntgasl
 */
public class Criminal extends Mecanica {

    ////////// FALTA TERMINAR //////////////////////
    @Override
    public void onEnable() {
       
    }
    
    public void playDeath(PlayerDeathEvent ev) {
        if(!isCriminal(ev.getEntity()) && !isPK(ev.getEntity()) && ev.getEntity().getKiller()!=null) {
            
        }
    }
    
    public static final int NEED_KILLS_FOR_PK = 3;
    
    public void setCriminal(final Player p) {
        Cooldown.setMetaCooldown(p, "criminal", 1000*60); // 1 min
        Runnable r = new Runnable() {
            public void run() {
                KarmaFameTables.updateTitle(p);
            }
        };
        Bukkit.getScheduler().scheduleSyncDelayedTask(NewKom.instancia, r, 20*62); // a little more then 1 min
    }
    
    public static boolean isCriminal(Player p) {
        return Cooldown.isCooldown(p, "criminal");
    }
    
    public static boolean isPK(Player p) {
       return NewKom.database.evilKills.getKills(p) >= NEED_KILLS_FOR_PK;
    }

    @Override
    public String getName() {
       return "Criminal";
    }
    
    
}
