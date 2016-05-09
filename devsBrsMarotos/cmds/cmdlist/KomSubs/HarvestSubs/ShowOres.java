/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.cmds.cmdlist.KomSubs.HarvestSubs;

import devsBrsMarotos.cmds.Comando;
import devsBrsMarotos.cmds.Comando.CommandType;
import devsBrsMarotos.cmds.SubCmd;
import devsBrsMarotos.mechanic.list.Harvesting.BlockInfoDisplay;
import devsBrsMarotos.mechanic.list.Skills.SkillList.Mining;
import devsBrsMarotos.mechanic.list.Lang.L;
import java.util.HashSet;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author User
 */
public class ShowOres extends SubCmd {
    
    public ShowOres() {
        super("showores", CommandType.OP);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        Player p = (Player)cs;
        Mining.showOres(p, 10);
        p.sendMessage("Seeing ores...( if there are ores on stones nearby on your floor )");
    }
}
