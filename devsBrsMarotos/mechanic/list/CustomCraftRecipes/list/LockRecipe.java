/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.CustomCraftRecipes.list;

import devsBrsMarotos.mechanic.list.CustomCraftRecipes.CustomRecipe;
import devsBrsMarotos.mechanic.list.CustomItems.CustomItem;
import devsBrsMarotos.mechanic.list.CustomItems.list.Lock;
import devsBrsMarotos.mechanic.list.CustomItems.list.RegenHeart;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author User
 *
 */
public class LockRecipe extends CustomRecipe {

    private ItemStack[][] recipe;

    public LockRecipe() {
        super("Lock Recipe", CustomItem.getItem(Lock.class).generateItem(1));
        recipe = new ItemStack[][]{
            {null,new ItemStack(Material.IRON_INGOT), new ItemStack(Material.STICK, 1)},
            {null, new ItemStack(Material.STICK, 1), new ItemStack(Material.STICK, 1)},
            {null, new ItemStack(Material.STICK, 1), null}
        };
    }

    @Override
    public ItemStack[][] getRecipe() {
        return recipe;
    }

}
