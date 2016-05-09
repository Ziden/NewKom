/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Damage.Equipment;

import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.Damage.Equipment.ItemAttributes.AttributeEnum;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 *
 * @author vntgasl
 */
public class WeaponEvents extends Mecanica {

    @Override
    public String getName() {
       return "Weapon Events";
    }
    
    
    ///////////  PVP ONLY /////////////
    @EventHandler(priority=EventPriority.LOW)
    public void entityDamage(EntityDamageByEntityEvent ev) {
        // player hits a player
        if(ev.getDamager().getType()==EntityType.PLAYER && ev.getCause()==DamageCause.ENTITY_ATTACK) {
            EquipMeta meta = EquipManager.getPlayerEquipmentMeta((Player)ev.getDamager());
            double neutralDamage = 0;
            if(meta.attributes.containsKey(AttributeEnum.Physical_Damage))
                neutralDamage += meta.attributes.get(AttributeEnum.Physical_Damage);
            ev.setDamage(neutralDamage==0?1:neutralDamage);
        //// PROJECTILE DAMAGE /////
        } else if(ev.getDamager() instanceof Projectile && ((Projectile)ev.getDamager()).getShooter() instanceof Player) {
            if(ev.getDamager() instanceof Arrow) {
                Player shooter = (Player)((Projectile)ev.getDamager()).getShooter();
                EquipMeta meta = EquipManager.getPlayerEquipmentMeta((Player)ev.getDamager());
                double neutralDamage = 0;
                    if(meta.attributes.containsKey(AttributeEnum.Bow_Damage))
                neutralDamage += meta.attributes.get(AttributeEnum.Bow_Damage);
                ev.setDamage(neutralDamage==0?1:neutralDamage);
            } 
        }
    }
    
}
