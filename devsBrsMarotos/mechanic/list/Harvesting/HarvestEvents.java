/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Harvesting;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import devsBrsMarotos.DB.Models.Skills;
import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.CustomItems.CustomItem;
import devsBrsMarotos.mechanic.list.CustomItems.list.Heart;
import devsBrsMarotos.mechanic.list.CustomItems.list.RegenHeart;
import devsBrsMarotos.mechanic.list.Skills.SkillList.Mining;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import devsBrsMarotos.mechanic.list.Skills.SkillEvents.BlockHarvestEvent;
import devsBrsMarotos.mechanic.list.Skills.SkillFormulas;
import devsBrsMarotos.mechanic.list.Skills.SkillFormulas.SkillResult;
import devsBrsMarotos.util.Cooldown;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author User
 */
public class HarvestEvents extends Mecanica {

    @Override
    public String getName() {
        return "Harvest Events";
    }

    public static void sendCrack(Player pBreaker, Location loc) {
        try {
            PacketContainer blockBreakAnimation = NewKom.protocolManager.createPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
            blockBreakAnimation.getIntegers().write(0, Integer.valueOf(500));
            blockBreakAnimation.getIntegers().write(1, Integer.valueOf(loc.getBlockX()));
            blockBreakAnimation.getIntegers().write(2, Integer.valueOf(loc.getBlockY()));
            blockBreakAnimation.getIntegers().write(3, Integer.valueOf(loc.getBlockZ()));
            blockBreakAnimation.getIntegers().write(4, Integer.valueOf(5));
            NewKom.protocolManager.sendServerPacket(pBreaker, blockBreakAnimation);

            loc.getWorld().playEffect(loc, Effect.STEP_SOUND, loc.getBlock().getType());
        } catch (Exception e) {
        }
    }

    public static void setPersonalBlock(final Player p, final Location l, final Material m, final byte b) {
        Runnable r = new Runnable() {
            public void run() {
                p.sendBlockChange(l, m, b);
            }
        };
        Bukkit.getScheduler().scheduleSyncDelayedTask(NewKom.instancia, r, 10);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void breakBlock(final BlockBreakEvent ev) {
       //GAMBETA PORCAUSA DAS ARVORES
        if (ev.getBlock().getType() == Material.AIR || ev.getBlock().getType() == Material.SAPLING) {
            return;
        }
        if (ev.getPlayer().getGameMode() == GameMode.SURVIVAL) {

            ev.setCancelled(true);
            boolean isPlant = false;

            /////////////////////////////////////////
            // General Harvest - Non Skill Related //
            /////////////////////////////////////////
            // PLANTS FOR HEARTS
            if (ev.getBlock().getType().getId() == 31 || (ev.getBlock().getType() == Material.DOUBLE_PLANT && (ev.getBlock().getData() == 3 || ev.getBlock().getData() == 2))) {
                isPlant = true;
                double healthPct = (ev.getPlayer().getHealth() * 100) / ev.getPlayer().getHealth();
                double heartChance = 5;
                if (healthPct < 25) {
                    heartChance = 25;
                }
                if (SkillFormulas.takePercentageChance(heartChance)) {
                    ev.getBlock().getWorld().dropItemNaturally(ev.getBlock().getLocation(), CustomItem.getItem(RegenHeart.class).generateItem(1));
                }

            }
            final boolean b = isPlant;

            Harvestable harvestable = HarvestCache.getHarvestable(ev.getBlock());
            /// This is a harvestable !

            if (harvestable != null) {

                ////////////////////////
                // checking cooldowns //
                ////////////////////////
                if (Cooldown.isHarvestCooldown(ev.getBlock())) {
                    ev.getPlayer().sendMessage(ChatColor.RED + L.m("This block has already been harvested. Come back later."));
                    Runnable r = new Runnable() {
                        public void run() {
                            ev.getPlayer().sendBlockChange(ev.getBlock().getLocation(), Material.BARRIER, (byte) 0);
                        }
                    };
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, r, 5);
                    return;
                } else if (Cooldown.isPersonalHarvestCooldown(ev.getBlock(), ev.getPlayer())) {
                    ev.getPlayer().sendMessage(ChatColor.RED + L.m("You have to wait to try to harvest the block again !"));
                    return;
                }

                Skills s = NewKom.database.skills.getSkills(ev.getPlayer());

                SkillResult result = SkillFormulas.hasSucess(harvestable.skillsNeeded, ev.getPlayer(), harvestable.minSkill);
                // DISPATCHING HARVEST EVENT FOR CUSTOM HARVEST PROCESSING
                BlockHarvestEvent harvestEvent = new BlockHarvestEvent(ev.getPlayer(), ev.getBlock(), harvestable, s, result);
                Bukkit.getServer().getPluginManager().callEvent(harvestEvent);
                if (!harvestEvent.isDefaultHarvest()) {
                    return;
                }

                ////////////////////////////////////////////////////
                // IF THERE WAS NO EVENT TO PROCESS THAT HARVEST  //
                // WE DO THE DEFAULT PROCESS                      //
                ////////////////////////////////////////////////////
                double expRatio = harvestable.expRatio;

                ////////////////////////////////
                /// SKILL CHANCES TO HARVEST ///
                ////////////////////////////////
                // FAIL
                if (result == SkillResult.ALMOST_FAILED || result == SkillResult.FAIL) {
                    ev.getPlayer().sendMessage(ChatColor.RED + L.m("You failed to harvest the resource..."));
                    Cooldown.addHarvestPersonalCooldown(ev.getPlayer(), ev.getBlock(), harvestable.cooldown * 1000);

                    // SUCCESS
                } else if (result == SkillResult.SUCCESS) {
                  
                    SkillFormulas.levelUpSkill(ev.getPlayer(), harvestable.skillsNeeded, harvestable.minSkill,  expRatio);
                    ev.getPlayer().sendMessage(ChatColor.GREEN + L.m("You harvested the resource."));
                    Runnable r = new Runnable() {
                        public void run() {
                            ev.getPlayer().sendBlockChange(ev.getBlock().getLocation(), Material.BARRIER, (byte) 0);
                        }
                    };
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, r, 5);
                    ev.getPlayer().getInventory().addItem(ev.getBlock().getDrops().toArray(new ItemStack[ev.getBlock().getDrops().size()]));
                    Cooldown.addHarvestGlobalCooldown(ev.getBlock(), harvestable.cooldown * 1000);

                    // EPIC SUCESS
                } else if (result == SkillResult.EPIC_SUCCESS) {
                    SkillFormulas.levelUpSkill(ev.getPlayer(), harvestable.skillsNeeded, harvestable.minSkill, expRatio);
                    SkillFormulas.levelUpSkill(ev.getPlayer(), harvestable.skillsNeeded, harvestable.minSkill, expRatio);
                    Runnable r = new Runnable() {
                        public void run() {
                            ev.getPlayer().sendBlockChange(ev.getBlock().getLocation(), Material.BARRIER, (byte) 0);
                        }
                    };
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, r, 5);
                    ev.getPlayer().sendMessage(ChatColor.GREEN + L.m("You harvested the resource exceptionally !"));
                    ev.getPlayer().getInventory().addItem(ev.getBlock().getDrops().toArray(new ItemStack[ev.getBlock().getDrops().size()]));
                    ev.getPlayer().getInventory().addItem(ev.getBlock().getDrops().toArray(new ItemStack[ev.getBlock().getDrops().size()]));
                    Cooldown.addHarvestGlobalCooldown(ev.getBlock(), harvestable.cooldown * 1000);

                    // EPIC FAIL
                } else if (result == SkillResult.EPIC_FAIL) {
                    ev.getPlayer().sendMessage(ChatColor.RED + EpicFailEffect.getEffect(ev.getPlayer(), harvestable.skillsNeeded));
                    Cooldown.addHarvestPersonalCooldown(ev.getPlayer(), ev.getBlock(), harvestable.cooldown * 1000);
                }

                /////// THIS BLOCK IS NOT A HARVESTABLE
            } else {
                if (isPlant) {
                    Runnable r = new Runnable() {
                        public void run() {
                            ev.getPlayer().sendBlockChange(ev.getBlock().getLocation(), Material.AIR, (byte) 0);
                            if (!b && ev.getBlock().getRelative(BlockFace.UP).getType() == ev.getBlock().getType()) {
                                ev.getPlayer().sendBlockChange(ev.getBlock().getRelative(BlockFace.UP).getLocation(), Material.AIR, (byte) 0);
                            }
                        }
                    };
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, r, 5);
                } else {

                    ev.getPlayer().sendMessage(ChatColor.RED + L.m("You cannot harvest this block !"));
                }
            }

        }
    }

}
