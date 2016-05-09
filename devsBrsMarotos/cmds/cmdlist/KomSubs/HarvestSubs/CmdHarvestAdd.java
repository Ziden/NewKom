/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.cmds.cmdlist.KomSubs.HarvestSubs;

import devsBrsMarotos.cmds.Comando;
import devsBrsMarotos.cmds.Comando.CommandType;
import devsBrsMarotos.cmds.SubCmd;
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
public class CmdHarvestAdd extends SubCmd {

    public CmdHarvestAdd() {
        super("add", CommandType.OP);
    }

    public void display(Player p, HashSet<Harvestable> harvestables) {
        Inventory i = Bukkit.createInventory(p, InventoryType.CHEST, L.m("Harvestables"));
        for (Harvestable h : harvestables) {
            i.addItem(h.getIcon());
        }
        p.openInventory(i);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (args.length < 5) {
            cs.sendMessage("Use /kom harvest add <skillName> <minSkill> <cooldown>");
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

                int cooldown = Integer.valueOf(args[4]);
                Material mat = item.getType();
                byte data = item.getData().getData();
                Harvestable harvestable = new Harvestable(mat, data, skill,minSkill,cooldown, 1);
                HarvestConfig.add(harvestable);
                p.sendMessage(ChatColor.GREEN+L.m("Harvestable added !"));
            } else {
                cs.sendMessage(ChatColor.RED + L.m("This is not a valid skill !"));
            }
        }
    }

}
