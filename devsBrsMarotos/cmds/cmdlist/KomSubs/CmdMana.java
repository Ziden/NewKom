/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.cmds.cmdlist.KomSubs;

import devsBrsMarotos.cmds.Comando;
import devsBrsMarotos.cmds.Comando.CommandType;
import devsBrsMarotos.cmds.SubCmd;
import devsBrsMarotos.mechanic.list.Harvesting.BlockInfoDisplay;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.Skills.ActiveSkillManager;
import devsBrsMarotos.mechanic.list.Skills.PlayerMana;
import devsBrsMarotos.mechanic.list.Skills.SkillList.Hiding.HidingActiveSkill;
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
public class CmdMana extends SubCmd {

    public CmdMana() {
        super("mana", CommandType.OP);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {

        PlayerMana.getMana((Player) cs).mana = PlayerMana.getMana((Player) cs).maxMana;
        cs.sendMessage("§9" + L.m("Mana filled."));

    }
}
