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
import devsBrsMarotos.cmds.cmdlist.KomSubs.CraftSubs.CmdCraftAdd;
import devsBrsMarotos.cmds.cmdlist.KomSubs.CraftSubs.CmdCraftCheck;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author vntgasl
 */
public class CmdCrafts extends SubCmd {

    public CmdCrafts() {
        super("crafts", CommandType.OP);
        subs.add(new CmdCraftCheck());
        subs.add(new CmdCraftAdd());
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (args.length <= 1) {
            this.showSubCommands(cs, "crafts");
        } else {
            boolean executed = false;
            for (SubCmd cmd : subs) {
                if (cmd.cmd.equalsIgnoreCase(args[1])) {
                    cmd.execute(cs, args);
                    executed = true;
                }
            }
            if(!executed)
                this.showSubCommands(cs, "crafts"); 
        }
    }

}
