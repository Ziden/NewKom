/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.CustomItems;

import devsBrsMarotos.mechanic.Mecanica;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author User
 */
public class ItemProtections extends Mecanica {

    @Override
    public String getName() {
        return "Item Protection";
    }

    public HashMap<UUID, List<ItemStack>> savedItems = new HashMap<UUID, List<ItemStack>>();

    public boolean isBlessed(ItemStack ss) {
        ItemMeta m = ss.getItemMeta();
        List<String> lore = m.getLore();
        if (lore != null) {
            for (String l : lore) {
                if (l.contains("* Blessed *") || l.contains("* Bound *")) {
                    return true;
                }
            }
        }
        return false;
    }

    @EventHandler
    public void respawn(final PlayerRespawnEvent ev) {
        Runnable r = new Runnable() {
            public void run() {
                if (savedItems.containsKey(ev.getPlayer().getUniqueId())) {
                    List<ItemStack> saved = savedItems.get(ev.getPlayer().getUniqueId());
                    ev.getPlayer().getInventory().addItem(savedItems.get(ev.getPlayer().getUniqueId()).toArray(new ItemStack[saved.size()]));
                    savedItems.remove(ev.getPlayer().getUniqueId());
                }
            }
        };
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, r, 5);
    }
    
    
    @EventHandler
    public void join(final PlayerJoinEvent ev) {
        Runnable r = new Runnable() {
            public void run() {
                if (savedItems.containsKey(ev.getPlayer().getUniqueId())) {
                    List<ItemStack> saved = savedItems.get(ev.getPlayer().getUniqueId());
                    ev.getPlayer().getInventory().addItem(savedItems.get(ev.getPlayer().getUniqueId()).toArray(new ItemStack[saved.size()]));
                    savedItems.remove(ev.getPlayer().getUniqueId());
                }
            }
        };
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, r, 5);
    }

    @EventHandler
    public void playerDies(PlayerDeathEvent ev) {
        List<ItemStack> savedItems = new ArrayList<ItemStack>();
        for (ItemStack ss : ev.getEntity().getInventory().getContents()) {
            if(ss==null || ss.getType()==Material.AIR) continue;
            if (isBlessed(ss)) {
                savedItems.add(ss);
            }
        }
        for (ItemStack ss : ev.getEntity().getInventory().getArmorContents()) {
            if(ss==null || ss.getType()==Material.AIR) continue;
            if (isBlessed(ss)) {
                savedItems.add(ss);
            }
        }
        ev.getDrops().removeAll(savedItems);
        this.savedItems.put(ev.getEntity().getUniqueId(), savedItems);
    }

}
