package devsBrsMarotos.mechanic.list.CustomItems;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.list.CustomItems.CustomItem;
import devsBrsMarotos.mechanic.Mecanica;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author vntgasl
 * 
 */

public class CustomItemListener extends Mecanica {

    @EventHandler(priority = EventPriority.LOWEST)
    public void interage(PlayerInteractEvent ev) {
        if (ev.getAction() == Action.RIGHT_CLICK_BLOCK || ev.getAction() == Action.RIGHT_CLICK_AIR) {
            if (ev.getPlayer().getItemInHand() != null) {
                CustomItem item = CustomItem.getCustomItem(ev.getPlayer().getItemInHand());
                if (item != null) {
                    item.interage(ev);
                    if(item.blockInteract())
                        ev.setCancelled(true);
                    else
                        NewKom.log.info("CAN? "+ev.isCancelled());
                }
            }
        }
    }
    
    @Override
    public void onEnable() {
        ItemLoader.load();
    }

    @Override
    public String getName() {
       return "Custom Items";
    }

}
