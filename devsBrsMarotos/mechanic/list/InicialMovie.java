/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list;

import devsBrsMarotos.DB.Models.Stage;
import devsBrsMarotos.DB.Models.Stage.PredefinedStages;
import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.Lang.L;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 *
 * @author vntgasl
 */
public class InicialMovie extends Mecanica {

    @Override
    public String getName() {
        return "Initial Story";
    }

    public Random rnd = new Random();

    public Location boatSpot;
    public Location blackSpot;
    public Location startSpot;

    @EventHandler
    public void damage(EntityDamageEvent ev) {
        if (ev.getEntity().getWorld().getName().equalsIgnoreCase(NewKom.darkWorld)) {
            ev.setCancelled(true);
        }
    }

    @EventHandler
    public void playerJoin(final PlayerJoinEvent ev) {
        final Stage playerStages = NewKom.database.stages.getPlayerStage(ev.getPlayer());

        if (!playerStages.stagesCompleted.contains(PredefinedStages.SEEN_INTRO.name())) {
            boatSpot = new Location(Bukkit.getWorld(NewKom.darkWorld), 2, 14, -19);
            blackSpot = new Location(Bukkit.getWorld(NewKom.darkWorld), 125, 4, -39);
            startSpot = new Location(Bukkit.getWorld(NewKom.world), 22, 40, 9);
            final Runnable movie = new Runnable() {
                public void run() {

                    Vanish.vanish(ev.getPlayer());
                    final World w = Bukkit.getWorld(NewKom.darkWorld);
                    w.setTime(16000L);
                    w.setStorm(true);
                    w.setThundering(true);
                    ev.getPlayer().teleport(boatSpot);
                    final String sailor = ChatColor.GREEN + "[Sailor] " + ChatColor.YELLOW;
                    final String myPrefix = ChatColor.GREEN + "[Lord " + ev.getPlayer().getName() + "] " + ChatColor.YELLOW;
                    ev.getPlayer().sendMessage(myPrefix + L.m("Sailor, where are you ???"));
                    ev.getPlayer().sendMessage(myPrefix + L.m("A storm is coming down !!!"));
                    ev.getPlayer().getWorld().strikeLightningEffect(new Location(w, boatSpot.getBlockX() + rnd.nextInt(20) - 10, 20, boatSpot.getBlockZ() + rnd.nextInt(20) - 10));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

                        @Override
                        public void run() {
                            ev.getPlayer().getWorld().strikeLightningEffect(new Location(w, boatSpot.getBlockX() + rnd.nextInt(20) - 10, 20, boatSpot.getBlockZ() + rnd.nextInt(20) - 10));
                            ev.getPlayer().sendMessage(sailor + L.m("Down here my lord ! The door is stuck !!!"));
                            ev.getPlayer().sendMessage(sailor + L.m("The storm is too strong !! I Think we are going to SINK !!!!!"));
                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

                                @Override
                                public void run() {
                                    ev.getPlayer().getWorld().strikeLightningEffect(new Location(w, boatSpot.getBlockX() + rnd.nextInt(20) - 10, 20, boatSpot.getBlockZ() + rnd.nextInt(20) - 10));
                                    ev.getPlayer().sendMessage(myPrefix + L.m("NO WE ARENT  !!!! Hang on there !"));
                                    ev.getPlayer().getWorld().createExplosion(boatSpot.getBlockX() + rnd.nextInt(20) - 10, 20, boatSpot.getBlockZ() + rnd.nextInt(20) - 10, 10, false, false);
                                    ev.getPlayer().getWorld().createExplosion(boatSpot.getBlockX() + rnd.nextInt(20) - 10, 20, boatSpot.getBlockZ() + rnd.nextInt(20) - 10, 10, false, false);
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

                                        @Override
                                        public void run() {
                                            ev.getPlayer().getWorld().createExplosion(boatSpot.getBlockX() + rnd.nextInt(20) - 10, 20, boatSpot.getBlockZ() + rnd.nextInt(20) - 10, 10, false, false);
                                            ev.getPlayer().getWorld().createExplosion(boatSpot.getBlockX() + rnd.nextInt(20) - 10, 20, boatSpot.getBlockZ() + rnd.nextInt(20) - 10, 10, false, false);
                                            ev.getPlayer().getWorld().strikeLightningEffect(new Location(w, boatSpot.getBlockX() + rnd.nextInt(20) - 10, 20, boatSpot.getBlockZ() + rnd.nextInt(20) - 10));
                                            ev.getPlayer().sendMessage(sailor + L.m("NOOOOOOOOOOOOOO !!!"));
                                            ev.getPlayer().sendMessage(sailor + L.m("THE BOAT HAS BURST UP MY LORD !!!"));
                                            ev.getPlayer().sendMessage(myPrefix + L.m("Ouch, my head !"));
                                            ev.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 20, 1));
                                            ev.getPlayer().teleport(blackSpot);
                                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

                                                @Override
                                                public void run() {
                                                    ev.getPlayer().sendMessage(myPrefix + L.m("Ugh... but ... we didnt... hang on... sailor..."));
                                                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

                                                        @Override
                                                        public void run() {
                                                            ev.getPlayer().sendMessage(myPrefix + L.m("Where am ..i  ?..."));
                                                            ev.getPlayer().sendMessage(myPrefix + L.m("Can i... open my eyes ?"));
                                                            ev.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 10, 0));
                                                            ev.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 10, 0));
                                                            ev.getPlayer().teleport(startSpot);
                                                            NewKom.database.stages.addCompletedStage(ev.getPlayer(), PredefinedStages.SEEN_INTRO.name());
                                                            Vanish.appear(ev.getPlayer());
                                                        }

                                                    }, 20 * 10);
                                                }

                                            }, 20 * 10);

                                        }

                                    }, 20 * 10);
                                }

                            }, 20 * 10);
                        }

                    }, 20 * 10);
                }
            };
            Runnable preMovie = new Runnable() {
                public void run() {
                    ev.getPlayer().teleport(blackSpot);
                    ev.getPlayer().sendMessage(ChatColor.GREEN + L.m("You are ") + ev.getPlayer().getName() + L.m(", an explorer who sails the world for adventure and treasures !"));
                    ev.getPlayer().sendMessage(ChatColor.GREEN + L.m("You sailed for a far land , searching for adventure... but not everything worked out as expected..."));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, movie, 30);
                }
            };
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, preMovie, 20);
        }
    }

}
