/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.cmds.cmdlist.KomSubs;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.cmds.cmdlist.KomSubs.*;
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
public class CmdAddCompass extends SubCmd {

    public CmdAddCompass() {
        super("addcompass", CommandType.OP);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        Player p = (Player) cs;
        if (args.length != 2) {
            cs.sendMessage("§cUse: /kom addcompass NOME");
            return;
        }
        NewKom.database.compass.addLocation(p, p.getLocation(), args[1]);

    }

}
