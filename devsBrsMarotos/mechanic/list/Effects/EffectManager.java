/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Effects;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 *
 * @author User
 */
public class EffectManager {
    
    public static void addEffect(LivingEntity e, PotionEffectType p, int dur, int amp) {
        e.addPotionEffect(new PotionEffect(p,dur, amp));
    }
    
}
