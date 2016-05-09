/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills;

import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.util.Cooldown;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author Carlos
 */
public class ActiveSkillManager {

    public static void active(Player p, ActiveSkill sk) {
        if (Cooldown.isCooldown(p, "cdcd:" + sk.getSkillName())) {
            p.sendMessage("§a" + L.m("Wait to use the skill again."));
            return;
        }
        if (sk.getMana() != 0) {
            if (PlayerMana.getMana(p).mana < sk.getMana()) {
                PlayerMana mana = PlayerMana.getMana(p);
                p.sendMessage(ChatColor.BLUE + L.m("You need more mana: ") + mana.mana + "/" + mana.maxMana + " - " + ChatColor.RED + sk.getMana());

                return;
            }
        }
        SkillFormulas.SkillResult sr = SkillFormulas.hasSucess(sk.getSkill(), p, sk.getMinimumSkill(p));

        boolean b = sk.activeSkill(p, sr);
        if (b) {
            if (sk.getCooldown() != 0) {
                Cooldown.setMetaCooldown(p, "cdcd:" + sk.getSkillName(), sk.getCooldown());

            }
            if (sk.getMana() != 0) {
                PlayerMana.spendMana(p, sk.getMana());
            }
        }
    }
}
