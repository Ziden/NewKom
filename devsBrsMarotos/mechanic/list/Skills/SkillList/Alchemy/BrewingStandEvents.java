/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills.SkillList.Alchemy;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.CustomItems.CustomItem;
import devsBrsMarotos.mechanic.list.CustomItems.list.PotionExtract;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.Skills.SkillList.Alchemy.CustomPotions.CustomPotion;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author User
 */
public class BrewingStandEvents extends Mecanica {

    @Override
    public String getName() {
        return "Brewing Stand";
    }

    @EventHandler
    public void brew(BrewEvent ev) {
        ev.setCancelled(true);

        BrewerInventory inv = ev.getContents();
        ItemStack ingredient = inv.getIngredient();

        for (int x = 0; x < inv.getContents().length; x++) {
            ItemStack brewing = inv.getContents()[x];
            if (brewing == null || brewing.getType() == Material.AIR || brewing.getType() == ev.getContents().getIngredient().getType()) {
                continue;
            }

            PotionExtract extract = (PotionExtract) CustomItem.getCustomItem(brewing);
            CustomPotion result = extract.getPotionFromExtract(brewing);
            ItemStack neededToBrew = result.brewWith();
            if (neededToBrew.getType() != ingredient.getType() || neededToBrew.getData().getData() != ingredient.getData().getData()) {
                inv.setItem(x, new ItemStack(Material.GLASS_BOTTLE, 1));
            } else {
                inv.setItem(x, result.generateItem(1));
            }

        }
        if (ingredient.getAmount() > 1) {
            ingredient.setAmount(ingredient.getAmount() - 1);
        } else {
            ev.getContents().setIngredient(null);
        }
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent ev) {
        if (ev.getInventory().getType() == InventoryType.BREWING && ev.getWhoClicked().getType() == EntityType.PLAYER) {
            
            ///// TO-DO SUPPORT SHIFT-CLICK
            if(ev.isShiftClick())
                ev.setCancelled(true);
            
            Player p = (Player) ev.getWhoClicked();
            BrewerInventory inv = (BrewerInventory) ev.getInventory();
            ItemStack placing = ev.getCursor();
            if (ev.isShiftClick()) {
                placing = ev.getCurrentItem();
            }
            // im placing something in the potion area
            if (placing != null && placing.getType() != Material.AIR && (ev.getRawSlot() == 0 || ev.getRawSlot() == 1 || ev.getRawSlot() == 2)) {
                // will check if its an extract
                CustomItem ci = CustomItem.getCustomItem(placing);
                if (ci == null || !(ci instanceof PotionExtract)) {
                    ev.setCancelled(true);
                    p.sendMessage(ChatColor.RED + L.m("You can only place potion extracts made by a cauldron on the stand !"));
                    return;
                }
            }
        }
    }

}
