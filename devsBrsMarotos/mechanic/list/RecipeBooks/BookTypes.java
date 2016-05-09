/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.RecipeBooks;

import devsBrsMarotos.mechanic.list.Lang.L;
import org.bukkit.ChatColor;

/**
 *
 * @author User
 */
public enum BookTypes {

    Magery, Chivalry, Focus, Alchemy;

    public static String getFirstPage(BookTypes type) {
       String name = getRecipeTitle(type);
        String page = "";
        page += ChatColor.DARK_AQUA + L.m(type.name() + " Book\n\nDrag " + type.name() + " " + name + "s into the book to add them !\n");
        if (type == Alchemy) {
            page += L.m("To create potions, add ingredients to a Cauldron to make the potion extract !\n\nThen brew the potion extract with the right brewing ingredient to make the potion !");
        } else if (type == Magery) {
            page += L.m("Use your mouse clicks with or without shift to summon elements, combine them to cast spells !");
        }
        return page;
    }

    public static String getRecipeTitle(BookTypes type) {
        String name = L.m("Recipe");
        if (type == BookTypes.Magery || type == BookTypes.Focus || type == BookTypes.Chivalry) {
            name = L.m("Scroll");
        }
        return name;
    }

}
