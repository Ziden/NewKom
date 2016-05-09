/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills.SkillEvents;

import devsBrsMarotos.DB.Models.Skills;
import devsBrsMarotos.mechanic.list.Harvesting.Harvestable;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import devsBrsMarotos.mechanic.list.Skills.SkillFormulas.SkillResult;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author User
 */
public class BlockHarvestEvent extends Event implements Cancellable {

    public static HandlerList handlers = new HandlerList();

    public BlockHarvestEvent(Player p, Block b, Harvestable h, Skills playerSkills, SkillResult rs) {
        this.block = b;
        this.playerSkills = playerSkills;
        this.harvestable = h;
        this.player = p;
    }

    private boolean shouldDefaultHarvest = true;
    private Skills playerSkills;
    private boolean canceled = false;
    private Block block;
    private Harvestable harvestable;
    private Player player;
    public SkillResult result;

    public SkillResult getResult() {
        return result;
    }

    public Block getBlock() {
        return block;
    }

    public double getSkill(SkillEnum s) {
        return playerSkills.get(s).getLvl();
    }

    public void setDefaultHarvest(boolean b) {
        this.shouldDefaultHarvest = b;
    }

    public boolean isDefaultHarvest() {
        return shouldDefaultHarvest;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public Harvestable getHarvestable() {
        return harvestable;
    }

    public void setHarvestable(Harvestable harvestable) {
        this.harvestable = harvestable;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.canceled;
    }

    @Override
    public void setCancelled(boolean bln) {
        this.canceled = bln;
    }

}
