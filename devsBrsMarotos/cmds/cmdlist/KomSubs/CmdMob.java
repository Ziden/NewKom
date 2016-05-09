/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.cmds.cmdlist.KomSubs;

import devsBrsMarotos.DB.MobConfigDB;
import devsBrsMarotos.MetaShit;
import devsBrsMarotos.NewKom;
import devsBrsMarotos.cmds.Comando.CommandType;
import devsBrsMarotos.cmds.SubCmd;
import devsBrsMarotos.mechanic.list.MobConfigs.MobConfig;
import devsBrsMarotos.mechanic.list.MobConfigs.SpawnController;
import devsBrsMarotos.util.MobUtils;
import devsBrsMarotos.util.RegionUtils;
import devsBrsMarotos.util.GeneralUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

/**
 *
 * @author ciro
 *
 */
public class CmdMob extends SubCmd {

    public CmdMob() {
        super("mob", CommandType.OP);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            Player p = (Player) cs;
            if (!p.isOp()) {
                return;
            }

        }
    }

}
