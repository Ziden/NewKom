/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Skills.SkillList;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.CustomItems.CustomItem;
import devsBrsMarotos.mechanic.list.CustomItems.list.Lock;
import devsBrsMarotos.mechanic.list.CustomItems.list.LockKey;
import devsBrsMarotos.mechanic.list.CustomItems.list.LockPick;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.mechanic.list.Skills.SkillEnum;
import devsBrsMarotos.mechanic.list.Skills.SkillEvents.CreateItemEvent;
import devsBrsMarotos.mechanic.list.Skills.SkillFormulas;
import devsBrsMarotos.mechanic.list.Skills.SkillFormulas.SkillResult;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author vntgasl
 *
 */
public class LockpickingAndLocks extends Mecanica {

    @Override
    public String getName() {
        return "LockPicking";
    }
    
    @EventHandler
    public void crafta(CreateItemEvent ev) {
        if(ev.getResult()!=null) {
            CustomItem item = CustomItem.getCustomItem(ev.getResult());
            if(item!=null && item instanceof Lock) {
                double mySkill = NewKom.database.skills.getSkills(ev.getPlayer()).get(SkillEnum.Engineering).getLvl();
                int lockLevel = (int)(mySkill/2) + SkillFormulas.rnd.nextInt((int)(mySkill/2));
                ev.setResult(((Lock)CustomItem.getItem(Lock.class)).create(lockLevel));
                ev.getPlayer().sendMessage(ChatColor.GREEN+L.m("You crafted a lock with level of ")+lockLevel);
            }
        }
    }
    
    @EventHandler
    public void interact(PlayerInteractEvent ev) {
        if (ev.getClickedBlock() != null && ev.getClickedBlock().getType() == Material.CHEST) {
            Chest c = (Chest) ev.getClickedBlock().getState();
            boolean locked = false;
            // looking if the chest has a lock
            for (ItemStack ss : c.getBlockInventory().getContents()) {
                if (ss != null) {
                    if (ss.getType() == Material.TRIPWIRE_HOOK) {
                        CustomItem item = CustomItem.getCustomItem(ss);
                        // it has a lock !
                        if (item != null && item instanceof Lock) {
                            Lock lock = (Lock) CustomItem.getItem(Lock.class);
                            int lockLevel = lock.getLockLevel(ss);
                            int lockId = lock.getId(ss);
                            // does the player have the key in hand ?
                            if (ev.getPlayer().getItemInHand() != null && ev.getPlayer().getItemInHand().getType() == Material.TRIPWIRE_HOOK) {
                                CustomItem key = CustomItem.getCustomItem(ev.getPlayer().getItemInHand());
                                // hes with a key in hand ?
                                if (key != null && key instanceof LockKey) {
                                    int keyId = LockKey.getId(ev.getPlayer().getItemInHand());
                                    // is it the right key ?
                                    if (keyId != lockId) {
                                        ev.setCancelled(true);
                                        ev.getPlayer().sendMessage(ChatColor.RED + L.m("This key wont open this chest !"));
                                        return;
                                    }else {
                                            // OK
                                            ev.getPlayer().sendMessage(ChatColor.GREEN+L.m("You used yor key to open the chest"));
                                        }
                                }
                                // he has no key, checking if he has a lockpick
                            } else if (ev.getPlayer().getItemInHand() != null && ev.getPlayer().getItemInHand().getType() == Material.STICK) {
                                CustomItem lockpick = CustomItem.getCustomItem(ev.getPlayer().getItemInHand());
                                // is it a lockpick ?
                                if (lockpick != null && lockpick instanceof LockPick) {
                                    CustomItem.consome(ev.getPlayer());
                                    SkillResult result = SkillFormulas.hasSucess(SkillEnum.Lockpicking, ev.getPlayer(), lockLevel);
                                    if (result == SkillResult.SUCCESS || result == SkillResult.EPIC_SUCCESS) {
                                        ev.getPlayer().sendMessage(ChatColor.GREEN + L.m("You unlocked the chest !"));
                                        List<ItemStack> stolen = new ArrayList<ItemStack>();
                                        // listing the stolen items
                                        for (ItemStack inchest : c.getBlockInventory().getContents()) {
                                            if (inchest != null && inchest.getType() != Material.TRIPWIRE_HOOK) {
                                                stolen.add(ss);
                                            }
                                        }
                                        // giving the player the stolen items and removing from chest
                                        for (ItemStack steal : stolen) {
                                            if (ev.getPlayer().getInventory().firstEmpty() == -1) {
                                                ev.getPlayer().getWorld().dropItemNaturally(c.getLocation(), steal);
                                            } else {
                                                ev.getPlayer().getInventory().addItem(steal);
                                            }
                                            c.getBlockInventory().remove(steal);
                                        }
                                        // adding a stolen note from the chest
                                        ItemStack stealNote = new ItemStack(Material.PAPER, 1);
                                        ItemMeta m = stealNote.getItemMeta();
                                        m.setDisplayName(ChatColor.RED + "Stolen by " + ev.getPlayer().getName());
                                        stealNote.setItemMeta(m);
                                        c.getBlockInventory().addItem(stealNote);
                                    } else {
                                        ev.getPlayer().sendMessage(ChatColor.RED + L.m("You failed to crack the lock !"));
                                        ev.setCancelled(true);
                                    }
                                }
                            } else {
                                ev.getPlayer().sendMessage(ChatColor.RED + L.m("This chest is locked !"));
                                ev.setCancelled(true);
                            }
                            break;
                        }
                    }
                }
            }
        } else {
            if (ev.getClickedBlock() != null && ev.getClickedBlock().getType() == Material.WOOD_DOOR) {
                if (ev.getClickedBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN).getType() != Material.CHEST) {
                    return;
                }
                Chest c = (Chest) ev.getClickedBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN).getState();
                boolean locked = false;
                // looking if the chest has a lock
                for (ItemStack ss : c.getBlockInventory().getContents()) {
                    if (ss != null) {
                        if (ss.getType() == Material.TRIPWIRE_HOOK) {
                            CustomItem item = CustomItem.getCustomItem(ss);
                            // it has a lock !
                            if (item != null && item instanceof Lock) {
                                Lock lock = (Lock) CustomItem.getItem(Lock.class);
                                int lockLevel = lock.getLockLevel(ss);
                                int lockId = lock.getId(ss);
                                // does the player have the key in hand ?
                                if (ev.getPlayer().getItemInHand() != null && ev.getPlayer().getItemInHand().getType() == Material.TRIPWIRE_HOOK) {
                                    CustomItem key = CustomItem.getCustomItem(ev.getPlayer().getItemInHand());
                                    // hes with a key in hand ?
                                    if (key != null && key instanceof LockKey) {
                                        int keyId = LockKey.getId(ev.getPlayer().getItemInHand());
                                        // is it the right key ?
                                        if (keyId != lockId) {
                                            ev.setCancelled(true);
                                            ev.getPlayer().sendMessage(ChatColor.RED + L.m("This key wont open this door !"));
                                            return;
                                        } else {
                                            // OK
                                            ev.getPlayer().sendMessage(ChatColor.GREEN+L.m("You used yor key to open the door"));
                                        }
                                    }
                                    // he has no key, checking if he has a lockpick
                                } else if (ev.getPlayer().getItemInHand() != null && ev.getPlayer().getItemInHand().getType() == Material.STICK) {
                                    CustomItem lockpick = CustomItem.getCustomItem(ev.getPlayer().getItemInHand());
                                    // is it a lockpick ?
                                    if (lockpick != null && lockpick instanceof LockPick) {
                                        // consume item in hand by 1
                                        CustomItem.consome(ev.getPlayer());
                                        SkillResult result = SkillFormulas.hasSucess(SkillEnum.Lockpicking, ev.getPlayer(), lockLevel);
                                        if (result == SkillResult.SUCCESS || result == SkillResult.EPIC_SUCCESS) {
                                            ev.getPlayer().sendMessage(ChatColor.GREEN + L.m("You unlocked the chest !"));
                                            List<ItemStack> stolen = new ArrayList<ItemStack>();
                                            // listing the stolen items
                                            for (ItemStack inchest : c.getBlockInventory().getContents()) {
                                                if (inchest != null && inchest.getType() != Material.TRIPWIRE_HOOK) {
                                                    stolen.add(ss);
                                                }
                                            }
                                            // giving the player the stolen items and removing from chest
                                            for (ItemStack steal : stolen) {
                                                if (ev.getPlayer().getInventory().firstEmpty() == -1) {
                                                    ev.getPlayer().getWorld().dropItemNaturally(c.getLocation(), steal);
                                                } else {
                                                    ev.getPlayer().getInventory().addItem(steal);
                                                }
                                                c.getBlockInventory().remove(steal);
                                            }
                                            // adding a stolen note from the chest
                                            ItemStack stealNote = new ItemStack(Material.PAPER, 1);
                                            ItemMeta m = stealNote.getItemMeta();
                                            m.setDisplayName(ChatColor.RED + "Stolen by " + ev.getPlayer().getName());
                                            stealNote.setItemMeta(m);
                                            c.getBlockInventory().addItem(stealNote);
                                            ev.setCancelled(true);
                                        } else {
                                            ev.getPlayer().sendMessage(ChatColor.RED + L.m("You failed to crack the lock !"));
                                            ev.setCancelled(true);
                                        }
                                    }
                                } else {
                                    ev.getPlayer().sendMessage(ChatColor.RED + L.m("This chest is locked !"));
                                    ev.setCancelled(true);
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

}
