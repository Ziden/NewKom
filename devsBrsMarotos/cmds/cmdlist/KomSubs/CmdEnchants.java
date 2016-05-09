/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.cmds.cmdlist.KomSubs;

import devsBrsMarotos.cmds.Comando.CommandType;
import devsBrsMarotos.cmds.SubCmd;
import devsBrsMarotos.mechanic.list.CustomItems.list.EnchantmentScroll;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author User
 *
 */
public class CmdEnchants extends SubCmd {

    public CmdEnchants() {
        super("enchants", CommandType.OP);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        Player p = (Player) cs;
        Inventory i = Bukkit.createInventory(null, 54, "§l§eEnchants");
        
        for (Enchantment ench : Enchantment.values()) {
            ItemStack is = EnchantmentScroll.create(ench);
            i.addItem(is);
        }
        p.openInventory(i);
    }

}
