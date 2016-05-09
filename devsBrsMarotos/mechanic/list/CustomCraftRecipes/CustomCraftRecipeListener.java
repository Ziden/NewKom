package devsBrsMarotos.mechanic.list.CustomCraftRecipes;

import devsBrsMarotos.mechanic.list.CustomItems.*;
import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.list.CustomItems.CustomItem;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.util.GeneralUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author vntgasl
 *
 */
public class CustomCraftRecipeListener extends Mecanica {

    @Override
    public void onEnable() {

    }

    @Override
    public String getName() {
        return "Custom Items";
    }

    @EventHandler
    public void invClick(final InventoryClickEvent ev) {
        
        NewKom.log.info("SLOT "+ev.getRawSlot());
        
        if(ev.getInventory().getName().equalsIgnoreCase("Custom Recipes") || ev.getInventory().getTitle().equalsIgnoreCase("Custom Recipes")) {
            ev.setCancelled(true);
            ItemStack clicked = ev.getCurrentItem();
            if(clicked!=null) {
                for(final CustomRecipe recipe : RecipeLoader.craftRecipes.values()) {
                    if(GeneralUtils.isEqual(recipe.result, clicked)) {
                        ev.getWhoClicked().closeInventory();
                        Runnable r = new Runnable() {
                            public void run() {
                                recipe.showRecipe((Player)ev.getWhoClicked());
                            }
                        };
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, r, 10);
                        
                    }
                }
            }
        } else   if(ev.getInventory().getName().equalsIgnoreCase("Custom Recipe") || ev.getInventory().getTitle().equalsIgnoreCase("Custom Recipe")) {
            ev.setCancelled(true);
        }
    }
    
    @EventHandler
    public void itemCraft(PrepareItemCraftEvent ev) {
        ItemStack result = ev.getInventory().getResult();
        for (CustomRecipe recipe : RecipeLoader.craftRecipes.values()) {
            // it is a custom recipe !
            if (recipe.result.equals(result)) {
                ItemStack[][] ingredientsNeeded = recipe.getRecipe();
                int x = 0;
                int y = 0;
                // lets test for custom items in formula
                for (int iX = 0; iX < 9; iX++) {
                    ItemStack ingredient = ev.getInventory().getMatrix()[iX];
                    ItemStack needIngredient = ingredientsNeeded[y][x];
                    NewKom.log.info("X " + x + " Y " + y);
                    if (needIngredient != null) {
                        if (ingredient == null) {
                            NewKom.log.info("INGRED NULL");
                            ev.getInventory().setResult(new ItemStack(Material.AIR));
                            return;
                        }
                    }
                    if (needIngredient != null && ingredient != null && !GeneralUtils.isEqual(ingredient, needIngredient)) {
                        ev.getInventory().setResult(new ItemStack(Material.AIR));
                        NewKom.log.info("NAO DEU =");
                        return;
                    }

                    x += 1;
                    if (x == 3) {
                        x = 0;
                        y += 1;
                    }
                }
            }
        }
    }

}
