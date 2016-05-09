/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.cmds.cmdlist.KomSubs;

import devsBrsMarotos.DB.MobConfigDB;
import static devsBrsMarotos.DB.MobConfigDB.hasConfig;
import devsBrsMarotos.DB.Models.ModelMobConfig;
import devsBrsMarotos.NewKom;
import static devsBrsMarotos.NewKom.alertTag;
import static devsBrsMarotos.NewKom.errorTag;
import static devsBrsMarotos.NewKom.successTag;
import devsBrsMarotos.cmds.Comando;
import devsBrsMarotos.cmds.Comando.CommandType;
import devsBrsMarotos.cmds.SubCmd;
import devsBrsMarotos.mechanic.list.MobConfigs.MobInfo;
import devsBrsMarotos.mechanic.list.MobConfigs.MobConfig;
import devsBrsMarotos.util.MobUtils;
import devsBrsMarotos.util.RegionUtils;
import devsBrsMarotos.util.GeneralUtils;
import static devsBrsMarotos.util.GeneralUtils.isInt;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

/**
 *
 * @author ciro
 */
public class CmdMobConfig extends SubCmd {

    public CmdMobConfig() {
        super("mobconfig", CommandType.OP);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            Player p = (Player) cs;
            if (!p.hasPermission("cmdkom.mobconfig")) {
                p.sendMessage("§cYou do not have permission!");
                return;
            }

            if (args.length == 2) {
                if (args[1].equalsIgnoreCase("mobname")) {
                    MobUtils.showMobNames(p);
                    return;
                } else {
                    help(p);
                    return;
                }
            }
            if (args.length == 3) {
                if (args[1].equalsIgnoreCase("check")) {
                    if (!MobConfigDB.hasConfig(args[2])) {
                        p.sendMessage(errorTag + "§cNo §6config §cof that name");
                        return;
                    }
                    p.openInventory(MobInfo.showMobs(p, 27, "Region: " + args[2], MobConfigDB.cache.getCached(args[2]).mobs));
                    return;
                } else if (args[1].equalsIgnoreCase("rem")) {
                    if (p.hasMetadata("VaiRemoverTudoMob")) {
                        MobConfigDB.remConfigAll(p.getWorld().getName(), args[2]);
                        p.removeMetadata("VaiRemoverTudoMob", NewKom.instancia);
                        p.sendMessage(successTag + "Successful!");
                    } else {
                        p.setMetadata("VaiRemoverTudoMob", new FixedMetadataValue(NewKom.instancia, "VaiRemoverTudoMob"));
                        p.sendMessage(alertTag + "Please, confirm the command!");
                    }
                } else {
                    help(p);
                    return;
                }
            } else if (args.length >= 6) {
                if (args[1].equalsIgnoreCase("add")) {
                    String nameRegion = args[2];
                    if (!RegionUtils.isRegion(p, nameRegion)) {
                        p.sendMessage(errorTag + "§cNo §6region §cof that name");
                        p.sendMessage(errorTag + "§cPlease, create a region with §6WorldGuard");
                        return;
                    }
                    String mob = args[3];
                    CreatureType ct;
                    try {
                        ct = CreatureType.valueOf(mob.toUpperCase());
                    } catch (Exception e) {
                        p.sendMessage(errorTag + "§cNo §6mob§c of that name.");
                        p.sendMessage(errorTag + "§cCheck names with §f/kom mobconfig mobName");
                        return;
                    }
                    String lv = args[4];
                    if (!isInt(lv)) {
                        p.sendMessage(errorTag + "§cEnter a integer number in §6lv");
                        return;
                    }
                    String percent = args[5];
                    if (!isInt(percent)) {
                        p.sendMessage(errorTag + "§cEnter a integer number in §6percent");
                        return;
                    }
                    String name = "";
                    if (args.length > 6) {
                        name = args[6];
                    }
                    if (MobConfigDB.setMob(p, args[2], p.getWorld().getName(), mob, lv, percent, name)) {
                        p.sendMessage(successTag + "§3Successful!");
                    } else {
                        p.sendMessage(errorTag + "§cError!");
                    }

                } else {
                    help(p);
                    return;
                }
            } else {
                help(p);
                return;
            }
        }
    }

    public static void help(Player p) {
        p.sendMessage("§cUse:");
        p.sendMessage("§e/kom mobconfig add <nameRegion> <mob> <lv> <%> [name]");
        p.sendMessage("§2-§fAdds mob in an existing region");
        p.sendMessage("§e/kom mobconfig check <nameRegion>");
        p.sendMessage("§2-§fCheck a existing region");
        p.sendMessage("§e/kom mobconfig rem <nameRegion> ");
        p.sendMessage("§2-§fRemove mobs a region");
        p.sendMessage("§e/kom mobconfig mobname");
        p.sendMessage("§2-§fCheck available names mobs.");
    }

}
