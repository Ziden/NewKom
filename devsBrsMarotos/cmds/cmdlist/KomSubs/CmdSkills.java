/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.cmds.cmdlist.KomSubs;

import devsBrsMarotos.DB.Models.PlayerData;
import devsBrsMarotos.DB.Models.Skills;
import devsBrsMarotos.NewKom;
import devsBrsMarotos.cmds.Comando;
import devsBrsMarotos.cmds.Comando.CommandType;
import devsBrsMarotos.cmds.SubCmd;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author User
 */
public class CmdSkills extends SubCmd {

    public CmdSkills() {
        super("skills", CommandType.PLAYER);
    }

    
    
    @Override
    public void execute(CommandSender cs, String[] args) {
       Player p = (Player)cs;
       PlayerData data = NewKom.database.playerData.getPlayerData(p);
       Inventory inv = Bukkit.createInventory(p, 6*9, "Skills:");
       Skills s = NewKom.database.skills.getSkills(((Player)cs));
       for(SkillEnum skill : SkillEnum.values()) {
           inv.addItem(skill.getItem(s.skills.get(skill)));
       }
       p.openInventory(inv);
    }
    
}
