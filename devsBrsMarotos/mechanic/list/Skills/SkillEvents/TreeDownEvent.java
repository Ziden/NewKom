/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills.SkillEvents;

import devsBrsMarotos.mechanic.list.Skills.SkillList.Lumberjacking.KomTree;
import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Event for when a player enchant a item
 */
public class TreeDownEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final List<ItemStack> drops;
    private final KomTree tree;

    public TreeDownEvent(Player player, List<ItemStack> drops, KomTree tree) {
        this.player = player;
        this.drops = drops;
        this.tree = tree;
    }

    public List<ItemStack> getDrops() {
        return drops;
    }

    public Player getPlayer() {
        return player;
    }

    public KomTree getTree() {
        return tree;
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

    private boolean cancelled;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean bln) {
        cancelled = bln;
    }
}
