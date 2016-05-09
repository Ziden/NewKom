/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills.SkillList.Healing;

import devsBrsMarotos.MetaShit;
import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author User
 */
public class HealingEvents extends Mecanica {

    @Override
    public String getName() {
        return "Healing Events";
    }
    
    @EventHandler(priority=EventPriority.HIGHEST)
    public void takeDamage(EntityDamageEvent ev) {
        if(ev.getEntity().getType()==EntityType.PLAYER && ev.getEntity().hasMetadata("healing")) {
            HealingRunnable heal = (HealingRunnable)MetaShit.getMetaObject("healing",ev.getEntity());
            heal.disturb(ev.getDamage());
        }
    }

    @EventHandler
    public void interactEnt(PlayerInteractEntityEvent ev) {
        if (ev.getRightClicked() != null && ev.getRightClicked().getType() == EntityType.PLAYER) {
            Player target = (Player) ev.getRightClicked();
            if (ev.getPlayer().getItemInHand() != null && (ev.getPlayer().getItemInHand().getType() == Material.LEAVES || ev.getPlayer().getItemInHand().getType() == Material.LEAVES_2)) {
                if (ev.getPlayer().hasMetadata("healing")) {
                    ev.getPlayer().sendMessage(ChatColor.RED + L.m("You are already healing..."));
                    return;
                }
                double healingSkill = NewKom.database.skills.getSkills(ev.getPlayer()).get(SkillEnum.Healing).getLvl();
                HealingRunnable heal = new HealingRunnable(ev.getPlayer(), target, healingSkill);
                MetaShit.setMetaObject("healing", ev.getPlayer(), heal);
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, heal);
            }
        }
    }

    @EventHandler
    public void interact(PlayerInteractEvent ev) {
        if (ev.getAction() == Action.RIGHT_CLICK_AIR && ev.getPlayer().getItemInHand() != null && (ev.getPlayer().getItemInHand().getType() == Material.LEAVES || ev.getPlayer().getItemInHand().getType() == Material.LEAVES_2)) {
            if (ev.getPlayer().hasMetadata("healing")) {
                ev.getPlayer().sendMessage(ChatColor.RED + L.m("You are already healing..."));
                return;
            }
            double healingSkill = NewKom.database.skills.getSkills(ev.getPlayer()).get(SkillEnum.Healing).getLvl();
            HealingRunnable heal = new HealingRunnable(ev.getPlayer(), ev.getPlayer(), healingSkill);
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, heal);
        }
    }

}
