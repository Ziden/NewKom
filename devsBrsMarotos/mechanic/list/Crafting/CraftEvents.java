/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Crafting;

import devsBrsMarotos.DB.Models.Skills;
import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import devsBrsMarotos.mechanic.list.Skills.SkillEvents.CreateItemEvent;
import devsBrsMarotos.mechanic.list.Skills.SkillFormulas;
import devsBrsMarotos.mechanic.list.Skills.SkillFormulas.SkillResult;
import java.util.HashSet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author User
 */
public class CraftEvents extends Mecanica {

    @Override
    public String getName() {
        return "Crafting Events";
    }

    @EventHandler
    public void brew(BrewEvent ev) {

    }

    @EventHandler
    public void takePotionOut(InventoryClickEvent ev) {

    }

    @EventHandler
    public void craft(CraftItemEvent ev) {

      

        Craftable craftable = CraftCache.getCraftable(ev.getCurrentItem().getType());

        if (craftable == null) {
            return;
        }

        Player p = (Player) ev.getWhoClicked();

        ///////////
        // TO-DO //
        ///////////
        // MAKE IT WORK WITH SHIFT+CLICK !!!!!!!!
        if (ev.isShiftClick()) {
            ev.setCancelled(true);
            p.updateInventory();
            return;
        }
        
        CraftingInventory inventory = ev.getInventory();
        ItemStack contents[] = inventory.getContents();

        CreateItemEvent event = new CreateItemEvent(p, inventory, ev.getCurrentItem(), ev.getCursor(), craftable);
        Bukkit.getServer().getPluginManager().callEvent(event);

        if (!event.isIsDefaultCraft()) {
            ev.setCurrentItem(event.getResult());
            if(event.canceled) {
                ev.setCancelled(true);
                p.updateInventory();
                return;
            }
            
            if (event.failed) {
                // spending resources
                for (int i = 1; i < contents.length; i++) {
                    Material item = contents[i].getType();
                    if (!item.equals(Material.AIR)) {
                        int amount;
                        if ((amount = contents[i].getAmount()) > 1) {
                            contents[i].setAmount(amount - 1);
                        } else {
                            inventory.setItem(i, new ItemStack(Material.AIR));
                        }
                    }
                }
            }
            return;
        }

        //////////////////////
        // DEFAULT CRAFTING //
        //////////////////////
        Skills skills = NewKom.database.skills.getSkills(p);
        double mySkill = skills.get(craftable.skillsNeeded).getLvl();
        double expRatio = craftable.expRatio;
        int amt = ev.getCurrentItem().getAmount();
        int finalAmt = 0;
        for (int x = 0; x < amt; x++) {
            SkillResult result = SkillFormulas.hasSucess(craftable.skillsNeeded, p, craftable.minSkill);
            if (result != SkillResult.ALMOST_FAILED && result != SkillResult.FAIL && result != SkillResult.EPIC_FAIL) {
                finalAmt += 1;
            }
        }

        if (finalAmt > 0) {
            p.sendMessage(ChatColor.GREEN + L.m("You crafted ") + finalAmt + " " + ev.getCurrentItem().getType().name());
            ev.getCurrentItem().setAmount(finalAmt);
            SkillFormulas.levelUpSkill(p, craftable.skillsNeeded, craftable.minSkill, expRatio);
            p.playSound(p.getLocation(), Sound.ANVIL_USE, 1.2F, 1);
        } else {
            ev.setCancelled(true);
            p.sendMessage(ChatColor.RED + L.m("You failed to craft the item !"));
            p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 1.2F, 1);
            for (int i = 1; i < contents.length; i++) {
                Material item = contents[i].getType();
                if (!item.equals(Material.AIR)) {
                    int amount;
                    if ((amount = contents[i].getAmount()) > 1) {
                        contents[i].setAmount(amount - 1);
                    } else {
                        inventory.setItem(i, new ItemStack(Material.AIR));
                    }
                }
            }
        }
    }

}
