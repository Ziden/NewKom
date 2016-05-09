/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Harvesting;

import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import org.bukkit.entity.Player;

/**
 *
 * @author User
 */
public class EpicFailEffect {
    
    public static String getEffect(Player p, SkillEnum skill) {
        switch(skill.name()) {
            case "Lumberjacking": 
                p.damage(1);
                return L.m("You failed to chop the wood hurting your finger...");
        }
        return "You failed epically...";
    }
    
}
