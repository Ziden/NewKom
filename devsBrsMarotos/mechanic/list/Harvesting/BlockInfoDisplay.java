/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Harvesting;

import devsBrsMarotos.DB.Models.Skills;
import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import devsBrsMarotos.mechanic.list.Skills.SkillFormulas;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 *
 * @author User
 */
public class BlockInfoDisplay {
    
    public static void displayAllInfos(Player p, Block b) {
        Harvestable h = HarvestCache.getHarvestable(b);
        if(h == null) {
            p.sendMessage(ChatColor.RED+L.m("This block is not a harvestable..."));
        } else {
            Skills s = NewKom.database.skills.getSkills(p);
            p.sendMessage(ChatColor.GREEN+"--------------Harvestable Information for "+b.getType().name()+"-----------");
            p.sendMessage(ChatColor.GREEN+"Skill Needed: "+ChatColor.YELLOW+h.skillsNeeded.name());
            p.sendMessage(ChatColor.GREEN+"Min Skill: "+ChatColor.YELLOW+h.minSkill);
            p.sendMessage(ChatColor.GREEN+"Xp: "+ChatColor.YELLOW+SkillFormulas.getXpEarned(p, h.skillsNeeded, h.minSkill, h.expRatio, s));
            double mySkill = s.get(h.skillsNeeded).getLvl();
            double chanceToSuccess = SkillFormulas.getChancesToSuccess(h.skillsNeeded, h.minSkill, mySkill);
            p.sendMessage(ChatColor.GREEN+"Chance to Harvest: "+ChatColor.YELLOW+chanceToSuccess+"%");
        }
    }
    
}
