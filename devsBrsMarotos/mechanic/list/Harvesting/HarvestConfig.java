/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Harvesting;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.Lang.ConfigManager;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import devsBrsMarotos.mechanic.list.Skills.SkillList.Lumberjacking.TreeType;
import java.util.HashSet;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author vntgasl
 * 
 */

public class HarvestConfig extends Mecanica {

    public static ConfigManager ConfigFile;
    public static ConfigManager MiningCfg;

    @Override
    public void onEnable() {
        Load();
        TreeType.fix();
    }
    
    public static void Load() {
        try {
            ConfigFile = new ConfigManager(NewKom.instancia.getDataFolder() + "/Harvesting.yml"); 
            
            HashSet<Harvestable> harvestables = getAll();
            for(Harvestable h : harvestables) {
                HarvestCache.add(h);
                NewKom.log.info("ADDING "+h.m.name()+" "+h.skillsNeeded.name());
            }   
        } catch (Exception ex) {
             ex.printStackTrace();
        }
    }
    
    /*
    Blacksmithy:
        IRON_DOOR-0:
            MinSkill: 35
            Cooldown: 1
    */
    
    private static HashSet<Harvestable> getAll() {
        HashSet<Harvestable> harvestables = new HashSet<Harvestable>();
        for(SkillEnum skill : SkillEnum.values()) {
            ConfigurationSection section = ConfigFile.getConfig().getConfigurationSection(skill.name());
            if(section==null) continue;
            for(String key : section.getKeys(false)) {
                String materialName = key.split("-")[0];
                byte data = Byte.parseByte(key.split("-")[1]);
                double minSkill = ConfigFile.getConfig().getDouble(skill.name()+"."+key+".MinSkill");
                int cooldown = ConfigFile.getConfig().getInt(skill.name()+"."+key+".Cooldown");
                double expRatio = ConfigFile.getConfig().getDouble(skill.name()+"."+key+".ExpRatio");
                Harvestable harvest = new Harvestable(Material.valueOf(materialName),data, skill, minSkill, cooldown, expRatio);
                harvestables.add(harvest);
            }
        }
        
        return harvestables;
    }
    
    public static void add(Harvestable h) {
        String node = h.skillsNeeded.name()+"."+h.m.name()+"-"+h.data;
        ConfigFile.getConfig().set(node+".MinSkill", h.minSkill);
        ConfigFile.getConfig().set(node+".Cooldown", h.cooldown);
        ConfigFile.getConfig().set(node+".ExpRatio", h.expRatio);
        ConfigFile.SaveConfig();
        HarvestCache.add(h);
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
       return "HarvestConfig";
    }
    
}
