/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.cmds.cmdlist.KomSubs;

import devsBrsMarotos.cmds.Comando.CommandType;
import devsBrsMarotos.cmds.SubCmd;
import devsBrsMarotos.mechanic.list.CustomItems.CustomItem;
import devsBrsMarotos.mechanic.list.CustomItems.ItemLoader;
import devsBrsMarotos.mechanic.list.Skills.SkillList.Alchemy.CustomPotions.CustomPotion;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 *
 * @author User
 * 
 */

public class CmdPotions extends SubCmd {

    public CmdPotions() {
        super("potions", CommandType.OP);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        Player p = (Player) cs;
        CustomPotion.mostra(p);
    }

}
