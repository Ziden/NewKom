/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.RecipeBooks;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.CustomItems.CustomItem;
import devsBrsMarotos.mechanic.list.CustomItems.list.RecipeBook;
import devsBrsMarotos.mechanic.list.CustomItems.list.RecipeForBook;
import devsBrsMarotos.mechanic.list.Lang.L;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author User
 */
public class RecipeBooksEvents extends Mecanica {

    @Override
    public String getName() {
        return "Recipe Books";
    }

    @EventHandler
    public void click(InventoryClickEvent ev) {
        if (ev.getWhoClicked().getType() == EntityType.PLAYER && ev.getCurrentItem() != null && ev.getCurrentItem().getType() == Material.WRITTEN_BOOK && ev.getCursor() != null && ev.getCursor().getType() == Material.PAPER) {

            CustomItem item = CustomItem.getCustomItem(ev.getCurrentItem());
            if (item != null && item instanceof RecipeBook) {
                RecipeBook book = (RecipeBook) item;
                BookTypes bookType = book.getBookType(ev.getCurrentItem());

                CustomItem recipe = CustomItem.getCustomItem(ev.getCursor());
                if (recipe != null && recipe instanceof RecipeForBook) {
                    RecipeForBook recipePage = (RecipeForBook) recipe;
                    BookTypes recipeType = recipePage.getBookType(ev.getCursor());
                    Player p = (Player) ev.getWhoClicked();
                    if (!recipeType.name().equalsIgnoreCase(bookType.name())) {
                        p.sendMessage(ChatColor.RED + L.m("This book only accepts "+BookTypes.getRecipeTitle(bookType)+" for ") + bookType.name());
                        ev.setCancelled(true);
                        return;
                    }
                    
                    List<String> recipes = book.getRecipes(ev.getCurrentItem());
                    
                    RecipePage page = RecipePage.getPage(ev.getCursor());
                    if(page==null) {
                        NewKom.log.info("ERRORRRR: PAGE IS NULL !!");
                    }
                    if(recipes != null && recipes.contains(page.name)) {
                        p.sendMessage(ChatColor.RED+L.m("This book already have this "+BookTypes.getRecipeTitle(bookType)+" !"));
                        ev.setCancelled(true);
                        return;
                    }
                    book.addToBook(ev.getCurrentItem(), page);
                    ev.setResult(Event.Result.DENY);
                    ev.setCursor(new ItemStack(Material.AIR));
                    p.sendMessage(ChatColor.GREEN + L.m("You added the "+BookTypes.getRecipeTitle(bookType)+" to your ") + bookType.name() + L.m(" book !"));
                }
            }

        }
    }

}
