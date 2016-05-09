/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.CustomItems.list;

import devsBrsMarotos.mechanic.list.CustomItems.list.*;
import devsBrsMarotos.mechanic.list.CustomItems.CustomItem;
import devsBrsMarotos.mechanic.list.CustomItems.CustomItem;
import devsBrsMarotos.mechanic.list.Lang.L;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 *
 * @author User
 */
public class LockPick extends CustomItem {

    public LockPick() {
        super(Material.STICK, L.m("LockPick"), L.m("Open locked stuff !"));
    }

    @Override
    public void interage(PlayerInteractEvent ev) {
         if(ev.getClickedBlock()==null || (ev.getClickedBlock().getType()!=Material.CHEST && ev.getClickedBlock().getType()!=Material.WOODEN_DOOR)) {
             ev.getPlayer().sendMessage(ChatColor.RED+L.m("Use this on locked doors or chests to break the lock"));
         }
    }

    @Override
    public boolean blockInteract() {
        return false;
    }
}
