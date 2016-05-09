/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Harvesting;

import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 *
 * @author vntgasl
 */
public class HarvestCache {

    private static HashMap<SkillEnum, HashSet<Harvestable>> harvestablesBySkill = new HashMap<SkillEnum, HashSet<Harvestable>>();
    private static HashMap<Integer, HashSet<Harvestable>> harvestableByMaterial = new HashMap<Integer, HashSet<Harvestable>>();

    public static void add(Harvestable h) {
        if (harvestablesBySkill.containsKey(h.skillsNeeded)) {
            HashSet<Harvestable> harvestables = harvestablesBySkill.get(h.skillsNeeded);
            harvestables.add(h);
        } else {
            HashSet<Harvestable> harvestables = new HashSet<Harvestable>();
            harvestables.add(h);
            harvestablesBySkill.put(h.skillsNeeded, harvestables);
        }
        if (harvestableByMaterial.containsKey(h.m.getId())) {
            HashSet<Harvestable> harvestables = harvestableByMaterial.get(h.m.getId());
            harvestables.add(h);
        } else {
            HashSet<Harvestable> harvestables = new HashSet<Harvestable>();
            harvestables.add(h);
            harvestableByMaterial.put(h.m.getId(), harvestables);
        }

    }

    public static HashSet<Harvestable> getHarvestable(SkillEnum m) {
        return harvestablesBySkill.get(m);
    }

    public static HashSet<Harvestable> getHarvestable(Material m) {
        HashSet<Harvestable> harvestables = new HashSet<Harvestable>();
        HashSet<Harvestable> byBlock = harvestableByMaterial.get(m.getId());
        if (byBlock != null) {
            for (Harvestable harv : byBlock) {
                harvestables.add(harv);
            }
        }
        return harvestables;
    }
    
    
    public static Harvestable getHarvestable(Material m, byte data) {
        HashSet<Harvestable> harvs = harvestableByMaterial.get(m.getId());
            for (Harvestable harv : harvs) {
                if(harv.data==data && harv.m==m)
                return harv;
            }
        return null;
    }

    public static Harvestable getHarvestable(Block b) {
        Material blockMat = b.getType();
        byte data = b.getData();
        HashSet<Harvestable> byBlock = harvestableByMaterial.get(blockMat.getId());
        if (byBlock != null) {
            for (Harvestable harv : byBlock) {
                if (harv.data == data) {
                    return harv;
                }
            }
        }
        return null;
    }
}
