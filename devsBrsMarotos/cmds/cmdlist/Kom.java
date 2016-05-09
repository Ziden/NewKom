/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.cmds.cmdlist;

import devsBrsMarotos.cmds.cmdlist.KomSubs.CmdAddCompass;
import devsBrsMarotos.cmds.Comando;
import devsBrsMarotos.cmds.SubCmd;
import devsBrsMarotos.cmds.cmdlist.KomSubs.CmdCrafts;
import devsBrsMarotos.cmds.cmdlist.KomSubs.CmdCreateBook;
import devsBrsMarotos.cmds.cmdlist.KomSubs.CmdDamageTest;
import devsBrsMarotos.cmds.cmdlist.KomSubs.CmdHarvest;
import devsBrsMarotos.cmds.cmdlist.KomSubs.CmdEnchants;
import devsBrsMarotos.cmds.cmdlist.KomSubs.CmdMob;
import devsBrsMarotos.cmds.cmdlist.KomSubs.CmdMobConfig;
import devsBrsMarotos.cmds.cmdlist.KomSubs.CmdPotions;
import devsBrsMarotos.cmds.cmdlist.KomSubs.CmdCraftRecipes;
import devsBrsMarotos.cmds.cmdlist.KomSubs.CmdEquips;
import devsBrsMarotos.cmds.cmdlist.KomSubs.CmdItems;
import devsBrsMarotos.cmds.cmdlist.KomSubs.CmdRecipes;
import devsBrsMarotos.cmds.cmdlist.KomSubs.CmdSetLife;
import devsBrsMarotos.cmds.cmdlist.KomSubs.CmdSetSkill;
import devsBrsMarotos.cmds.cmdlist.KomSubs.CmdSkills;
import devsBrsMarotos.cmds.cmdlist.KomSubs.CmdStudy;
import devsBrsMarotos.cmds.cmdlist.KomSubs.CmdSkill;
import org.bukkit.command.CommandSender;

/**
 *
 * @author vntgasl
 */
public class Kom extends Comando {

    public Kom() {
        super("kom", CommandType.TODOS);
        // NO OP     
        subs.add(new CmdSkills());
        subs.add(new CmdStudy());

        // YES OP
        subs.add(new CmdSetSkill());
        subs.add(new CmdHarvest());
        subs.add(new CmdCrafts());
        subs.add(new CmdEnchants());
        subs.add(new CmdMobConfig());
        subs.add(new CmdCreateBook());
        subs.add(new CmdCraftRecipes());
        subs.add(new CmdPotions());
        subs.add(new CmdMob());
        subs.add(new CmdSkill());
        subs.add(new CmdDamageTest());
        subs.add(new CmdCraftRecipes());
        subs.add(new CmdEnchants());
        subs.add(new CmdEquips());
        subs.add(new CmdItems());
        subs.add(new CmdSetLife());
        subs.add(new CmdAddCompass());
        subs.add(new CmdRecipes());
    }

    @Override
    public void usouComando(CommandSender cs, String[] args) {
        if (args.length > 0) {
            for (SubCmd cmd : subs) {
                if (cmd.cmd.equalsIgnoreCase(args[0])) {
                    cmd.execute(cs, args);
                }
            }
        } else {
            showSubCommands(cs);
        }

    }
}
