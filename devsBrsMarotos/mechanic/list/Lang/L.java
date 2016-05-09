package devsBrsMarotos.mechanic.list.Lang;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import java.io.IOException;
import org.bukkit.configuration.InvalidConfigurationException;

/**
 *
 * @author Gabriel
 */
public class L extends Mecanica{

    public static ConfigManager ConfLanguage;
    public static ConfigManager Lang;

    @Override
    public void onEnable() {
        LoadLang();
    }
    
    public static void LoadLang() {
        try {
            ConfLanguage = new ConfigManager(NewKom.instancia.getDataFolder() + "/strings.yml");
            Lang = new ConfigManager(NewKom.instancia.getDataFolder() + "/lang.yml");
        } catch (Exception ex) {
             ex.printStackTrace();
        }
    }

    public static String get(String node) {
        return L.ConfLanguage.getConfig().getString(node);
    }
    
    public static String m(String msg) {
        if(Lang==null || Lang.getConfig()==null)
            LoadLang();
         String node = msg.replaceAll("\\ ", "");
         node = node.replaceAll("\\.", "");
         node = node.replaceAll("\\:", "");
         if (!Lang.getConfig().contains(node)) {
         Lang.getConfig().set(node, msg);
         Lang.SaveConfig();
         }
         return L.Lang.getConfig().getString(node);
         
        //return msg;
    }

    public static String m(String msg, String parm) {
        String node = msg.replaceAll("\\ ", "");
        node = node.replaceAll("\\.", "");
        node = node.replaceAll("\\:", "");
        if (!Lang.getConfig().contains(node)) {
            Lang.getConfig().set(node, msg);
            Lang.SaveConfig();
        }
        return L.Lang.getConfig().getString(node).replace("%", parm);
    }
    
    public static String m(String msg, int parm) {
        String node = msg.replaceAll("\\ ", "");
         node = node.replaceAll("\\.", "");
         node = node.replaceAll("\\:", "");
        if (!Lang.getConfig().contains(node)) {
            Lang.getConfig().set(node, msg);
            Lang.SaveConfig();
        }
        return L.Lang.getConfig().getString(node).replace("%", ""+parm);
    }
    
     public static String m(String msg, long parm) {
        String node = msg.replaceAll("\\ ", "");
        node = node.replaceAll("\\.", "");
        node = node.replaceAll("\\:", "");
        if (!Lang.getConfig().contains(node)) {
            Lang.getConfig().set(node, msg);
            Lang.SaveConfig();
        }
        return L.Lang.getConfig().getString(node).replace("%", ""+parm);
    }

    public static void i(String node, Object value) {
        if (!Lang.getConfig().contains(node)) {
            Lang.getConfig().set(node, value);
        }
        Lang.SaveConfig();
    }

    public static void init(String node, Object value) {
        if (!ConfLanguage.getConfig().contains(node)) {
            ConfLanguage.getConfig().set(node, value);
        }
        ConfLanguage.SaveConfig();
    }

    @Override
    public String getName() {
       return "Language";
    }
}
