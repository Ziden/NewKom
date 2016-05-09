/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.MobConfigs;

import devsBrsMarotos.DB.MobConfigDB;
import devsBrsMarotos.mechanic.Mecanica;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 *
 * @author ciro
 */
public class MobEvents extends Mecanica {

    @Override
    public String getName() {
        return "Mob Configs";
    }

    @EventHandler
    public void spawnMob(CreatureSpawnEvent ev) {
        if (ev.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
            ev.setCancelled(true);
        }

    }

    @EventHandler
    public void removeMob(InventoryClickEvent ev) {
        if (ev.isCancelled()) {
            return;
        }
        Player p = (Player) ev.getWhoClicked();
        if (!p.isOp()) {
            return;
        }
        if (ev.getInventory().getName().startsWith("Region: ")) {
            ev.setCancelled(true);
            if (ev.getCurrentItem() == null) {
                return;
            }
            String region = ev.getInventory().getName().split(" ")[1];
            String mob = ev.getCurrentItem().getItemMeta().getDisplayName();
            String cName = ChatColor.stripColor(ev.getCurrentItem().getItemMeta().getLore().get(2).split(" ")[2]);
            String lv = ChatColor.stripColor(ev.getCurrentItem().getItemMeta().getLore().get(0).split(" ")[1]);
            String percent = ChatColor.stripColor(ev.getCurrentItem().getItemMeta().getLore().get(1).split(" ")[1]);
            MobConfigDB.remConfigMob(p.getWorld().getName(), region, mob, percent, lv, cName);
            p.closeInventory();
            p.openInventory(MobInfo.showMobs(p, 27, "Region: " + region, MobConfigDB.cache.getCached(region).mobs));

        }
    }
}
