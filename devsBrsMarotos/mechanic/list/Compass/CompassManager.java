package devsBrsMarotos.mechanic.list.Compass;

import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.Events.UpdateEvent;
import devsBrsMarotos.mechanic.list.Events.UpdateType;
import devsBrsMarotos.util.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author Carlos
 */
public class CompassManager extends Mecanica {

    @Override
    public String getName() {
        return "Compass Manager";
    }

    @EventHandler
    public void interact(PlayerInteractEvent ev) {
        if (ev.getAction() == Action.RIGHT_CLICK_BLOCK || ev.getAction() == Action.RIGHT_CLICK_AIR) {
            if (ev.getPlayer().getItemInHand() != null && ev.getPlayer().getItemInHand().getType() == Material.COMPASS) {
                ev.setCancelled(true);
                CompassInventory.open(ev.getPlayer());
            }
        }
    }

    @EventHandler
    public void update(UpdateEvent ev) {
        if (ev.getType() == UpdateType.FASTER) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (CompassInventory.locs.hasCache(p.getUniqueId())) {
                    Location l = CompassInventory.locs.getCached(p.getUniqueId()).l.clone();
                    if (l.getWorld() == p.getWorld()) {
                        for (double x = 0; x < 20; x += 0.5) {
                            ParticleEffect.BLOCK_DUST.display(new ParticleEffect.BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.1F, 0.1F, 0.1F, 0, 10, l.clone().add(0, x, 0), p);
                        }
                    }

                }
            }
        }
    }

}
