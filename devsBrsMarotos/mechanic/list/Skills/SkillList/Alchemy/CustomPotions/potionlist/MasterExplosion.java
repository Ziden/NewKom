/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills.SkillList.Alchemy.CustomPotions.potionlist;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.list.Damage.Dmg;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.Skills.SkillList.Alchemy.CustomPotions.CustomPotion;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

/**
 *
 * @author User
 */
public class MasterExplosion extends CustomPotion {

    private int damage = 15;
    private DamageCause damageType = DamageCause.ENTITY_EXPLOSION;
    
    public MasterExplosion() {
        super(L.m("Master Explosion Potion"),L.m("Makes a big KBOOM"), PotionType.INSTANT_DAMAGE, true);
    }

    @Override
    public void interage(PlayerInteractEvent ev) {
        ThrownPotion thrownPotion = ev.getPlayer().launchProjectile(ThrownPotion.class);
        thrownPotion.setItem(new ItemStack(ev.getPlayer().getItemInHand()));
        thrownPotion.setShooter(ev.getPlayer());
        this.consome(ev.getPlayer());
    }

    @Override
    public void splashEvent(PotionSplashEvent ev) {
       ev.getEntity().getWorld().playEffect(ev.getPotion().getLocation(), Effect.EXPLOSION, 2);
       for(Entity e : ev.getPotion().getNearbyEntities(5, 5, 5)) {
           if(e instanceof LivingEntity) {
               Dmg.dealDamage((LivingEntity)ev.getEntity().getShooter(), (LivingEntity)e, damage, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION);
           }
       }
    }

    @Override
    public ItemStack[] getRecipe() {
        return new ItemStack [] {
        new ItemStack(Material.QUARTZ, 1), 
        new ItemStack(Material.POTION,1), 
        new ItemStack(Material.COAL,1)};
    }

    
    @Override
    public double getMinimumSkill() {
        return 80;
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
        
    }
    
}
