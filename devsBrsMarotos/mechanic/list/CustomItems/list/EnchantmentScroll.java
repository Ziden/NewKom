/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.CustomItems.list;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.list.CustomItems.CustomItem;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.RecipeBooks.BookTypes;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author User
 */
public class EnchantmentScroll extends CustomItem {

    public EnchantmentScroll() {
        super(Material.PAPER, L.m("Enchantment Scroll"), L.m("Place on a enchantment table !"));
    }

    @Override
    public void specialThreat(ItemStack ss) {

    }

    @Override
    public void interage(PlayerInteractEvent ev) {

    }

    public boolean displayOnItems() {
        return false;
    }

    public static ItemStack create(Enchantment ench) {
        ItemStack ss = CustomItem.getItem(EnchantmentScroll.class).generateItem(1);
        ItemMeta meta = ss.getItemMeta();
        List<String> lore = meta.getLore();

        lore.add(0, ChatColor.YELLOW + "Enchantment: §b" + NewKom.trad.getEnchantmentName(ench));
        meta.setDisplayName(ChatColor.YELLOW + "Enchantment Scroll");
        meta.setLore(lore);
        ss.setItemMeta(meta);
        return ss;
    }

    @Override
    public boolean blockInteract() {
        return false;
    }

    public static Enchantment getFromScroll(ItemStack i) {

        if (i == null) {
            return null;
        }
        CustomItem c = CustomItem.getCustomItem(i);
        if (c != null && c instanceof EnchantmentScroll) {
            for (String s : i.getItemMeta().getLore()) {
                s = ChatColor.stripColor(s);
                if (s.startsWith("Enchantment: ")) {
                    String name = s.split(":")[1].trim();
                    for (Enchantment ench : Enchantment.values()) {
                        if (NewKom.trad.getEnchantmentName(ench).equals(name)) {
                            return ench;
                        }
                    }
                }
            }
        }
        return null;

    }

}
