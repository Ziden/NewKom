/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Events;

import devsBrsMarotos.mechanic.Mecanica;
import org.bukkit.Bukkit;

/**
 *
 * @author Carlos
 */
public class CustomEvents extends Mecanica {

    @Override
    public String getName() {
        return "Custom Events";
    }

    @Override
    public void onEnable() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            @Override
            public void run() {
                for (UpdateType updateType : UpdateType.values()) {
                    if (updateType.Elapsed()) {
                        Bukkit.getServer().getPluginManager().callEvent(new UpdateEvent(updateType));
                    }
                }
            }
        }, 1, 1);
    }

}
