/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.InitialKits;

import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author User
 */
public class Kit {
    
    public String [] desc;
    public String name;
    public HashMap<SkillEnum, Double> skills = new HashMap<SkillEnum, Double>();
    public List<ItemStack> initialItems = new ArrayList<ItemStack>();
    
}
