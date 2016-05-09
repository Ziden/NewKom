/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.cmds.cmdlist.KomSubs;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.cmds.Comando.CommandType;
import devsBrsMarotos.cmds.SubCmd;
import devsBrsMarotos.mechanic.list.Harvesting.BlockInfoDisplay;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import devsBrsMarotos.mechanic.list.Skills.SkillFormulas;
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
public class CmdStudy extends SubCmd {

    public CmdStudy() {
        super("study", CommandType.PLAYER);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {

          double chanceToSuccess = SkillFormulas.getChancesToSuccess(SkillEnum.Hiding, HidingActiveSkill.instance.getMinimumSkill((Player) cs), NewKom.database.skills.getSkills((Player)cs).get(SkillEnum.Hiding).getLvl());
                cs.sendMessage("§a Chance to hide: " + chanceToSuccess+"%");
          
        
        Player p = (Player) cs;
        Block b = p.getTargetBlock((HashSet<Byte>) null, 6);
        if (b == null || b.getType() == Material.AIR) {
            p.sendMessage(ChatColor.RED + L.m("You must be taking a close look to a block to study it..."));
        } else {
            BlockInfoDisplay.displayAllInfos(p, b);
        }
    }
}
