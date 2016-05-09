/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Damage;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.Damage.Equipment.EquipManager;
import devsBrsMarotos.mechanic.list.Damage.Equipment.ItemAttributes.AttributeEnum;
import devsBrsMarotos.mechanic.list.Damage.Equipment.ItemAttributes.ItemAttributes;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author User
 */
public class Dmg extends Mecanica {

    public static boolean canDamage(LivingEntity damager, LivingEntity damaged) {
        /// TO-DO
        return true;
    }

    public static double getDamageReducedByArmor(Player player) {
        double red = 0;

        double armor = (double) EquipManager.getPlayerAttribute(AttributeEnum.Armor, player);
        //NewKom.log.info("ARMOR = " + armor);
        red += armor / 100; // each armor adds 1% resistance
        //NewKom.log.info("RED = " + red);
        return red;
    }

    /////// ARMOR WILL PROTECT DAMAGE //////
    public static List<DamageCause> armorProtects = Arrays.asList(new DamageCause[]{
        DamageCause.CONTACT,
        DamageCause.FALLING_BLOCK,
        DamageCause.ENTITY_ATTACK,
        DamageCause.ENTITY_EXPLOSION,
        DamageCause.PROJECTILE,
        DamageCause.THORNS
    });

    @EventHandler
    public void dealDamage(EntityDamageByEntityEvent ev) {

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void takeDamage(EntityDamageEvent ev) {

        if (ev.isCancelled()) {
            return;
        }
        if (ev.getEntity() instanceof LivingEntity) {
            LivingEntity e = (LivingEntity) ev.getEntity();
            if (e.getNoDamageTicks() > 0) {
                ev.setCancelled(true);
                return;
            }

            double damage = ev.getDamage();
            double finalDamage = damage;

            if (armorProtects.contains(ev.getCause()) && ev.getEntity().getType() == EntityType.PLAYER) {

                // resisting damage from ARMOR
                double resisted = getDamageReducedByArmor((Player) ev.getEntity()) * finalDamage;
                //NewKom.log.info("RESIST: " + resisted);
                finalDamage = finalDamage - resisted;
            }

            if (finalDamage > 0 && !ev.isCancelled()) {
                e.setNoDamageTicks(20);
                double life = e.getHealth() - finalDamage;
                if (life < 0) {
                    life = 0;
                }
                e.setLastDamageCause(ev);
                e.setLastDamage(finalDamage);
                e.setHealth(life);
                e.playEffect(EntityEffect.HURT);
                //NewKom.log.info("DANO FINAL: " + finalDamage);
                //NewKom.log.info("VIDA FINAL: " + e.getHealth());
                ev.setCancelled(true);
            }
        }

    }

    public static void dealDamage(LivingEntity damager, LivingEntity damaged, double damage, DamageCause cause) {
        EntityDamageByEntityEvent dmgEvent = new EntityDamageByEntityEvent(damager, damaged, cause, damage);
        Bukkit.getServer().getPluginManager().callEvent(dmgEvent);
    }

    @Override
    public String getName() {
        return "Damage";
    }

}
