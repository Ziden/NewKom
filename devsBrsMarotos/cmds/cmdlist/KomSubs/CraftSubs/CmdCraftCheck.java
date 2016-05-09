/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.cmds.cmdlist.KomSubs.CraftSubs;

import devsBrsMarotos.cmds.Comando;
import devsBrsMarotos.cmds.Comando.CommandType;
import devsBrsMarotos.cmds.SubCmd;
import devsBrsMarotos.mechanic.list.Crafting.CraftCache;
import devsBrsMarotos.mechanic.list.Crafting.Craftable;
import devsBrsMarotos.mechanic.list.Harvesting.HarvestCache;
import devsBrsMarotos.mechanic.list.Harvesting.Harvestable;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import java.util.Arrays;
import java.util.HashSet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author vntgasl
 * 
 */

public class CmdCraftCheck extends SubCmd {

    public CmdCraftCheck() {
        super("check", CommandType.OP);
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
        if (args.length < 3) {
            cs.sendMessage(L.m("Use /kom crafts check <skillName>"));
            cs.sendMessage(L.m("Or /kom crafts check <materialName>"));
        } else {
            SkillEnum skill = null;
            try {
                skill = SkillEnum.valueOf(args[2]);
            } catch (Exception e) {

            }
            if (skill != null) {
                HashSet<Craftable> craftables = CraftCache.getCraftables(skill);
                if (craftables == null) {
                    cs.sendMessage(ChatColor.RED + L.m("This skill has no craftables !"));
                } else {
                    display((Player) cs, craftables);
                }
            } else {
                Material m = null;
                try {
                    m = Material.valueOf(args[2]);
                } catch (Exception e) {
                    
                }
                if (m == null) {
                    m = Material.getMaterial(args[2]);
                }
                Craftable craftables = CraftCache.getCraftable(m);
                if (craftables == null) {
                    cs.sendMessage(ChatColor.RED + L.m("Couldnt find craftables for ") + args[2]);
                } else {
                    display((Player) cs, new HashSet<Craftable>(Arrays.asList(new Craftable[]{craftables})));
                }
            }
        }
    }

}
