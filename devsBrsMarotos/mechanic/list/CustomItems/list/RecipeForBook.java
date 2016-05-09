/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.CustomItems.list;

import devsBrsMarotos.mechanic.list.CustomItems.CustomItem;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.RecipeBooks.BookTypes;
import devsBrsMarotos.mechanic.list.RecipeBooks.RecipePage;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 *
 * @author User
 */
public class RecipeForBook extends CustomItem {

    public RecipeForBook() {
        super(Material.PAPER, L.m("Recipe for Book"), L.m("Place on a recipe book !"));
    }
    
    public BookTypes getBookType(ItemStack book) {
        ItemMeta meta = book.getItemMeta();
        String firstLine = meta.getLore().get(0);
        try {
            return BookTypes.valueOf(firstLine.split(" ")[2]);
        } catch(Exception e) {
            return null;
        }
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

}
