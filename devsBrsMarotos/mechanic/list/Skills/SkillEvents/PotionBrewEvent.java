/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills.SkillEvents;

import static devsBrsMarotos.mechanic.list.Skills.SkillEvents.BlockHarvestEvent.handlers;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author User
 */
public class PotionBrewEvent extends Event {

    public static HandlerList handlers = new HandlerList();
    
     public static HandlerList getHandlerList() {
       return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
}
