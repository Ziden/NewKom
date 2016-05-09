package devsBrsMarotos.mechanic.list.CustomCraftRecipes;

import devsBrsMarotos.mechanic.list.CustomItems.*;
import devsBrsMarotos.mechanic.list.CustomItems.list.Heart;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryCustom;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author vntgasl
 *
 */
public abstract class CustomRecipe {

    public CustomRecipe(String name, ItemStack result) {
        this.name = name;
        this.result = result;
    }

    public abstract ItemStack[][] getRecipe();

    public ItemStack result;

    public String name;

    public void showRecipe(Player p) {
        ItemStack[][] recipe = getRecipe();
        Inventory inv = Bukkit.createInventory(p, InventoryType.CHEST, "Custom Recipe");
     
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                ItemStack ingred = recipe[x][y];
                if (ingred != null) {
                      inv.setItem(x+(y*9), ingred);
                }
            }
        }
        inv.setItem(12, result);
        p.openInventory(inv);
        //inv.setItem(0, result);
        //inv.setMatrix(matrix);
        //inv.setResult(result);

    }

}
