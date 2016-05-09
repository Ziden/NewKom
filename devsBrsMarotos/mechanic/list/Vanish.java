/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list;

import devsBrsMarotos.mechanic.Mecanica;
import java.util.HashSet;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author User
 */
public class Vanish extends Mecanica {

    private static HashSet<UUID> vanished = new HashSet<UUID>();

    @EventHandler
    public void playerJoin(PlayerJoinEvent ev) {
         update(ev.getPlayer());
    }

    private static void update(Player p) {
        if (vanished.contains(p.getUniqueId())) {
            for (Player on : Bukkit.getOnlinePlayers()) {
                if (!on.isOp()) {
                    on.hidePlayer(p);
                }
            }
        } else {
            for (Player on : Bukkit.getOnlinePlayers()) {
                on.showPlayer(p);
            }
        }
    }

    public static void vanish(Player p) {
        vanished.add(p.getUniqueId());
        update(p);
    }

    public static void appear(Player p) {
        vanished.remove(p.getUniqueId());
        update(p);
    }

    public static boolean isVanished(Player p) {
        return vanished.contains(p.getUniqueId());
    }

    @Override
    public String getName() {
        return "Vanish";
    }

}
