/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills.SkillList.Healing;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import devsBrsMarotos.mechanic.list.Skills.SkillFormulas;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/**
 *
 * @author User
 */
public class HealingRunnable implements Runnable {

    public HealingRunnable(Player p, Player target, double mySkill) {
        this.player = p;
        this.healTarget = target;
        this.mySkill = mySkill;
    }

    public int loops = 0;
    public boolean canceled = false;
    public Player healTarget;
    public Player player;
    public double mySkill;
    public int loopsToComplete = 0;
    public double ammountToHeal = 4;
    public int healingSkill = 0;

    @Override
    public void run() {

        if (player == null || !player.isOnline()) {
            cancel();
            return;
        }
        
         if(ammountToHeal <= 0) {
            player.sendMessage(ChatColor.RED+L.m("You sliped too much and couldnt heal your target !"));
            cancel();
            return;
        }

        if (!this.isSelfHealing()) {
            if (healTarget == null || !healTarget.isOnline()) {
                cancel();
                return;
            }
            if (player.getLocation().distance(healTarget.getLocation()) > 5) {
                player.sendMessage(ChatColor.GREEN + L.m("Your target is too far to heal !"));
                cancel();
                return;
            }
            if(healTarget.hasPotionEffect(PotionEffectType.POISON)) {
                player.sendMessage(ChatColor.RED+L.m("The target is poisoned and cannot be healed !"));
                cancel();
            }
        } else {
            if(player.hasPotionEffect(PotionEffectType.POISON)) {
                 player.sendMessage(ChatColor.RED+L.m("You are is poisoned and cannot be healed !"));
                cancel();
            }
        }
        
        

        //// INITIAL HEALING
        if (loops == 0) {
            int skill = (int) NewKom.database.skills.getSkills(player).get(SkillEnum.Healing).getLvl();
            healingSkill = skill;
            loopsToComplete = 10 - (skill / 20); // max -5
            ammountToHeal += skill / 5; // max 4 + 20 = 24
            if (this.isSelfHealing()) {
                player.sendMessage(ChatColor.GREEN + L.m("You started to heal yourself !"));
            } else {
                player.sendMessage(ChatColor.GREEN + L.m("You started to heal ") + healTarget.getName());
                healTarget.sendMessage(ChatColor.GREEN + player.getName() + L.m(" started to heal you !"));
                loopsToComplete /= 2;
            }
        } else {
            // continue healing to the next loop
            if (this.isSelfHealing()) {
                player.sendMessage(ChatColor.GREEN + L.m("You apply the leaves over your wounds..."));
            } else {
                player.sendMessage(ChatColor.GREEN + L.m("You apply the leaves over ") + healTarget.getName());
                healTarget.sendMessage(ChatColor.GREEN + player.getName() + L.m(" apply leaves over you..."));
            }
        }

        // FINISHED HEALING
        if (loops == loopsToComplete) {
            SkillFormulas.levelUpSkill(player, SkillEnum.Healing, healingSkill-10, SkillEnum.Healing.expRatio);
            player.removeMetadata("healing", NewKom.instancia);
            if (this.isSelfHealing()) {
                player.sendMessage(ChatColor.GREEN + L.m("You healed yourself !"));
                double life = player.getHealth() + this.ammountToHeal;
                if (life > player.getMaxHealth()) {
                    life = player.getMaxHealth();
                }
                player.setHealth(life);
            } else {
                healTarget.sendMessage(ChatColor.GREEN + player.getName() + L.m(" healed you !"));
                player.sendMessage(ChatColor.GREEN + L.m("You finished healing ") + healTarget.getName());
                double life = healTarget.getHealth() + this.ammountToHeal;
                if (life > healTarget.getMaxHealth()) {
                    life = healTarget.getMaxHealth();
                }
                healTarget.setHealth(life);
            }
            return;
        }
        loops++;
        Bukkit.getScheduler().scheduleSyncDelayedTask(NewKom.instancia, this, 60);
    }

    public void disturb(double damage) {
        int disturbedHealing = (int)damage/3;
        this.ammountToHeal -= disturbedHealing;
        player.sendMessage(ChatColor.RED+L.m("Your fingers slip !"));
    }

    public boolean isSelfHealing() {
        return (player.getName().equalsIgnoreCase(healTarget.getName()));
    }

    public void cancel() {
        if (player != null) {
            player.removeMetadata("healing", NewKom.instancia);
        }
        if (player.isOnline()) {
            player.sendMessage(ChatColor.RED + L.m("You stopped healing..."));
        }
        if (healTarget != null && healTarget.isOnline()) {
            healTarget.sendMessage(ChatColor.RED + L.m("You are not being healed anymore !"));
        }
    }

}
