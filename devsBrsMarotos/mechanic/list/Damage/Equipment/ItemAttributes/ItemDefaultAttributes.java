/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Damage.Equipment.ItemAttributes;

import net.minecraft.server.v1_8_R3.ItemArmor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author vntgasl
 */
public class ItemDefaultAttributes {

    public static int getDefaultArmorValue(ItemStack armor) {
        if (armor.getType().name().contains("DIAMOND")) {
            return 12;
        } else if (armor.getType().name().contains("IRON") || armor.getType().name().contains("GOLD")) {
            return 10;
        } else if (armor.getType().name().contains("CHAIN")) {
            return 8;
        } else if (armor.getType().name().contains("LEATHER")) {
            return 6;
        }
        return 0;
    }

    public static int getDefaultDamage(ItemStack weapon) {
        double damage = 0;
        if(weapon==null || weapon.getType()==Material.AIR)
            return 0;
        if (weapon.getType().name().contains("_HOE") || weapon.getType().name().contains("_SPADE")) {
            damage = 1;
        } else if (weapon.getType().name().contains("_AXE") || weapon.getType().name().contains("_SWORD")) {
            damage = 2;
        }
        if (weapon.getType().name().contains("DIAMOND_")) {
            damage += 5;
        } else if (weapon.getType().name().contains("IRON_") || weapon.getType().name().contains("GOLD_")) {
            damage += 3;
        } else if (weapon.getType().name().contains("STONE_")) {
            damage += 1;
        } 
        return (int)damage;
    }

    public static boolean isArmor(ItemStack ss) {
        return (CraftItemStack.asNMSCopy(ss).getItem() instanceof ItemArmor);
    }
    
     public static boolean isWeapon(ItemStack ss) {
        return getDefaultDamage(ss) > 0;
    }
    
    

}
