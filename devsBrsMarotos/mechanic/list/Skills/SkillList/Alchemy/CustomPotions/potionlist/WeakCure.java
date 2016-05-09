/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills.SkillList.Alchemy.CustomPotions.potionlist;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.Skills.SkillList.Alchemy.CustomPotions.CustomPotion;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

/**
 *
 * @author User
 */
public class WeakCure extends CustomPotion {

    public WeakCure() {
        super(L.m("Weak Cure Potion"),L.m("Cure Small Poisons"), PotionType.INSTANT_HEAL, false);
    }

    @Override
    public void interage(PlayerInteractEvent ev) {
      
    }

    @Override
    public void splashEvent(PotionSplashEvent ev) {
   
       
    }

    @Override
    public ItemStack[] getRecipe() {
        return new ItemStack [] {
        new ItemStack(Material.LEAVES, 1), 
        new ItemStack(Material.POTION,1), 
        new ItemStack(Material.APPLE,1)};
    }

    
    @Override
    public double getMinimumSkill() {
        return 1;
    }

    @Override
    public double getExpRatio() {
        return 1d;
    }

    @Override
    public ItemStack brewWith() {
       return new ItemStack(Material.SUGAR, 1);
    }

    @Override
    public void drink(PlayerItemConsumeEvent ev) {
        if(ev.getPlayer().hasPotionEffect(PotionEffectType.POISON)) {
            for(PotionEffect effect : ev.getPlayer().getActivePotionEffects()) {       
                if(effect.getType().getName()==PotionEffectType.POISON.getName()) {
                    if(effect.getAmplifier() <= 2) {
                        ev.getPlayer().removePotionEffect(PotionEffectType.POISON);
                        ev.getPlayer().sendMessage(ChatColor.GREEN+L.m("You were healed from the poison !"));
                    } else {
                        ev.getPlayer().sendMessage(ChatColor.RED+L.m("This poison is too strong to be healed !"));
                    }
                }
            }
        }
    }
    
}
