/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills.SkillList.Magery;

import org.bukkit.ChatColor;

/**
 *
 * @author User
 */
public enum Elements {

    Fire(ChatColor.RED + "☣"), Thunder(ChatColor.DARK_AQUA + "☼"), Earth(ChatColor.DARK_GREEN + "☢");
    
    public String icon;
    
    private Elements(String icon) {
        this.icon = icon;
    }
    
}
