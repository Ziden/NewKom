/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills;

import devsBrsMarotos.mechanic.Mecanica;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 *
 * @author vntgasl
 */
public class SkillsEvents extends Mecanica {

    @Override
    public String getName() {
        return "Skills Generic Events";
    }
    
    @EventHandler
    public void invClick(InventoryClickEvent ev) {
        if(ev.getInventory().getName().contains("Skills:") || ev.getInventory().getTitle().contains("Skills:")) {
            ev.setCancelled(true);
        }
    }
    
    
    
}
