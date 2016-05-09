/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import devsBrsMarotos.DB.Database;
import devsBrsMarotos.DB.MobConfigDB;
import devsBrsMarotos.cmds.Comando;
import devsBrsMarotos.cmds.CommandLoader;
import devsBrsMarotos.mechanic.MLoader;
import devsBrsMarotos.mechanic.list.CustomCraftRecipes.RecipeLoader;
import devsBrsMarotos.mechanic.list.MobConfigs.SpawnMob;
import devsBrsMarotos.mechanic.list.WorldGen.ChunkCreator;
import devsBrsMarotos.mechanic.list.rankings.RankDB;
import devsBrsMarotos.util.Traducoes;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author vntgasl
 */
public class NewKom extends JavaPlugin {

    public static String TAG = ChatColor.GOLD + "[KoM]";
    public static String world = "Aden2";
    public static String alertTag = "§f[§6§l!§f] §e";
    public static String successTag = "§f[§2§l!§f] §a";
    public static String errorTag = "§f[§4§l!§f] §c";
    public static String darkWorld = "darkWorld";
    public static NewKom instancia;
    public static final Logger log = Logger.getLogger("Minecraft");
    public static Traducoes trad;
    public static Database database = null;
    public static WorldGuardPlugin worldGuard = null;
    public static ProtocolManager protocolManager;
    

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new ChunkCreator();
    }

    public void onEnable() {
       
        instancia = this;
        trad = new Traducoes();
        database = new Database();
        database.inicializa();
        RankDB.InitMysql();
        MobConfigDB.init();
        MobConfigDB.loadConfigs();
        worldGuard = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
        protocolManager = ProtocolLibrary.getProtocolManager();
        MLoader.load();
        loadCommands();
        SpawnMob startSpawn = new SpawnMob();
        RecipeLoader.load();
 
        
      
    }

    public void onDisable() {
        RankDB.saveAll();
        MLoader.disable();
    }

    
    private static CommandMap cmap;

    public void loadCommands() {
        try {
            if (Bukkit.getServer() instanceof CraftServer) {
                final Field f = CraftServer.class.getDeclaredField("commandMap");
                f.setAccessible(true);
                cmap = (CommandMap) f.get(Bukkit.getServer());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        CommandLoader.load();
    }

    public static void addCommand(Comando cmd) {
        cmap.register(cmd.getName(), cmd);
        cmd.setExecutor(instancia);
    }

}
