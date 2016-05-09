/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.cmds.cmdlist.KomSubs;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.cmds.Comando.CommandType;
import devsBrsMarotos.cmds.SubCmd;
import devsBrsMarotos.mechanic.list.Damage.Equipment.EquipManager;
import devsBrsMarotos.mechanic.list.Damage.Equipment.EquipMeta;
import devsBrsMarotos.mechanic.list.Damage.Equipment.ItemAttributes.AttributeEnum;
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
public class CmdStats extends SubCmd {

    public CmdStats() {
        super("equipmeta", CommandType.OP);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {

        EquipMeta meta = EquipManager.getPlayerEquipmentMeta((Player)cs);
        for(AttributeEnum a : meta.attributes.keySet()) {
            cs.sendMessage(ChatColor.GREEN+""+a.name()+" "+meta.attributes.get(a));
        }
    }
}
