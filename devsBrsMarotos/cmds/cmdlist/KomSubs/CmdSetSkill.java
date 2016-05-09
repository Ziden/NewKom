/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.cmds.cmdlist.KomSubs;

import devsBrsMarotos.DB.Models.Skills;
import devsBrsMarotos.NewKom;
import devsBrsMarotos.cmds.Comando;
import devsBrsMarotos.cmds.Comando.CommandType;
import devsBrsMarotos.cmds.SubCmd;
import devsBrsMarotos.mechanic.list.Harvesting.BlockInfoDisplay;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
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
public class CmdSetSkill extends SubCmd {
    
    public CmdSetSkill() {
        super("setskill", CommandType.OP);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if(args.length!=3) {
            cs.sendMessage("Use /kom setskill <skill> <amount>");
        }
        Player p = (Player)cs;
        try {
            SkillEnum skill = SkillEnum.valueOf(args[1]);
            Skills s = NewKom.database.skills.getSkills(p);
            s.get(skill).setLvl(Integer.valueOf(args[2]));
            NewKom.database.skills.updateSkills(p, s);
            p.sendMessage("Updated...");
        } catch(Exception e) {
            p.sendMessage("Not a valid skill...");
            return;
        }
    }
}
