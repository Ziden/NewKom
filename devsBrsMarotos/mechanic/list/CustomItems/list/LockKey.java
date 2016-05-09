/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.CustomItems.list;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.list.CustomItems.CustomItem;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import devsBrsMarotos.mechanic.list.Skills.SkillFormulas;
import devsBrsMarotos.mechanic.list.Skills.SkillFormulas.SkillResult;
import devsBrsMarotos.util.GeneralUtils;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 *
 * @author User
 *
 */
public class LockKey extends CustomItem {

    public LockKey() {
        super(Material.TRIPWIRE_HOOK, L.m("Key"), L.m("Open up locks !"));
    }

    public ItemStack create(int id) {
        ItemStack lock = this.generateItem(1);
        ItemMeta meta = lock.getItemMeta();
        List<String> lore = meta.getLore();
        int lockId = SkillFormulas.rnd.nextInt(10000000);
        if(id!=-1) {
            lockId = id;
        }
        lore.add(0, ChatColor.GREEN + "Lock Id:" + lockId);
        meta.setLore(lore);
        lock.setItemMeta(meta);
        return lock;
    }

    public static int getId(ItemStack lock) {
        ItemMeta meta = lock.getItemMeta();
        return Integer.valueOf(meta.getLore().get(0).split(":")[1]);
    }
    
   @Override
    public boolean blockInteract() {
        return false;
    }

    @Override
    public void interage(PlayerInteractEvent ev) {
        if (ev.getClickedBlock() != null && ev.getClickedBlock().getType() == Material.ANVIL) {
            if (!GeneralUtils.hasItem(ev.getPlayer().getInventory(), Material.IRON_INGOT, 1, (byte) 0)) {
                ev.getPlayer().sendMessage(ChatColor.RED + L.m("You need a iron ingot to copy the key !"));
                return;
            } else {
                
                SkillResult result = SkillFormulas.hasSucess(SkillEnum.Engineering, ev.getPlayer(), 50);
                if(result==SkillResult.FAIL || result==SkillResult.EPIC_FAIL) {
                    ev.getPlayer().sendMessage(ChatColor.RED+L.m("You failed to copy the key !"));
                    return;
                } else {
                    SkillFormulas.levelUpSkill(ev.getPlayer(), SkillEnum.Engineering, 50 , 1);
                }
                ItemStack newKey = ev.getPlayer().getItemInHand().clone();
                newKey.setItemMeta(ev.getPlayer().getItemInHand().getItemMeta().clone());
                ev.getPlayer().getInventory().addItem(newKey);
                ev.getPlayer().sendMessage(ChatColor.GREEN+L.m("You cloned the key !"));
            }
        } else 
            ev.getPlayer().sendMessage(ChatColor.GREEN + "Right click on a Anvil to copy this key !");
    }

    public boolean displayOnItems() {
        return false;
    }

}
