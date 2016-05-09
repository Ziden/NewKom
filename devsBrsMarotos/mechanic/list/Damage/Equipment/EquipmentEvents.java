/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Damage.Equipment;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.Damage.Equipment.ItemAttributes.ItemAttributes;
import devsBrsMarotos.mechanic.list.Damage.Equipment.ItemAttributes.ItemDefaultAttributes;
import devsBrsMarotos.mechanic.list.Skills.SkillEvents.PlayerEquipEvent;
import devsBrsMarotos.mechanic.list.Skills.SkillEvents.PlayerUnequipEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author vntgasl
 */
public class EquipmentEvents extends Mecanica {
    
    @Override
    public String getName() {
       return "Armor Events";
    }
    
    @EventHandler
    public void itemHeld(PlayerItemHeldEvent  ev) {
        ItemStack old = ev.getPlayer().getInventory().getItem(ev.getPreviousSlot());
        ItemStack niw = ev.getPlayer().getInventory().getItem(ev.getNewSlot());
        if(niw!=null)
            NewKom.log.info("NEW "+niw.toString());
        if(ItemDefaultAttributes.isWeapon(niw)) {
            NewKom.log.info("EQUIP NEW");
            Bukkit.getServer().getPluginManager().callEvent(new PlayerEquipEvent(ev.getPlayer(), niw));
        }
        if(old!=null && ItemDefaultAttributes.isWeapon(old)) {
             NewKom.log.info("UNEQUIP");
            Bukkit.getServer().getPluginManager().callEvent(new PlayerUnequipEvent(ev.getPlayer(), old));
        }
    }
    
    @EventHandler
    public void equip(PlayerEquipEvent ev) {
        if(ev.getItem()==null || ev.getItem().getType()==Material.AIR)
            return;
        EquipMeta itemMeta = ItemAttributes.getAttributes(ev.getItem());
        EquipMeta playerEquipMeta = EquipManager.getPlayerEquipmentMeta(ev.getPlayer());
        EquipMeta.addMeta(playerEquipMeta, itemMeta);
        EquipManager.setPlayerEquipmentMeta(ev.getPlayer(), playerEquipMeta);
    }
    
    @EventHandler
    public void unequip(PlayerUnequipEvent ev) {
        if(ev.getItem()==null || ev.getItem().getType()==Material.AIR)
            return;
        EquipMeta itemMeta = ItemAttributes.getAttributes(ev.getItem());
        EquipMeta playerEquipMeta = EquipManager.getPlayerEquipmentMeta(ev.getPlayer());
        EquipMeta.subMeta(playerEquipMeta, itemMeta);
        EquipManager.setPlayerEquipmentMeta(ev.getPlayer(), playerEquipMeta);
    }
    
}
