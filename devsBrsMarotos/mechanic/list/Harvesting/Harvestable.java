/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Harvesting;

import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author vntgasl
 */
public class Harvestable {

    public Harvestable(Material m, byte data, SkillEnum skill, double minSkill, int cooldown, double expRatio) {
        this.m = m;
        this.data = data;
        this.skillsNeeded = skill;
        this.minSkill = minSkill;
        this.cooldown = cooldown;
        this.expRatio = expRatio;
    }

    public ItemStack getIcon() {
        ItemStack ss = new ItemStack(m,1,data);
        ItemMeta meta = ss.getItemMeta();
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.YELLOW + skillsNeeded.name());
        lore.add(ChatColor.GREEN + "Min Skill Needed: " + minSkill);
        lore.add(ChatColor.GREEN+(cooldown+" minutes to re-harvest "));
        meta.setLore(lore);
        ss.setItemMeta(meta);
        return ss;
    }

    public double expRatio;
    public int cooldown;
    public Material m;
    public byte data = 0;
    public SkillEnum skillsNeeded; // MEDIUM will be calculated
    public double minSkill;

}
