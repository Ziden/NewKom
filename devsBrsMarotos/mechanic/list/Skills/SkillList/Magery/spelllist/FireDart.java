/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills.SkillList.Magery.spelllist;

import devsBrsMarotos.mechanic.list.Skills.SkillList.Magery.Elements;
import devsBrsMarotos.mechanic.list.Skills.SkillList.Magery.MageSpell;
import org.bukkit.entity.Player;

/**
 *
 * @author User
 */
public class FireDart extends MageSpell {

    public FireDart() {
        super("Fire Dart");
    }

    @Override
    public void cast(Player caster) {
        // TO-DO
        caster.sendMessage("SORTEI FIRE DART");
    }

    @Override
    public double getManaCost() {
        return 5;
    }

    @Override
    public double getExpRatio() {
        return 1;
    }

    @Override
    public double getMinSkill() {
        return 5;
    }

    @Override
    public Elements[] getElements() {
        return new Elements[]{Elements.Fire, Elements.Fire, Elements.Thunder};
    }

    @Override
    public int getCooldownInSeconds() {
        return 1;
    }
    
}
