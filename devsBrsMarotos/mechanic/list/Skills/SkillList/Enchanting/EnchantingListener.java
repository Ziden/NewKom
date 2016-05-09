/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills.SkillList.Enchanting;

import devsBrsMarotos.DB.Models.EnchantmentTable;
import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.CustomItems.CustomItem;
import devsBrsMarotos.mechanic.list.CustomItems.list.EnchantmentScroll;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import devsBrsMarotos.mechanic.list.Skills.SkillEvents.PlayerEnchantEvent;
import devsBrsMarotos.mechanic.list.Skills.SkillFormulas;
import devsBrsMarotos.util.ItemUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Carlos
 */
public class EnchantingListener extends Mecanica {

    @Override
    public String getName() {
        return "EnchantingListener";
    }

    public static List<Enchantment> defaults = Arrays.asList(Enchantment.LURE, Enchantment.ARROW_KNOCKBACK, Enchantment.DURABILITY, Enchantment.WATER_WORKER, Enchantment.PROTECTION_FALL, Enchantment.PROTECTION_PROJECTILE, Enchantment.DAMAGE_UNDEAD);

    @EventHandler
    public void prepare(EnchantItemEvent ev) {
        boolean b = false;
        int custo = ev.whichButton() + 1;
        if (!ev.getEnchanter().getInventory().contains(Material.GLOWSTONE, custo)) {
            ev.getEnchanter().sendMessage(NewKom.errorTag + L.m("You need " + custo + " glowstone(s) to enchant!"));
            ev.setCancelled(true);
            ev.getEnchantsToAdd().clear();
            return;
        }
        ItemUtils.removeCustomItem(ev.getEnchanter().getInventory(), Material.GLOWSTONE, custo);
        for (Enchantment enc : new ArrayList<Enchantment>(ev.getEnchantsToAdd().keySet())) {
            EnchantmentTable et = NewKom.database.ench.getEnchantment(ev.getEnchantBlock());
            Enchantment toenchant = enc;
            int lvltoench = ev.getEnchantsToAdd().get(enc);
            if (!et.enchants.containsKey(enc) || !et.enchants.get(enc)) {
                System.out.println("PASSEI 1");
                List<Enchantment> ench = new ArrayList(et.enchants.keySet());
                Enchantment def = ench.get(new Random().nextInt(ench.size()));

                while (!def.canEnchantItem(ev.getItem())) {
                    def = ench.get(new Random().nextInt(ench.size()));
                }

                int lvl = ev.getEnchantsToAdd().get(enc);
                if (lvl > enc.getMaxLevel()) {
                    lvl = enc.getMaxLevel();
                }
                toenchant = def;
                lvltoench = lvl;

            }

            int xp = ev.getExpLevelCost();
            int diff = getDiff(toenchant, xp);
            SkillFormulas.SkillResult result = SkillFormulas.hasSucess(SkillEnum.Enchanting, ev.getEnchanter(), diff);
            if (result != SkillFormulas.SkillResult.EPIC_SUCCESS && result != SkillFormulas.SkillResult.SUCCESS) {
                ((EnchantingInventory) ev.getView().getTopInventory()).setItem(new ItemStack(Material.SULPHUR));
                ev.getEnchanter().sendMessage(NewKom.errorTag + L.m("You failed to enchant this item!"));
                ev.setCancelled(true);
                ev.getEnchantsToAdd().clear();
                return;

            }
            SkillFormulas.levelUpSkill(ev.getEnchanter(), SkillEnum.Enchanting, getDiff(toenchant, xp), 1);

            ev.getEnchantsToAdd().clear();
            ev.getEnchantsToAdd().put(toenchant, lvltoench);

            ev.getEnchanter().giveExpLevels(custo);
            if (result == SkillFormulas.SkillResult.EPIC_SUCCESS) {
                Enchantment rand = getRandom(ev.getItem(), ev.getEnchantsToAdd().keySet(), et);
                if (rand != null) {
                    ev.getEnchanter().sendMessage(NewKom.successTag + L.m("You found a bonus and gained another enchantment."));
                    ev.getEnchantsToAdd().put(rand, getRandom(ev.getEnchanter(), rand));
                }
            }
            Bukkit.getPluginManager().callEvent(new PlayerEnchantEvent(ev.getEnchanter(), ev.getItem(), ev.getEnchantsToAdd().keySet(), xp, lvltoench, ev.whichButton()));
            break;
        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void breakblock(BlockBreakEvent ev) {
        if (ev.getBlock().getType() == Material.ENCHANTMENT_TABLE && !ev.isCancelled()) {
            EnchantmentTable tb = NewKom.database.ench.getEnchantment(ev.getBlock());
            for (Enchantment ench : tb.enchants.keySet()) {
                if (!defaults.contains(ench)) {
                    ev.getBlock().getWorld().dropItemNaturally(ev.getBlock().getLocation(), EnchantmentScroll.create(ench));
                }
            }
            NewKom.database.ench.delete(ev.getBlock());
        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void interact(PlayerInteractEvent ev) {
        if (!ev.isCancelled()) {
            if (ev.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (ev.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE) {
                    if (ev.getPlayer().isSneaking()) {
                        ev.setCancelled(true);
                        Enchantment ench = EnchantmentScroll.getFromScroll(ev.getPlayer().getItemInHand());
                        if (ench != null) {
                            EnchantmentTable tb = NewKom.database.ench.getEnchantment(ev.getClickedBlock());
                            if (tb.enchants.containsKey(ench)) {
                                ev.getPlayer().sendMessage(NewKom.errorTag + L.m("The enchantment table already has this enchantment."));
                                return;
                            }
                            tb.enchants.put(ench, true);
                            NewKom.database.ench.update(ev.getClickedBlock(), tb);
                            CustomItem.consome(ev.getPlayer());
                            ev.getPlayer().playSound(ev.getPlayer().getLocation(), Sound.LEVEL_UP, 1, 1);
                            ev.getPlayer().sendMessage(NewKom.successTag + L.m("You added the enchantment!"));
                            if (SelectEnchantmentsMenu.invs.containsKey(ev.getClickedBlock())) {
                                SelectEnchantmentsMenu.fill(SelectEnchantmentsMenu.invs.get(ev.getClickedBlock()), ev.getClickedBlock());
                            }
                            return;
                        }
                        ev.getPlayer().openInventory(SelectEnchantmentsMenu.getInventory(ev.getClickedBlock()));
                    } else {
                        ev.getPlayer().sendMessage(NewKom.alertTag + L.m("You can modify the enchantments clicking with shift!"));
                    }
                }
            }
        }

    }

    public int getDiff(Enchantment ench, int lvl) {

        double x = lvl * 2.5;
        if (ench == Enchantment.DAMAGE_ALL || ench == Enchantment.PROTECTION_ENVIRONMENTAL || ench == Enchantment.ARROW_INFINITE) {
            x += 20;
        }
        return (int) x;
    }

    public static Enchantment getRandom(ItemStack it, Set<Enchantment> enchs, EnchantmentTable et) {
        for (Enchantment ench : et.enchants.keySet()) {
            if (!et.enchants.get(ench)) {
                continue;
            }
            if (it.containsEnchantment(ench)) {
                continue;
            }
            if (!ench.canEnchantItem(it)) {
                continue;
            }
            if (enchs.contains(ench)) {
                continue;
            }
            return ench;
        }
        return null;
    }

    private Integer getRandom(Player enchanter, Enchantment ench) {
        double d = NewKom.database.skills.getSkills(enchanter).skills.get(SkillEnum.Enchanting).getLvl();
        int max = ench.getMaxLevel();
        if (max == 1) {
            return 1;
        }
        if (d <= 10) {
            return 1;
        }
        if (d <= 40) {
            return 2;
        }
        if (d <= 60 && max >= 3) {
            return 3;
        }

        if (d <= 80 && max >= 4) {
            return 4;
        }
        return 1;

    }
}
