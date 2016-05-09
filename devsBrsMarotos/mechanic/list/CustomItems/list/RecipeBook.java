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
import java.util.ArrayList;
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
public class RecipeBook extends CustomItem {

    public RecipeBook() {
        super(Material.WRITTEN_BOOK, L.m("Recipe Book"), L.m("Place Recipes Here !"));
    }

    public static ItemStack createBook(BookTypes type) {
        ItemStack ss = CustomItem.getItem(RecipeBook.class).generateItem(1);
        ItemMeta meta = ss.getItemMeta();
        List<String> lore = meta.getLore();
        lore.add(0,ChatColor.YELLOW+L.m("Book of ")+type.name());
        meta.setDisplayName(ChatColor.YELLOW+L.m("Book of ")+type.name());
        meta.setLore(lore);
        ss.setItemMeta(meta);
        BookMeta bookMeta = (BookMeta)ss.getItemMeta();
        bookMeta.setAuthor(type.name()+" Master");
        bookMeta.addPage(BookTypes.getFirstPage(type));
        ss.setItemMeta(bookMeta);
        return ss;
    }
    
   public void addToBook(ItemStack book, RecipePage recipe) {
        BookMeta meta = (BookMeta)book.getItemMeta();
        if(meta.getPages()==null)
            meta.setPages(new ArrayList<String>());
        List<String> pages = new ArrayList<String>(meta.getPages());
        String fullPage = BookTypes.getRecipeTitle(recipe.bookType)+":"+recipe.name+": \n\n"+recipe.text;
        pages.add(fullPage);
        meta.setPages(pages);
        book.setItemMeta(meta);
    }
    
    public List<String> getRecipes(ItemStack book) {
        BookMeta meta = (BookMeta)book.getItemMeta();
        List<String> recipes = new ArrayList<String>();
        if(meta.getPages() == null || meta.getPages().size()==0) {
            return recipes;
        }
        for(int x = 1 ; x < meta.getPages().size() ; x++) {
            String page = meta.getPages().get(x);
             String recipeName = page.split("\n")[0].split(":")[1];
             recipes.add(recipeName);
        }
        return recipes;
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
       BookMeta meta =(BookMeta)ss.getItemMeta();
       meta.setAuthor("Jabu");
       ss.setItemMeta(meta);
    }

    @Override
    public void interage(PlayerInteractEvent ev) {
    }
    
    @Override
    public boolean blockInteract() {
        return false;
    }

    public boolean isBlessed() {
        return true;
    }
    
    public boolean displayOnItems() {
        return false;
    }
    
    
}
