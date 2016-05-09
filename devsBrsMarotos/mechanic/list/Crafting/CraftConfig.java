/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Crafting;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.Harvesting.HarvestCache;
import devsBrsMarotos.mechanic.list.Harvesting.Harvestable;
import devsBrsMarotos.mechanic.list.Lang.ConfigManager;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import java.util.HashSet;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author User
 * 
 */

public class CraftConfig extends Mecanica {
    public static ConfigManager ConfigFile;
 
    @Override
    public void onEnable() {
        Load();
    }
    
    public static void Load() {
        try {
            ConfigFile = new ConfigManager(NewKom.instancia.getDataFolder() + "/Crafting.yml"); 
            HashSet<Craftable> harvestables = getAll();
            for(Craftable h : harvestables) {
                CraftCache.add(h);
                NewKom.log.info("+Craftable "+h.m.name()+" "+h.skillsNeeded.name());
            }   
        } catch (Exception ex) {
             ex.printStackTrace();
        }
    }

    private static HashSet<Craftable> getAll() {
        HashSet<Craftable> craftables = new HashSet<Craftable>();
        for(SkillEnum skill : SkillEnum.values()) {
            ConfigurationSection section = ConfigFile.getConfig().getConfigurationSection(skill.name());
            if(section==null) continue;
            for(String key : section.getKeys(false)) {
                String materialName = key.split("-")[0];
                byte data = Byte.parseByte(key.split("-")[1]);
                double minSkill = ConfigFile.getConfig().getDouble(skill.name()+"."+key+".MinSkill");
                double expRatio = ConfigFile.getConfig().getDouble(skill.name()+"."+key+".ExpRatio");
                Craftable craft = new Craftable(Material.valueOf(materialName),data, minSkill, skill,expRatio);
                craftables.add(craft);
            }
        }
        return craftables;
    }
    
    public static void add(Craftable h) {
        String node = h.skillsNeeded.name()+"."+h.m.name()+"-"+h.data;
        ConfigFile.getConfig().set(node+".MinSkill", h.minSkill);
        ConfigFile.getConfig().set(node+".ExpRatio", h.expRatio);
        ConfigFile.SaveConfig();
        CraftCache.add(h);
    }

    public static String get(String node) {
        return L.ConfLanguage.getConfig().getString(node);
    }
    
    public static void init(String node, Object value) {
        if (!ConfigFile.getConfig().contains(node)) {
            ConfigFile.getConfig().set(node, value);
        }
        ConfigFile.SaveConfig();
    }

    
    @Override
    public String getName() {
       return "Crafting Config";
    }
    
}
