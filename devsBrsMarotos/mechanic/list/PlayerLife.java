/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list;

import devsBrsMarotos.DB.Models.PlayerData;
import devsBrsMarotos.DB.Models.Stage;
import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.CustomItems.CustomItem;
import devsBrsMarotos.mechanic.list.CustomItems.list.Heart;
import devsBrsMarotos.mechanic.list.CustomItems.list.RegenHeart;
import devsBrsMarotos.mechanic.list.Lang.L;
import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

/**
 *
 * @author vntgasl
 *
 */
public class PlayerLife extends Mecanica {

    public static void increaseLife(Player p) {
        PlayerData data = NewKom.database.playerData.getPlayerData(p);
        data.life += 2;
        NewKom.database.playerData.updatePlayerData(p, data);
        p.sendMessage(ChatColor.GREEN + L.m("Congratulations ! Your life has increased by 2 and you got healed !!"));
        p.setMaxHealth(data.life);
        p.setHealth(p.getMaxHealth());
        Stage playerStages = NewKom.database.stages.getPlayerStage(p);
        if (!playerStages.stagesCompleted.contains(Stage.PredefinedStages.GOT_FIRST_HEART.name())) {
            p.sendMessage(ChatColor.GREEN + L.m("You can find hidden Life Powerups in the realm of Aden !!"));
            p.sendMessage(ChatColor.GREEN + L.m("Search every place to be the strongest !!"));
            NewKom.database.stages.addCompletedStage(p, Stage.PredefinedStages.GOT_FIRST_HEART.name());
        }
    }
    
    @EventHandler
    public void pickup(PlayerPickupItemEvent ev) {
        
        CustomItem ci = CustomItem.getCustomItem(ev.getItem().getItemStack());
        if(ci!=null && ci instanceof RegenHeart) {
            if(ev.getPlayer().getHealth()==ev.getPlayer().getMaxHealth()) {
                ev.setCancelled(true);
                return;
            }
            ev.getPlayer().sendMessage(ChatColor.RED+L.m("Picked up a Heart !"));
            ev.getPlayer().playEffect(EntityEffect.WOLF_HEARTS);
            ev.getPlayer().playSound(null, Sound.NOTE_PIANO, 1, 1);
            double health = ev.getPlayer().getHealth()+1;
            if(health > ev.getPlayer().getMaxHealth())
                ev.getPlayer().setHealth(ev.getPlayer().getMaxHealth());
            else
                ev.getPlayer().setHealth(health);
            ev.getItem().setItemStack(null);
            ev.getItem().remove();
            ev.setCancelled(true);
        }
    }
    
    @EventHandler
    public void gainHealth(EntityRegainHealthEvent ev) {
        if(ev.getRegainReason()==RegainReason.SATIATED || ev.getRegainReason()==RegainReason.REGEN)
            ev.setCancelled(true);
    }

    @EventHandler
    public static void invClick(InventoryClickEvent ev) {
        if (ev.getCurrentItem() != null && ev.getWhoClicked().getType() == EntityType.PLAYER) {
            CustomItem item = CustomItem.getCustomItem(ev.getCurrentItem());
            if (item != null) {
                if (item instanceof Heart) {
                    if(((Player) ev.getWhoClicked()).isOp()) {
                        ((Player) ev.getWhoClicked()).sendMessage("You didnt picked up the life cause you are OP !");
                        return;
                    }
                    ev.setCurrentItem(null);
                    increaseLife((Player) ev.getWhoClicked());
                }
            }
        }
    }

    @EventHandler
    public static void drop(PlayerDropItemEvent ev) {
        if (ev.getItemDrop().getItemStack() != null) {
            CustomItem item = CustomItem.getCustomItem(ev.getItemDrop().getItemStack());
            if (item != null) {
                if (item instanceof Heart) {
                    ev.getItemDrop().setItemStack(null);
                    increaseLife(ev.getPlayer());
                }
            }
        }
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent ev) {
        PlayerData data = NewKom.database.playerData.getPlayerData(ev.getPlayer());
        ev.getPlayer().setMaxHealth(data.life);

        if (ev.getPlayer().isOp()) {
            ev.getPlayer().setGameMode(GameMode.CREATIVE);
        }

    }

    @Override
    public String getName() {
        return "Player Life";
    }

}
