package devsBrsMarotos.mechanic;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public abstract class Mecanica implements Listener {
    public Plugin plugin;
    
    public abstract String getName();

    public void onEnable() {
        // pra re-escrever se precisar
    }
    
    public void onDisable() {
        // pra re-escrever se precisar
    }
    
}
