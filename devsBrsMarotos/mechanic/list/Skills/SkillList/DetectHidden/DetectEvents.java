/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills.SkillList.DetectHidden;

import devsBrsMarotos.DB.Models.Skills.SkillInfo;
import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import org.bukkit.entity.Player;

/**
 *
 * @author root
 * 
 */

public class DetectEvents {
    
    public static void useSkill(Player p) {
        
        SkillInfo skill = NewKom.database.skills.getSkills(p).get(SkillEnum.DetectHidden);
        
    }
    
}
