/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.Lang.L;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

/**
 *
 * @author User
 */
public class PlayerMana extends Mecanica {

    @EventHandler
    public void onEnable() {
        PlayerMana.startRegenTimer();
    }

    public static void changeMana(Player p, double qtd) {
        PlayerMana mana = getMana(p);
        if (mana.mana == mana.maxMana && qtd > 0) {
            return;
        }
        mana.mana += qtd;
        if (mana.mana > mana.maxMana) {
            mana.mana = (int) mana.maxMana;
        }
        if (mana.mana < 0) {
            mana.mana = 0;
        }
        p.sendMessage(ChatColor.BLUE + L.m("Mana: ") + mana.mana + "/" + mana.maxMana);
    }

    public int maxMana = 100;

    public int mana = 100;
    public static HashMap<UUID, PlayerMana> manas = new HashMap<UUID, PlayerMana>();

    public static PlayerMana getMana(Player p) {
        if (manas.containsKey(p.getUniqueId())) {
            return manas.get(p.getUniqueId());
        } else {
            PlayerMana m = new PlayerMana();
            double meditation = NewKom.database.skills.getSkills(p).get(SkillEnum.Meditation).getLvl();
            m.maxMana = 100 + (int) meditation * 2;
            manas.put(p.getUniqueId(), m);
            return m;
        }
    }

    public static void startRegenTimer() {

        Runnable r = new Runnable() {
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    double meditation = NewKom.database.skills.getSkills(p).get(SkillEnum.Meditation).getLvl();
                    changeMana(p, 5 + (meditation / 10));
                }
            }
        };
        Bukkit.getScheduler().scheduleSyncRepeatingTask(NewKom.instancia, r, 20 * 20, 20 * 20);

    }

    public static boolean spendMana(Player p, int qt) {
        PlayerMana mana = getMana(p);
        if (mana.mana >= qt) {
            changeMana(p, -qt);
            return true;
        } else {
            p.sendMessage(ChatColor.BLUE + L.m("You need more mana: ") + mana.mana + "/" + mana.maxMana + " - " + ChatColor.RED + qt);
            return false;
        }

    }

    @Override
    public String getName() {
        return "Mana";
    }
}
