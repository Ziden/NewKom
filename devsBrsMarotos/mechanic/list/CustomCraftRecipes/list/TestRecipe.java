/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.CustomCraftRecipes.list;

import devsBrsMarotos.mechanic.list.CustomCraftRecipes.CustomRecipe;
import devsBrsMarotos.mechanic.list.CustomItems.CustomItem;
import devsBrsMarotos.mechanic.list.CustomItems.list.RegenHeart;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author User
 *
 */
public class TestRecipe extends CustomRecipe {

    private ItemStack[][] recipe;

    public TestRecipe() {
        super("Test Recipe", new ItemStack(Material.COAL, 64));
        recipe = new ItemStack[][]{
            {null, CustomItem.getItem(RegenHeart.class).generateItem(2), null},
            {null, new ItemStack(Material.APPLE, 1), null},
            {null, null, null}
        };
    }

    @Override
    public ItemStack[][] getRecipe() {
        return recipe;
    }

}
