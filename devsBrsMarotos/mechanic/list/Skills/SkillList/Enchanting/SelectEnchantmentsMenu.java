/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills.SkillList.Enchanting;

import devsBrsMarotos.DB.Models.EnchantmentTable;
import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.Lang.L;

import devsBrsMarotos.util.ItemUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Carlos
 */
public class SelectEnchantmentsMenu extends Mecanica {

    public static HashMap<Block, Inventory> invs = new HashMap();

    public static Inventory getInventory(Block b) {
        if (invs.containsKey(b)) {
            return invs.get(b);
        }

        Inventory i = Bukkit.createInventory(null, 27, "§lEnchantments");
        fill(i, b);
        invs.put(b, i);
        return i;
    }

    public static void fill(Inventory i, Block b) {
        i.clear();
        EnchantmentTable et = NewKom.database.ench.getEnchantment(b);

        List<Enchantment> ench = new ArrayList();
        ench.addAll(EnchantingListener.defaults);
        for (Enchantment enc : et.enchants.keySet()) {
            if (!EnchantingListener.defaults.contains(enc)) {
                ench.add(enc);
            }
        }
        for (Enchantment enc : Enchantment.values()) {
            if (!ench.contains(enc)) {
                ench.add(enc);
            }
        }
        for (Enchantment etc : ench) {
            i.addItem(getItemStack(etc, et));
        }
    }

    @EventHandler
    public void blockbreak(BlockBreakEvent ev) {

    }

    public static int getSlots(EnchantmentTable ench) {
        int x = ench.enchants.size();
        while (x % 9 != 0) {
            x++;
        }
        return x;
    }

    public static ItemStack getItemStack(Enchantment ench, EnchantmentTable table) {
        ItemStack is = new ItemStack(Material.WOOL);
        ChatColor cor = ChatColor.WHITE;
        if (!table.enchants.containsKey(ench)) {
            is.setDurability((short) 0);
            ItemUtils.AddLore(is, "§f§lYou don't have");

        } else if (!table.enchants.get(ench)) {
            is.setDurability((short) 5);
            cor = ChatColor.RED;
        } else {
            is.setDurability((short) 14);
            cor = ChatColor.GREEN;
        }
        if (EnchantingListener.defaults.contains(ench)) {
            is.setDurability((short) 4);
            cor = ChatColor.YELLOW;
        }

        if (EnchantingListener.defaults.contains(ench)) {
            ItemUtils.AddLore(is, "§f§lDEFAULT");
        }
        
        ItemUtils.SetItemName(is, cor + "§lEnchantment: " + NewKom.trad.getEnchantmentName(ench));
        return is;
    }

    @EventHandler
    public void click(InventoryClickEvent ev) {
        if (ev.getClickedInventory() == null) {
            return;
        }
        if (ev.getClickedInventory().getTitle() != null && ev.getClickedInventory().getTitle().startsWith("§lEnchantments")) {

            Block mesa = null;
            for (Block b : invs.keySet()) {
                Inventory i = invs.get(b);
                if (i.equals(ev.getClickedInventory()) || i.equals(ev.getInventory())) {
                    mesa = b;
                    break;
                }
            }
            if (mesa == null) {
                return;
            }
            ev.setCancelled(true);
            if (ev.getCurrentItem() != null) {
                EnchantmentTable et = NewKom.database.ench.getEnchantment(mesa);
                ItemStack is = ev.getCurrentItem();
                Enchantment ench = null;
                if (is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
                    String name = ChatColor.stripColor(is.getItemMeta().getDisplayName()).trim();
                    name = name.split(":")[1].trim();
                    for (Enchantment enchloop : Enchantment.values()) {
                        {
                            String tname = NewKom.trad.getEnchantmentName(enchloop);
                            if (tname != null && tname.equals(name)) {
                                ench = enchloop;
                                break;
                            }
                        }
                    }
                }

                if (ench == null) {
                    return;
                }
                if (EnchantingListener.defaults.contains(ench)) {
                    ev.getWhoClicked().sendMessage(NewKom.errorTag + L.m("You can't disable default enchantments!"));
                    return;
                }
                if (is.getDurability() == 0) {
                    ev.getWhoClicked().sendMessage(NewKom.errorTag + L.m("You don't have this enchantment!"));
                    return;
                }
                boolean b = false;
                if (is.getDurability() == 5) {
                    is.setDurability((short) 14);
                } else {
                    is.setDurability((short) 5);
                    b = true;
                }
                et.enchants.put(ench, b);
                NewKom.database.ench.update(mesa, et);

            }

        }
    }

    @Override
    public String getName() {
        return "Menu Enchantment";
    }

}
