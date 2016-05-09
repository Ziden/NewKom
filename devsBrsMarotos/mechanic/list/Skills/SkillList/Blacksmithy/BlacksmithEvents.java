/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills.SkillList.Blacksmithy;

import devsBrsMarotos.DB.Models.Skills;
import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.CustomItems.CustomItem;
import devsBrsMarotos.mechanic.list.CustomItems.list.HeatenCoal;
import devsBrsMarotos.mechanic.list.Damage.Equipment.ItemAttributes.AttributeEnum;
import devsBrsMarotos.mechanic.list.Damage.Equipment.ItemAttributes.ItemAttributes;
import devsBrsMarotos.mechanic.list.Damage.Equipment.ItemAttributes.ItemDefaultAttributes;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import devsBrsMarotos.mechanic.list.Skills.SkillEvents.CreateItemEvent;
import devsBrsMarotos.mechanic.list.Skills.SkillFormulas;
import devsBrsMarotos.mechanic.list.Skills.SkillFormulas.SkillResult;
import devsBrsMarotos.util.ItemUtils;
import devsBrsMarotos.util.LocUtils;
import java.util.HashSet;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author vntgasl
 *
 */
public class BlacksmithEvents extends Mecanica {

    @Override
    public String getName() {
        return "Blacksmith Events";
    }

    public static void setDurabilityPC(ItemStack item, int percent) {
        if (percent > 100) {
            percent = 100;
        }
        item.setDurability((short) (item.getType().getMaxDurability() - (percent * item.getType().getMaxDurability()) / 100));
    }

    /// heatening coal //
    @EventHandler
    public void interage(PlayerInteractEvent ev) {
        Action action = ev.getAction();
        if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
            if (ev.getPlayer().getItemInHand() != null && ev.getPlayer().getItemInHand().getType() == Material.COAL) {
                Block fina = ev.getPlayer().getTargetBlock((HashSet<Byte>) null, 4);
                List<Location> locs = LocUtils.trace(ev.getPlayer().getEyeLocation(), fina.getLocation(), 4);
                for (Location l : locs) {
                    if (l.getBlock().getType() == Material.LAVA) {
                        int amt = ev.getPlayer().getItemInHand().getAmount();
                        ev.getPlayer().setItemInHand(CustomItem.getItem(HeatenCoal.class).generateItem(amt));
                        ev.getPlayer().playSound(null, Sound.FIZZ, 1, 1);
                        ev.getPlayer().sendMessage(ChatColor.GREEN + L.m("You heaten the coal"));
                        break;
                    }
                }

            }
        }
    }

    public static boolean useAnvil(Player p) {
        int x = p.getLocation().getBlockX();
        int z = p.getLocation().getBlockZ();
        for (int xx = x - 2; xx < x + 2; xx++) {
            for (int zz = z - 2; zz < z + 2; zz++) {
                if (p.getWorld().getBlockAt(xx, p.getLocation().getBlockY(), zz).getType() == Material.ANVIL) {
                    Block bigorna = p.getWorld().getBlockAt(xx, p.getLocation().getBlockY(), zz);
                    int damage = bigorna.getState().getData().getData();
                    damage += 4;
                    if (damage >= 12) {
                        bigorna.setType(Material.AIR);
                    } else {
                        bigorna.setData((byte) damage);
                        bigorna.getState().getData().setData((byte) damage);
                        bigorna.getState().update();
                    }
                    p.sendMessage(ChatColor.GREEN + L.m("You used your anvil !"));
                    return true;
                }
            }
        }
        return false;
    }

    // fire damage reduction
    @EventHandler
    public void entityDamage(EntityDamageEvent ev) {
        if (ev.getCause() == DamageCause.FIRE || ev.getCause() == DamageCause.FIRE_TICK) {
            if (ev.getEntity().getType() == EntityType.PLAYER) {
                Skills s = NewKom.database.skills.getSkills((Player) ev.getEntity());
                double reduction = 1 - s.get(SkillEnum.Blacksmithing).getLvl() / 5 / 100; // 20% max fire resistance, result in 0.8
                ev.setDamage(ev.getDamage() * reduction);
            }
        }
    }

    // increase damage with spades
    @EventHandler
    public void entityDamageByEnt(EntityDamageByEntityEvent ev) {
        if (ev.getDamager().getType() == EntityType.PLAYER) {
            Player damager = (Player) ev.getDamager();
            if (damager.getItemInHand() != null && damager.getItemInHand().getType().name().contains("_SPADE")) {
                Skills s = NewKom.database.skills.getSkills((Player) ev.getEntity());
                double bonusDamage = s.get(SkillEnum.Blacksmithing).getLvl() / 15;
                ev.setDamage(ev.getDamage() + bonusDamage);
            }
        }
    }

    // main crafting
    @EventHandler
    public void craft(CreateItemEvent ev) {
        if (ev.getCraftable().skillsNeeded != SkillEnum.Blacksmithing) {
            return;
        }
        ev.setIsDefaultCraft(false);
        Player p = ev.getPlayer();

        // need heaten coal to craft blacksmithy things
        if (!ItemUtils.removeCustomItem(ev.getPlayer().getInventory(), CustomItem.getItem(HeatenCoal.class), 1)) {
            ev.getPlayer().sendMessage(ChatColor.RED + L.m("You need a heaten coal to craft this !"));
            ev.getPlayer().sendMessage(ChatColor.RED + L.m("Use a coal on lava to heaten it !"));
            ev.canceled = true;
            return;
        }

        // need a nearby anvil to craft certain items
        if (ev.getResult().getType().name().contains("DIAMOND_") || ev.getResult().getType().name().contains("IRON_") || ev.getResult().getType().name().contains("GOLD_") || ev.getResult().getType().name().contains("CHAIN_")) {
            if (!useAnvil(ev.getPlayer())) {
                ev.getPlayer().sendMessage(ChatColor.RED + L.m("You need a nearby anvil to craft this item !"));
                ev.canceled = true;
                return;
            }
        }

        Skills skills = NewKom.database.skills.getSkills(ev.getPlayer());
        double mySkill = skills.get(ev.getCraftable().skillsNeeded).getLvl();
        double expRatio = ev.getCraftable().expRatio;
        SkillFormulas.SkillResult result = SkillFormulas.hasSucess(ev.getCraftable().skillsNeeded, p, ev.getCraftable().minSkill);
        ///// FAIL AND EPIC FAILED /////
        if (result == SkillResult.EPIC_FAIL || result == SkillResult.FAIL) {
            if (result == SkillResult.EPIC_FAIL) {
                p.setFireTicks(80);
                p.sendMessage(ChatColor.RED + L.m("You combust into fire !"));
            }
            p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 1.2F, 1);
            ev.failed = true;
            p.sendMessage(ChatColor.RED + L.m("You failed to smith the item !"));
            ////// AVERAGE ITEM /////
        } else if (result == SkillResult.ALMOST_FAILED) {
            p.playSound(p.getLocation(), Sound.ANVIL_USE, 1.2F, 1);
            p.sendMessage(ChatColor.RED + L.m("You crafted a medium quality item !"));
            setDurabilityPC(ev.getResult(), 50);
            SkillFormulas.levelUpSkill(p, ev.getCraftable().skillsNeeded, ev.getCraftable().minSkill, expRatio);
            /// NORMAL ITEM //
        } else if (result == SkillResult.SUCCESS) {
            p.playSound(p.getLocation(), Sound.ANVIL_USE, 1.2F, 1);
            p.sendMessage(ChatColor.RED + L.m("You crafted the item !"));
            SkillFormulas.levelUpSkill(p, ev.getCraftable().skillsNeeded, ev.getCraftable().minSkill, expRatio);
            //////////////////////
            // EXCEPTIONAL ITEM //
            //////////////////////
        } else if (result == SkillResult.EPIC_SUCCESS) {
            p.playSound(p.getLocation(), Sound.ANVIL_USE, 1.2F, 1);
            p.sendMessage(ChatColor.RED + L.m("You crafted an excepcional item !"));
            SkillFormulas.levelUpSkill(p, ev.getCraftable().skillsNeeded, ev.getCraftable().minSkill, expRatio);
            ItemMeta meta = ev.getResult().getItemMeta();
            meta.setDisplayName("Exceptional " + NewKom.trad.getItemName(ev.getResult()));
            ev.getResult().setItemMeta(meta);
            if (ItemDefaultAttributes.isArmor(ev.getResult())) {
                ItemAttributes.addAttribute(ev.getResult(), AttributeEnum.Armor, 2);
            } else {
                ItemAttributes.addAttribute(ev.getResult(), AttributeEnum.Physical_Damage, 2);
            }
        }
    }

}
