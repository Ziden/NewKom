/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Crafting;

import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author User
 */
public class Craftable {
    
    public SkillEnum skillsNeeded;
    public double minSkill;
    public Material m;
    public byte data;
    public double expRatio;
    
    public Craftable(Material m, byte data, double minSkill, SkillEnum skillsNeeded, double expRatio) {
        this.m = m;
        this.data = data;
        this.minSkill = minSkill;
        this.skillsNeeded = skillsNeeded;
        this.expRatio = expRatio;
    }
    
      public ItemStack getIcon() {
        ItemStack ss = new ItemStack(m,1,data);
        ItemMeta meta = ss.getItemMeta();
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.YELLOW + skillsNeeded.name());
        lore.add(ChatColor.GREEN + "Min Skill Needed: " + minSkill);
        meta.setLore(lore);
        ss.setItemMeta(meta);
        return ss;
    }
    
}
