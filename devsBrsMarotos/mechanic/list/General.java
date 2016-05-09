package devsBrsMarotos.mechanic.list;

import devsBrsMarotos.mechanic.Mecanica;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;

/**
 *
 * @author Carlos
 */
public class General extends Mecanica {

    @Override
    public String getName() {
        return "General";
    }

    @EventHandler
    public void ping(ServerListPingEvent ev) {
        ev.setMaxPlayers(Bukkit.getOnlinePlayers().size() + 1);
        ev.setMotd("§l§fNew §c§lKnights of Minecraft");

    }

}
