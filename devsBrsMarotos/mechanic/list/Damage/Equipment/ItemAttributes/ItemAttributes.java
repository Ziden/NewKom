/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Damage.Equipment.ItemAttributes;

import devsBrsMarotos.mechanic.list.Damage.Equipment.EquipMeta;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author vntgasl
 */
public class ItemAttributes {

    public static void subAttribute(ItemStack ss, AttributeEnum attribute, int qtd) {
        ItemMeta meta = ss.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<String>();
        }
        // if already have this attribute, we sum
        for (String loreIn : lore) {
            if (loreIn.startsWith("§9+ ") && loreIn.endsWith(attribute.name())) {
                int hasAttribute = Integer.valueOf(loreIn.split(" ")[1]);
                hasAttribute -= qtd;
                if (hasAttribute < 0) {
                    hasAttribute = 0;
                }
                lore.remove(loreIn);
                lore.add(0, "§9+ " + hasAttribute + " " + attribute.name());
                meta.setLore(lore);
                ss.setItemMeta(meta);
                return;
            }
        }
        return;
    }

    public static EquipMeta getAttributes(ItemStack ss) {
        HashMap<AttributeEnum, Integer> attrs = new HashMap();
        ItemMeta meta = ss.getItemMeta();
        if (meta == null) {
            return new EquipMeta(attrs);
        }
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<String>();
        }
        // if already have this attribute, we sum
        for (String loreIn : lore) {
            if (loreIn.startsWith("§9+")) {
                String attrName = "";
                //+9 Physical Damage
                for (int x = 1; x < loreIn.split(" ").length; x++) {
                    attrName += loreIn.split(" ")[x];
                    if (x != loreIn.split(" ").length - 1) {
                        attrName += "_";
                    }
                }
                AttributeEnum attr = AttributeEnum.valueOf(attrName);
                int value = Integer.valueOf(loreIn.split("\\+")[1].split(" ")[0]);
              
                attrs.put(attr, value);
            }
        }
        return new EquipMeta(attrs);
    }

    public static int getAttribute(ItemStack ss, AttributeEnum attribute) {
        ItemMeta meta = ss.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<String>();
        }
        // if already have this attribute, we sum
        for (String loreIn : lore) {
            if (loreIn.startsWith("§9+") && loreIn.endsWith(attribute.name())) {
                return Integer.valueOf(loreIn.split("\\+")[1].split(" ")[0]);
            }
        }
        return 0;
    }

    public static void addAttribute(ItemStack ss, AttributeEnum attribute, int qtd) {
        ItemMeta meta = ss.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<String>();
        }
        // if already have this attribute, we sum
        for (String loreIn : lore) {
            if (loreIn.startsWith("§9+") && loreIn.endsWith(attribute.name().replace("_", " "))) {
                int hasAttribute = Integer.valueOf(loreIn.split(" ")[1]);
                hasAttribute += qtd;
                lore.remove(loreIn);
                lore.add(0, "§9+" + hasAttribute + " " + attribute.name().replace("_", " "));
                meta.setLore(lore);
                ss.setItemMeta(meta);
                return;
            }
        }
        // doesnt have the attribute, we add
        lore.add(0, "§9+" + qtd + " " + attribute.name().replace("_", " "));
        meta.setLore(lore);
        ss.setItemMeta(meta);
        return;
    }

}
