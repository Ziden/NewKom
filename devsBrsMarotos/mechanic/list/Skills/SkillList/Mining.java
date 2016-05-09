/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills.SkillList;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.Effects.EffectManager;
import devsBrsMarotos.mechanic.list.Harvesting.HarvestCache;
import devsBrsMarotos.mechanic.list.Harvesting.HarvestEvents;
import devsBrsMarotos.mechanic.list.Harvesting.Harvestable;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import devsBrsMarotos.mechanic.list.Skills.SkillEvents.BlockHarvestEvent;
import devsBrsMarotos.mechanic.list.Skills.SkillFormulas;
import devsBrsMarotos.mechanic.list.Skills.SkillFormulas.SkillResult;
import devsBrsMarotos.util.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

/**
 *
 * @author User
 *
 */
public class Mining extends Mecanica {

    @EventHandler
    public void harvest(BlockHarvestEvent ev) {
        if (ev.getBlock().getType() == Material.STONE) {
            ev.setDefaultHarvest(false);
            ev.setCancelled(true);
            if (ev.getBlock().getRelative(BlockFace.UP).getType() != Material.AIR) {
                ev.getPlayer().sendMessage(ChatColor.RED + "You must mine on a floor !");
            } else {
                getMiningHarvestable(ev.getBlock(), ev.getPlayer(), ev.getSkill(SkillEnum.Minning));
            }
        }
    }

    public Block getOre(Location l) {
        l.setY(1);
        Block b = null;
        if (l.getBlock().getType() == Material.CHEST) {
            Chest c = (Chest) l.getBlock().getState();
            for (ItemStack ss : c.getBlockInventory().getContents()) {

            }
        }
        return b;
    }

    public static void showOres(Player p, int radius) {
        Location floor = p.getLocation().add(new Vector(0, -1, 0));
        for (int x = floor.getBlockX() - radius; x < floor.getBlockX() + radius; x++) {
            for (int z = floor.getBlockZ() - radius; z < floor.getBlockZ() + radius; z++) {
                Block under = p.getLocation().getWorld().getBlockAt(x, 1, z);
                Harvestable harv = HarvestCache.getHarvestable(under);
                if (harv != null) {
                    p.sendBlockChange(new Location(under.getWorld(), under.getLocation().getBlockX(), floor.getBlockY(), under.getLocation().getBlockZ()), harv.m, (byte) 0);
                }
            }
        }
    }

    public static Material genOre(Block b) {
        Material ore = Material.COBBLESTONE;
        int num = SkillFormulas.rnd.nextInt(1000);
        if (num < 2) {
            ore = Material.DIAMOND_ORE;
        } else if (num < 4) {
            ore = Material.GLOWSTONE;
        } else if (num < 12) {
            ore = Material.MOSSY_COBBLESTONE;
        } else if (num < 15) {
            ore = Material.LAPIS_ORE;
        } else if (num < 25) {
            ore = Material.REDSTONE_ORE;
        } else if (num < 225) {
            ore = Material.EMERALD_ORE;
        } else if (num < 250) {
            ore = Material.GOLD_ORE;
        } else if (num < 300) {
            ore = Material.IRON_ORE;
        } else if (num < 450) {
            ore = Material.GRAVEL;
        } else if (num < 470) {
            ore = Material.COAL;
        }
        b.setType(ore);
        b.getRelative(BlockFace.UP).setType(Material.BEDROCK);
        return ore;
    }

    public static void getMiningHarvestable(final Block b, final Player p, double skill) {
        Block under = b.getWorld().getBlockAt(b.getLocation().getBlockX(), 1, b.getLocation().getBlockZ());
        Material vein = Material.STONE;
        vein = under.getType();
        Harvestable ore = HarvestCache.getHarvestable(under);

        // if this spot wasnt generated, generate it
        if ((under.getType() == Material.STONE || ore == null) && under.getType() != Material.COBBLESTONE) {
            vein = genOre(under);
            ore = HarvestCache.getHarvestable(under);
        }

        if (ore == null && vein != Material.COBBLESTONE) {
            p.sendMessage(ChatColor.RED + L.m("It appears there is nothing here to mine..."));
            return;
        }
        // its a common stone vein, you can only get stones
        if (vein == Material.COBBLESTONE) {
            ItemStack ss = new ItemStack(Material.COBBLESTONE, 1);
            p.sendMessage(ChatColor.GREEN + L.m("You mined some " + ss.getType().name()));
            p.getInventory().addItem(ss);
            Cooldown.addHarvestPersonalCooldown(p, b, 5 * 1000);
            HarvestEvents.sendCrack(p, b.getLocation());
            SkillFormulas.levelUpSkill(p, SkillEnum.Minning, 0, 1);
            return;
        }

        /*
         When you find ore, there is a chance
         that you will get COAL or STONE and wont find the ore !
         */
        if (ore.skillsNeeded == SkillEnum.Minning || vein == Material.COBBLESTONE) {
            int rnd = SkillFormulas.rnd.nextInt(100 - (int) skill);
            if (rnd < 10) {
                vein = ore.m; // the actual ore
            } else if (rnd < 35) {
                vein = Material.COAL;
            } else {
                vein = Material.STONE;
            }
        } else {
            p.sendMessage(ChatColor.RED + L.m("It appears there is nothing here to mine..."));
        }

        double minSkill = ore.minSkill;
        if (vein == Material.STONE || vein == Material.COAL) {
            minSkill = 0;
        }

        ItemStack ss = new ItemStack(vein, 1);

        double expRatio = ore.expRatio;

        // now, lets mine !
        SkillResult result = SkillFormulas.hasSucess(SkillEnum.Minning, p, minSkill);
        if (result == SkillResult.SUCCESS) {
            p.getInventory().addItem(ss);
            p.sendMessage(ChatColor.GREEN + L.m("You mined some ") + ss.getType().name());
            // if i got the hidden ore
            if (vein == ore.m) {
                HarvestEvents.sendCrack(p, b.getLocation());
                SkillFormulas.levelUpSkill(p, SkillEnum.Minning, minSkill, expRatio);
                Cooldown.addHarvestGlobalCooldown(b, ore.cooldown * 1000);
            }
        } else if (result == SkillResult.EPIC_SUCCESS) {
            p.getInventory().addItem(ss);
            p.getInventory().addItem(ss);
            p.sendMessage(ChatColor.GREEN + L.m("You exceptionally mined some ") + ss.getType().name());
            if (vein == ore.m) {
                  Bukkit.getScheduler().scheduleSyncDelayedTask(NewKom.instancia, new Runnable() {

                    @Override
                    public void run() {
                        HarvestEvents.sendCrack(p, b.getLocation());
                    }
                }, 1);
                SkillFormulas.levelUpSkill(p, SkillEnum.Minning, minSkill, expRatio);
                SkillFormulas.levelUpSkill(p, SkillEnum.Minning, minSkill, expRatio);
                Cooldown.addHarvestGlobalCooldown(b, ore.cooldown * 1000);
            }
        } else if (result == SkillResult.FAIL || result == SkillResult.ALMOST_FAILED) {
            p.sendMessage(ChatColor.RED + L.m("You found some % but couldnt extract ", ss.getType().name()));
            if (vein == ore.m) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(NewKom.instancia, new Runnable() {

                    @Override
                    public void run() {
                        HarvestEvents.sendCrack(p, b.getLocation());
                    }
                }, 1);

                Cooldown.addHarvestPersonalCooldown(p, b, ore.cooldown * 1000);
            }
            return;
        } else if (result == SkillResult.EPIC_FAIL) {
            p.sendMessage(ChatColor.RED + L.m("You found some % but couldnt extract ", ss.getType().name()));
            int rnd = SkillFormulas.rnd.nextInt(2);
            if (vein == ore.m) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(NewKom.instancia, new Runnable() {

                    @Override
                    public void run() {
                        HarvestEvents.sendCrack(p, b.getLocation());
                    }
                }, 1);
                Cooldown.addHarvestPersonalCooldown(p, b, ore.cooldown * 1000);
            }
            if (rnd == 0) {
                p.sendMessage(ChatColor.RED + L.m("A snake was on the rocks and bites you !"));
                EffectManager.addEffect(p, PotionEffectType.SLOW, 20 * 3, 0);
            } else if (rnd == 1) {
                p.sendMessage(ChatColor.RED + L.m("You got tired of mining !"));
                EffectManager.addEffect(p, PotionEffectType.SLOW_DIGGING, 20 * 20, 1);
            } else {
                p.sendMessage(ChatColor.RED + L.m("You hurt yourself with the pickaxe !"));
                p.damage(1d);
            }
        }
    }

    @Override
    public String getName() {
        return "Skill Mining";
    }

}
