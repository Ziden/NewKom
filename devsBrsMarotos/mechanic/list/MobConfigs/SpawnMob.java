/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.MobConfigs;

import devsBrsMarotos.NewKom;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author ciro
 */
public class SpawnMob {

    private static Integer shed_id = null;

    public SpawnMob() {
        shed_id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(NewKom.instancia, new Runnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    SpawnController.spawna(p);
                }

            }
        }, 0, 20 * 60);
    }

    public static void Cancel() {
        if (shed_id != null) {
            Bukkit.getServer().getScheduler().cancelTask(shed_id);
            shed_id = null;
        }
    }
}
