/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.cmds.cmdlist.KomSubs;

import devsBrsMarotos.DB.Models.PlayerData;
import devsBrsMarotos.DB.Models.Stage;
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
public class CmdSetLife extends SubCmd {

    public CmdSetLife() {
        super("setlife", CommandType.OP);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {

        if(args.length != 2) {
            cs.sendMessage("Use /kom setlife <life>");
        }
        Player p = (Player)cs;
        int life = Integer.parseInt(args[1]);
          PlayerData data = NewKom.database.playerData.getPlayerData(p);
        data.life = life;
        NewKom.database.playerData.updatePlayerData(p, data);
        p.setMaxHealth(data.life);
        p.setHealth(p.getMaxHealth());
        p.sendMessage("Life Changed");
    }
}
