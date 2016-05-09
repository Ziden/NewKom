/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.cmds.cmdlist.KomSubs;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.cmds.Comando;
import devsBrsMarotos.cmds.Comando.CommandType;
import devsBrsMarotos.cmds.SubCmd;
import devsBrsMarotos.cmds.cmdlist.KomSubs.HarvestSubs.CmdHarvestAdd;
import devsBrsMarotos.cmds.cmdlist.KomSubs.HarvestSubs.CmdHarvestCheck;
import devsBrsMarotos.cmds.cmdlist.KomSubs.HarvestSubs.ShowOres;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author vntgasl
 */
public class CmdHarvest extends SubCmd {

    public CmdHarvest() {
        super("harvest", CommandType.OP);
        subs.add(new CmdHarvestCheck());
        subs.add(new CmdHarvestAdd());
        subs.add(new ShowOres());
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (args.length <= 1) {
            this.showSubCommands(cs, "harvest");
        } else {
            boolean executed = false;
            for (SubCmd cmd : subs) {
                if (cmd.cmd.equalsIgnoreCase(args[1])) {
                    cmd.execute(cs, args);
                    executed = true;
                }
            }
            if(!executed)
                this.showSubCommands(cs, "harvest"); 
        }
    }

}
