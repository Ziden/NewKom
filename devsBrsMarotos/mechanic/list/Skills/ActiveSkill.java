/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills;

import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.Skills.SkillFormulas.SkillResult;
import devsBrsMarotos.util.Cooldown;
import org.bukkit.entity.Player;

/**
 *
 * @author Carlos
 */
public interface  ActiveSkill {

    /*
     Caso retornar true remove a mana e adiciona o cooldown da skill    
     */
    public abstract boolean activeSkill(Player p, SkillResult sr);

    public abstract int getMana();

    public abstract SkillEnum getSkill();

    /*
     Cooldown em ms
     */
    public abstract int getCooldown();

    public abstract String getSkillName();
    /*
     Passado argumento Player caso queira calcular alguma coisa a mais,
     por exemplo a lumiosidade no lugar que ele está
     */

    public abstract int getMinimumSkill(Player p);

   

}
