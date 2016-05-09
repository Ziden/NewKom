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
import devsBrsMarotos.mechanic.list.Harvesting.Harvestable;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
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
 */
public class CmdHarvestCheck extends SubCmd {

    public CmdHarvestCheck() {
        super("check", CommandType.OP);
    }

    public void display(Player p, HashSet<Harvestable> harvestables) {
        Inventory i = Bukkit.createInventory(p, 6 * 9, "Harvestables");
        for (Harvestable h : harvestables) {
            i.addItem(h.getIcon());
        }
        p.openInventory(i);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (args.length < 3) {
            cs.sendMessage(L.m("Use /kom harvest check <skillName>"));
            cs.sendMessage(L.m("Or /kom harvest check <materialName>"));
        } else {
            SkillEnum skill = null;
            try {
                skill = SkillEnum.valueOf(args[2]);
            } catch (Exception e) {

            }
            if (skill != null) {
                HashSet<Harvestable> harvestables = HarvestCache.getHarvestable(skill);
                if (harvestables == null) {
                    cs.sendMessage(ChatColor.RED + L.m("This skill has no harvestables !"));
                } else {
                    display((Player) cs, harvestables);
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
                HashSet<Harvestable> harvestables = HarvestCache.getHarvestable(m);
                if (harvestables == null || harvestables.size()==0) {
                    cs.sendMessage(ChatColor.RED + L.m("Couldnt find harvestables for ") + args[2]);
                } else {
                    display((Player) cs, harvestables);
                }
            }
        }
    }

}
