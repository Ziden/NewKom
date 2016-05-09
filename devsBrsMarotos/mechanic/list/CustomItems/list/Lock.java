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
public class Lock extends CustomItem {

    public Lock() {
        super(Material.TRIPWIRE_HOOK, L.m("Lock"), L.m("Put inside a chest to lock !"));
    }

    public ItemStack create(int lockLevel) {
        ItemStack lock = this.generateItem(1);
        ItemMeta meta = lock.getItemMeta();
        List<String> lore = meta.getLore();
        lore.add(0, ChatColor.GREEN + "Lock Level:" + lockLevel);
        int lockId = SkillFormulas.rnd.nextInt(10000000);
        lore.add(1, ChatColor.GREEN + "Lock Id:" + lockId);
        meta.setLore(lore);
        lock.setItemMeta(meta);
        return lock;
    }

    public int getLockLevel(ItemStack lock) {
        ItemMeta meta = lock.getItemMeta();
        return Integer.valueOf(meta.getLore().get(0).split(":")[1]);
    }

    public int getId(ItemStack lock) {
        ItemMeta meta = lock.getItemMeta();
        return Integer.valueOf(meta.getLore().get(1).split(":")[1]);
    }
    
      @Override
    public boolean blockInteract() {
        return false;
    }

    @Override
    public void interage(PlayerInteractEvent ev) {
        if (ev.getClickedBlock() != null && ev.getClickedBlock().getType() == Material.ANVIL) {
            if (!GeneralUtils.hasItem(ev.getPlayer().getInventory(), Material.IRON_INGOT, 1, (byte) 0)) {
                ev.getPlayer().sendMessage(ChatColor.RED + L.m("You need a iron ingot to make the key for this lock !"));
                return;
            } else {
                /////// CREATE KEY FOR THE LOCK
                SkillFormulas.SkillResult result = SkillFormulas.hasSucess(SkillEnum.Engineering, ev.getPlayer(), 35);
                if (result == SkillFormulas.SkillResult.FAIL || result == SkillFormulas.SkillResult.EPIC_FAIL) {
                    ev.getPlayer().sendMessage(ChatColor.RED + L.m("You failed to make the key !"));
                    return;
                } else {
                    double skill = NewKom.database.skills.getSkills(ev.getPlayer()).skills.get(SkillEnum.Engineering).getLvl();
                    SkillFormulas.levelUpSkill(ev.getPlayer(), SkillEnum.Engineering, 35, 1);
                }
                Lock lock = (Lock)CustomItem.getItem(Lock.class);
                LockKey lockKey = (LockKey)CustomItem.getItem(LockKey.class);
                
                int lockId = lock.getId(ev.getPlayer().getItemInHand());
                ItemStack key = lockKey.create(lockId);
                ev.getPlayer().getInventory().addItem(key);
                ev.getPlayer().sendMessage(ChatColor.GREEN + L.m("You made the key for this lock !"));
            }
            //////// COPY THE LOCK
        } else if(ev.getClickedBlock() != null && ev.getClickedBlock().getType() == Material.DISPENSER) {
             if (!GeneralUtils.hasItem(ev.getPlayer().getInventory(), Material.IRON_INGOT, 3, (byte) 0)) {
                ev.getPlayer().sendMessage(ChatColor.RED + L.m("You need 3 iron ingot to copy the lock !"));
                return;
            } else {
                 
                int difficulty = this.getLockLevel(ev.getPlayer().getItemInHand());
                
                SkillFormulas.SkillResult result = SkillFormulas.hasSucess(SkillEnum.Engineering, ev.getPlayer(), difficulty);
                if(result==SkillFormulas.SkillResult.FAIL || result==SkillFormulas.SkillResult.EPIC_FAIL) {
                    ev.getPlayer().sendMessage(ChatColor.RED+L.m("You failed to copy the lock !"));
                    return;
                } else {
                    double skill = NewKom.database.skills.getSkills(ev.getPlayer()).skills.get(SkillEnum.Engineering).getLvl();
                    SkillFormulas.levelUpSkill(ev.getPlayer(), SkillEnum.Engineering, difficulty , 1);
                }
                ItemStack newKey = ev.getPlayer().getItemInHand().clone();
                newKey.setItemMeta(ev.getPlayer().getItemInHand().getItemMeta().clone());
                ev.getPlayer().getInventory().addItem(newKey);
                ev.getPlayer().sendMessage(ChatColor.GREEN+L.m("You cloned the lock !"));
             }
        } else {
            ev.getPlayer().sendMessage(ChatColor.GREEN + "Put this lock into a chest to lock it !");
            ev.getPlayer().sendMessage(ChatColor.GREEN + "Right click on a Anvil to make a Key for it before !");
            ev.getPlayer().sendMessage(ChatColor.GREEN + "Right click on a Dispenser to copy this lock !");
        }
    }

    public boolean displayOnItems() {
        return false;
    }

}
