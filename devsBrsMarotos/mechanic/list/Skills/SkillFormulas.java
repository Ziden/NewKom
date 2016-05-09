/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills;

import devsBrsMarotos.DB.Models.Skills;
import devsBrsMarotos.DB.Models.Skills.SkillInfo;
import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.util.GeneralUtils;
import java.util.Random;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author vntgasl
 */
public class SkillFormulas {
    
    public static Random rnd = new Random();
    
    public enum SkillResult {
        
        EPIC_SUCCESS,
        SUCCESS,
        ALMOST_FAILED,
        FAIL,
        EPIC_FAIL,
        
    }
    
    private static double skillDifference = 30;
    private static double skillEasyLevelUntil = 20;

    /////////////// REMOVE /////////////
    /*
     public static double getChanceRaiseSkill(SkillEnum skill, double minSkill, double mySkill, double specificExpRatio) {
     if (mySkill < skillEasyLevelUntil) {
     return 80;
     }
     double chanceToSuccess = getChancesToSuccess(skill, minSkill, mySkill);
     double chanceToRaise = (100 - chanceToSuccess) / (mySkill / 10);
     if (mySkill > 80) {
     chanceToRaise /= 2;
     }
     chanceToRaise *= skill.expRatio;
     chanceToRaise *= specificExpRatio;
     return chanceToRaise;
     }
     */
    public static double getXpEarned(Player p, SkillEnum skill, double minLvl, double expRatio, Skills skills) {
        SkillInfo info = skills.get(skill);
        //How many actions with SKILL = MINSKILL would need to lvl up 0.1 ?
        int actionsNeeded = 1;

        // the more skill he have, the more difficult to level is
        actionsNeeded += info.getLvl() / 10; // at level 90, he will need +9 actions, so he would need to success 10 times where mySkill = minSkill (0% chance to success)

        // the more points he have in total, the difficult is to level the skills
        double totalLevels = skills.total();

        // each 50 levels he will need 1 more action to level up
        actionsNeeded += totalLevels / 50;

        // TROLL
        if (info.getLvl() > 80) {
            actionsNeeded += 10;
        }
        
        return ((minLvl + 2) * (minLvl + 2) * expRatio) / actionsNeeded;
    }
    
    public static void earnXp(Player p, SkillEnum skill, double minLvl, double expRatio) {
        
        Skills skills = NewKom.database.skills.getSkills(p);
        SkillInfo info = skills.get(skill);
        double earnedXp = getXpEarned(p, skill, minLvl, expRatio, skills);

        // giving him the xp
        double xp = info.getXp() + earnedXp;
        double xpToNextLevel = getXpToEachLevel((int) Math.floor(info.getLvl()) + 1);

        // he can level up
        info.setXpTotal(info.getXp() + earnedXp);
        
        if (xp > xpToNextLevel) {
            double resto = xp - getXpToEachLevel((int) info.getLvl() + 1);
            info.setLvl(info.getLvl() + 0.1);
            info.setXp(resto);
            p.sendMessage(ChatColor.AQUA + "Your skill in " + skill.name() + " has increased by 0.1 and is now " + info.getLvl());
        } else {
            info.setXp(xp);
            GeneralUtils.sendActionBar(p, "§a§l" + skill.name() + "    §c" + xp + "§9/§e" + xpToNextLevel);
        }
        NewKom.database.skills.updateSkills(p, skills);
    }

    //////// REMOVE //////////
    public static void levelUpSkill(Player p, SkillEnum skill, double minLvl, double expRatio) {
        earnXp(p, skill, minLvl, expRatio);
    }
    
    public static double getXpToEachLevel(int lvl) {
        return lvl * lvl;
        /*
         Demo Table:
         Lvl 10 = 100 xp
         Lvl 20 = 400 xp
         Lvl 50 = 2500 xp
         Lvl 100 = 10000xp
         */
    }
    
    public static double getChancesToSuccess(SkillEnum skill, double minSkill, double mySkill) {
        if (mySkill < 10) {
            mySkill = 10;
        }
        double chance = (mySkill - minSkill) * 2;
        if (chance < 0) {
            chance = 0;
        }
        return chance;
    }
    
    public static boolean takePercentageChance(double pct) {
        return rnd.nextInt(100) <= pct;
    }
    
    public static SkillResult hasSucess(SkillEnum skill, Player p, double minSkill) {
        double skillValue = NewKom.database.skills.getSkills(p).get(skill).getLvl();

        // have more then 30 missing to complete that
        if (skillValue + 30 < minSkill) {
            return SkillResult.EPIC_FAIL;
        }
        
        double chance = getChancesToSuccess(skill, minSkill, skillValue);
        // i dont have enought skill to make this
        if (chance == 0) {
            // 20% chance epic fail
            if (takePercentageChance(20)) {
                return SkillResult.EPIC_FAIL;
            } else {
                return SkillResult.FAIL;
            }
        } else {
            // i have chances on making this !!
            if (takePercentageChance(chance)) {
                // I MADE IT !

                // if i have 10 points or less over the limit... it can become 'average'
                if (skillValue - minSkill < 10) {
                    double chanceToAverage = (skillValue - minSkill) * 3; // mostly 30% at most
                    if (takePercentageChance(chanceToAverage)) {
                        return SkillResult.ALMOST_FAILED;
                    }
                }
                
                double epicSucessChance = chance / 4;
                // will it become EPIC ?
                if (takePercentageChance(epicSucessChance)) {
                    return SkillResult.EPIC_SUCCESS;
                } else {
                    return SkillResult.SUCCESS;
                }
            } else {
                // i failed...
                double chanceToEpicFail = 60 - chance;
                if (chanceToEpicFail > 0 && takePercentageChance(chanceToEpicFail)) {
                    return SkillResult.EPIC_FAIL;
                } else {
                    return SkillResult.FAIL;
                }
            }
        }
        
    }
    
}
