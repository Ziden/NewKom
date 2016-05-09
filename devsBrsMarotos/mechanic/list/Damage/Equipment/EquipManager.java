/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Damage.Equipment;

import devsBrsMarotos.MetaShit;
import devsBrsMarotos.mechanic.list.Skills.SkillEvents.PlayerEquipEvent;
import devsBrsMarotos.mechanic.list.Skills.SkillEvents.PlayerUnequipEvent;
import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.Damage.Equipment.ItemAttributes.AttributeEnum;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerJoinEvent;

public class EquipManager extends Mecanica {

    private final HashMap<UUID, ItemStack[]> equipment = new HashMap<UUID, ItemStack[]>();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getType() == InventoryType.CRAFTING || event.getInventory().getType() == InventoryType.PLAYER) {
              Player player = (Player) event.getWhoClicked();
            if (event.getSlotType() == InventoryType.SlotType.ARMOR || event.isShiftClick()) {
              
                checkEquips(player);
            }
            
            // if he clicks the selected item on hotbar
            if(event.getSlot()==player.getInventory().getHeldItemSlot()) {
                if(event.getCursor() != null && event.getCursor().getType() != Material.AIR) {
                    Bukkit.getServer().getPluginManager().callEvent(new PlayerEquipEvent(player, event.getCursor()));
                }
                if(event.getCurrentItem() != null && event.getCurrentItem().getType()!= Material.AIR) {
                    Bukkit.getServer().getPluginManager().callEvent(new PlayerUnequipEvent(player, event.getCurrentItem()));
                }
            }
        }
    }
    
    
    
    @EventHandler
    public void join(PlayerJoinEvent ev) {
        checkEquips(ev.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            String name = event.getPlayer().getItemInHand().getType().name();
            if (name.contains("_CHESTPLATE") || name.contains("_LEGGINGS") || name.contains("_BOOTS") || name.contains("_HELMET")) {
                checkEquips(event.getPlayer());
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent event) {
        checkEquips(event.getEntity());
    }
    
    public static int getPlayerAttribute(AttributeEnum attr, Player p) {
        EquipMeta meta = getPlayerEquipmentMeta(p);
        if(meta.attributes.containsKey(attr))
            return meta.attributes.get(attr);
        else
            return 0;
    }    
    
    
    public static EquipMeta getPlayerEquipmentMeta(Player p) {
        if(p.hasMetadata("EquipMeta")) {
            return (EquipMeta)MetaShit.getMetaObject("EquipMeta", p);
        } else {
            return new EquipMeta(new HashMap<AttributeEnum, Integer>());
        }
    }
    
    public static void setPlayerEquipmentMeta(Player p, EquipMeta meta) {
        MetaShit.setMetaObject("EquipMeta", p, meta);
    }

    private void checkEquips(final Player player) {
        Bukkit.getServer().getScheduler().runTaskLater(NewKom.instancia, new Runnable() {
            @Override
            public void run() {
                ItemStack[] equips = player.getEquipment().getArmorContents();
                ItemStack[] previous = equipment.get(player.getUniqueId());
                for (int i = 0; i < equips.length; i++) 
                {
                    /// taking a item off
                    if (equips[i] == null && (previous != null && previous[i] != null)) {
                        Bukkit.getServer().getPluginManager().callEvent(new PlayerUnequipEvent(player, previous[i]));
                        
                    // puttin a new item
                    } else if (equips[i] != null && (previous == null || previous[i] == null)) {
                        Bukkit.getServer().getPluginManager().callEvent(new PlayerEquipEvent(player, equips[i]));
                    
                    // replacing item
                    } else if (previous != null && !equips[i].toString().equalsIgnoreCase(previous[i].toString())) {
                        Bukkit.getServer().getPluginManager().callEvent(new PlayerUnequipEvent(player, previous[i]));
                        Bukkit.getServer().getPluginManager().callEvent(new PlayerEquipEvent(player, equips[i]));
                    }
                }
                equipment.put(player.getUniqueId(), equips);
            }
        }, 1);
    }


    
    @Override
    public String getName() {
       return "Equip Manager";
    }
}
