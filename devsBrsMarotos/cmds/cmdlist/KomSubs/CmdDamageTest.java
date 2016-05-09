/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.cmds.cmdlist.KomSubs;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.cmds.Comando.CommandType;
import devsBrsMarotos.cmds.SubCmd;
import devsBrsMarotos.mechanic.list.Damage.Dmg;
import devsBrsMarotos.mechanic.list.Harvesting.BlockInfoDisplay;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import devsBrsMarotos.mechanic.list.Skills.SkillFormulas;
import devsBrsMarotos.mechanic.list.Skills.SkillList.Hiding.HidingActiveSkill;
import java.util.HashSet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 *
 * @author User
 */
public class CmdDamageTest extends SubCmd {

    public CmdDamageTest() {
        super("damagetest", CommandType.OP);
    }

    // kom damagetest 5 MAGIC
    @Override
    public void execute(CommandSender cs, String[] args) {
        if(args.length != 5) {
            //                        0             1              2       3        4
            cs.sendMessage("Use /kom damagetest <damageDealer> <damaged> <damage> <cause>");
            return;
        }
        String sCause = args[4];
        String sDamage = args[3];
        String damaged = args[2];
        String damager = args[1];
        
        Player pDamager = Bukkit.getPlayer(damager);
        if(pDamager==null) {
            cs.sendMessage("No Player: "+damager);
            return;
        }
        Player pDamaged = Bukkit.getPlayer(damaged);
        if(pDamaged==null) {
            cs.sendMessage("No Player: "+damager);
            return;
        }
        
        try {
            DamageCause cause = DamageCause.valueOf(sCause);
            double damage = Double.valueOf(sDamage);
            
            Dmg.dealDamage(pDamager, pDamaged, damage, cause);
            
            cs.sendMessage("The damage was dealt !");
            
        } catch(Exception e) {
            String help = "";
            for(DamageCause c : DamageCause.values())
                help += c.name()+" ";
            cs.sendMessage("Valid Damage Causes:");
            cs.sendMessage(help);
        }
 
    }
}
