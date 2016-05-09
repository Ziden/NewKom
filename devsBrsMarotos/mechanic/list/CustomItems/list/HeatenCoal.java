/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.CustomItems.list;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.list.CustomItems.CustomItem;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.RecipeBooks.BookTypes;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author User
 */
public class HeatenCoal extends CustomItem {

    public HeatenCoal() {
        super(Material.COAL, L.m("Heaten Coal"), L.m("Use to forge items !"));
    }

    @Override
    public void specialThreat(ItemStack ss) {

    }

    @Override
    public void interage(PlayerInteractEvent ev) {
      
    }

    public boolean displayOnItems() {
        return false;
    }

   
    @Override
    public boolean blockInteract() {
        return false;
    }

}
