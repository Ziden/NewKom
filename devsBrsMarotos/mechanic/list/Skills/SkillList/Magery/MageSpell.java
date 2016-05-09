/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills.SkillList.Magery;

import devsBrsMarotos.mechanic.list.RecipeBooks.BookTypes;
import devsBrsMarotos.mechanic.list.RecipeBooks.HaveRecipe;
import devsBrsMarotos.mechanic.list.RecipeBooks.RecipePage;
import devsBrsMarotos.mechanic.list.Skills.SkillList.Alchemy.CustomPotions.CustomPotion;
import devsBrsMarotos.mechanic.list.Skills.SkillList.Alchemy.CustomPotions.PotionLoader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author User
 * 
 */
public abstract class MageSpell implements HaveRecipe{

    public MageSpell(String name) {
        this.name = name;
    }
    
    public String name;
    
    public abstract void cast(Player caster);
    public abstract double getManaCost();
    public abstract double getExpRatio();
    public abstract double getMinSkill();
    public abstract Elements [] getElements();
    public abstract int getCooldownInSeconds();
    
    public static void showRecipes(Player p) {
        Inventory inv = Bukkit.createInventory(p, InventoryType.CHEST, "Mage Scrolls");
        for (MageSpell cu : SpellLoader.spells.values()) {
            inv.addItem(cu.generateRecipe().getItem());
        }
        p.openInventory(inv);
    }
    
    @Override
    public RecipePage generateRecipe() {
         RecipePage page = new RecipePage();

        String text = ChatColor.GREEN + "Elements:\n\n";
        for (Elements ss : getElements()) {
            text += (ChatColor.BLACK + "- " +ss.icon + " "+ss.name() + "\n");
        }
        text+= ChatColor.DARK_BLUE+"Mana Cost:"+ChatColor.DARK_GREEN+getManaCost()+"\n";
        text+= ChatColor.DARK_BLUE+"Minimum Skill: "+ChatColor.DARK_GREEN+getMinSkill()+"\n";
        text+= ChatColor.DARK_BLUE+"Cooldown: "+ChatColor.DARK_GREEN+getCooldownInSeconds();
        page.createPage(name, text, BookTypes.Magery);
        return page;
    }
    
}
