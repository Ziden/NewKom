/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills.SkillEvents;

import java.util.Set;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Event for when a player enchant a item
 */
public class PlayerEnchantEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final ItemStack item;
    private final Set<Enchantment> ench;
    private final int lvlbooks;
    private final int lvlencantamento;
    private final int custo;

    public PlayerEnchantEvent(Player player, ItemStack item, Set<Enchantment> ench, int lvlbooks, int lvlencantamento, int custo) {
        this.player = player;
        this.item = item;
        this.ench = ench;
        this.lvlbooks = lvlbooks;
        this.lvlencantamento = lvlencantamento;
        this.custo = custo;
    }

 


    /**
     * @return plyer that enchant
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return item that was enchanted
     */
    public ItemStack getItem() {
        return item;
    }

    public int getCusto() {
        return custo;
    }

    public int getLvlbooks() {
        return lvlbooks;
    }

    public int getLvlencantamento() {
        return lvlencantamento;
    }

   
    

    /**
     * @return handlers for this event
     */
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * @return handlers for this event
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
