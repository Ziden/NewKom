/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Crafting;

import devsBrsMarotos.mechanic.list.Harvesting.Harvestable;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import java.util.HashMap;
import java.util.HashSet;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author User
 *
 */
public class CraftCache {

    private static HashMap<SkillEnum, HashSet<Craftable>> craftBySkill = new HashMap<SkillEnum, HashSet<Craftable>>();
    private static HashMap<Integer, Craftable> harvestableByMaterial = new HashMap<Integer, Craftable>();

    public static void add(Craftable h) {
        if (craftBySkill.containsKey(h.skillsNeeded)) {
            HashSet<Craftable> harvestables = craftBySkill.get(h.skillsNeeded);
            harvestables.add(h);
        } else {
            HashSet<Craftable> harvestables = new HashSet<Craftable>();
            harvestables.add(h);
            craftBySkill.put(h.skillsNeeded, harvestables);
        }
        harvestableByMaterial.put(h.m.getId(), h);
    }

    public static HashSet<Craftable> getCraftables(SkillEnum m) {
        return craftBySkill.get(m);
    }

    public static Craftable getCraftable(Material m) {
        if (harvestableByMaterial.containsKey(m.getId())) {
            return harvestableByMaterial.get(m.getId());
        }
        return null;
    }

}
