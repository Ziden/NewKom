/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.cmds.cmdlist.KomSubs.CraftSubs;

import devsBrsMarotos.cmds.Comando;
import devsBrsMarotos.cmds.Comando.CommandType;
import devsBrsMarotos.cmds.SubCmd;
import devsBrsMarotos.mechanic.list.Crafting.CraftConfig;
import devsBrsMarotos.mechanic.list.Crafting.Craftable;
import devsBrsMarotos.mechanic.list.Harvesting.HarvestCache;
import devsBrsMarotos.mechanic.list.Harvesting.HarvestConfig;
import devsBrsMarotos.mechanic.list.Harvesting.Harvestable;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import java.util.HashSet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author vntgasl
 */
public class CmdCraftAdd extends SubCmd {

    public CmdCraftAdd() {
        super("add", CommandType.OP);
    }

    public void display(Player p, HashSet<Craftable> craftable) {
        Inventory i = Bukkit.createInventory(p, InventoryType.CHEST, L.m("Craftables"));
        for (Craftable h : craftable) {
            i.addItem(h.getIcon());
        }
        p.openInventory(i);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (args.length < 4) {
            cs.sendMessage("Use /kom crafts add <skillName> <minSkill>");
        } else {
            SkillEnum skill = SkillEnum.valueOf(args[2]);
            if (skill != null) {
                Player p = (Player) cs;
                ItemStack item = p.getItemInHand();
                if (item == null) {
                    p.sendMessage(ChatColor.RED + L.m("Please , put an item on your hand ..."));
                    return;
                }
                int minSkill = Integer.valueOf(args[3]);
                Material mat = item.getType();
                byte data = item.getData().getData();
                Craftable craft = new Craftable(mat, data, minSkill,skill, 1);
                CraftConfig.add(craft);
                p.sendMessage(ChatColor.GREEN+L.m("Crafting item added !"));
            } else {
                cs.sendMessage(ChatColor.RED + L.m("This is not a valid skill !"));
            }
        }
    }

}
