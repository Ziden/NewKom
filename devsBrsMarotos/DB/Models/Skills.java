/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.DB.Models;

import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;

/**
 *
 * @author vntgasl
 */
public class Skills {

    public HashMap<SkillEnum, SkillInfo> skills = new HashMap<SkillEnum, SkillInfo>();

    public SkillInfo get(SkillEnum skill) {
        if (skills.containsKey(skill)) {
            return skills.get(skill);
        } else {
            return new SkillInfo();
        }
    }
    
    public double total() {
        double i = 0;
        for(SkillInfo info : skills.values()) {
            i += info.lvl;
        }
        return i;
    }

    public static class SkillInfo {

        double xp = 0;
        double lvl = 1;
        double xptotal = 0;

        public SkillInfo(double lvl, double xp, double xptotal) {
            this.xp = xp;
            this.lvl = lvl;
            this.xptotal = xptotal;
        }

        public SkillInfo() {
        }

        public double getXp() {
            return xp;
        }

        public double getLvl() {
            return lvl;
        }

        public void setXpTotal(double xptotal) {
            this.xptotal = xptotal;
        }

        public void setXp(double xp) {
            this.xp = xp;
        }

        public void setLvl(double lvl) {
            DecimalFormat df = new DecimalFormat("#.#");
            //GAMBETA? SIM OU CLARO? - Adaptação Técnica, by ziden
            this.lvl = Double.valueOf(df.format(lvl).replace(",", ","));
        }

        public double getXpTotal() {
            return xptotal;
        }

    }
}
