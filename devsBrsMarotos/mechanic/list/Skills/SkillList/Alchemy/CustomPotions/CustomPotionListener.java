package devsBrsMarotos.mechanic.list.Skills.SkillList.Alchemy.CustomPotions;

import devsBrsMarotos.DB.Models.Skills;
import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.list.CustomItems.*;
import devsBrsMarotos.mechanic.list.CustomItems.CustomItem;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import devsBrsMarotos.util.Cooldown;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

/**
 *
 * @author vntgasl
 *
 */
public class CustomPotionListener extends Mecanica {

    @EventHandler
    public void consume(PlayerItemConsumeEvent ev) {
        NewKom.log.info("DRINK !");
        CustomPotion item = CustomPotion.getCustomItem(ev.getItem());
        if(item!=null) {
            NewKom.log.info("DRINK CUSTOM POTION !");
            item.drink(ev);
        }
    }
    
    @EventHandler
    public void potionSplash(PotionSplashEvent ev) {
        CustomPotion item = CustomPotion.getCustomItem(ev.getEntity().getItem());
        if(item!=null) {
            item.splashEvent(ev);
            ev.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void interage(PlayerInteractEvent ev) {
        if (ev.getAction() == Action.RIGHT_CLICK_BLOCK || ev.getAction() == Action.RIGHT_CLICK_AIR) {
            if (ev.getPlayer().getItemInHand() != null) {
                CustomPotion item = CustomPotion.getCustomItem(ev.getPlayer().getItemInHand());
                if (item != null) {

                    if (item.isSplash) {
                        if (Cooldown.isCooldown(ev.getPlayer(), "throwPotion")) {
                            ev.setCancelled(true);
                            return;
                        }
                        Cooldown.setMetaCooldown(ev.getPlayer(), "throwPotion", 1000);
                        Skills s = NewKom.database.skills.getSkills(ev.getPlayer());
                        double alchemySkill = s.get(SkillEnum.Alchemy).getLvl();
                        double minSkill = item.getMinimumSkill();
                        if (alchemySkill < minSkill) {
                            ev.getPlayer().sendMessage(ChatColor.RED + L.m("You need more alchemy skill to throw this potion !"));
                            ev.setCancelled(true);
                            return;
                        }
                    }
                   
                    item.interage(ev);
                    if (item.blockInteract()) {
                        ev.setCancelled(true);
                    }
                }
            }
        }
    }

    @Override
    public void onEnable() {
        PotionLoader.load();
    }

    @Override
    public String getName() {
        return "Custom Items";
    }

}
