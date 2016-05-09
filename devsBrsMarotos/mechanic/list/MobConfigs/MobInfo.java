/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.MobConfigs;

import devsBrsMarotos.util.MobUtils;
import devsBrsMarotos.DB.MobConfigDB;
import devsBrsMarotos.DB.Models.ModelMobConfig;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.util.GeneralUtils;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author User
 */
public class MobInfo {

    public static Inventory showMobs(Player p, int size, String nameiv, List<ModelMobConfig> mobs) {
        Inventory iv = Bukkit.createInventory(p, size, nameiv);
        for (int i = 0; i < mobs.size(); i++) {
            iv.setItem(i,
                    GeneralUtils.createItem(new ItemStack(Material.MONSTER_EGG),
                            "" + mobs.get(i).mob,
                            MobUtils.getIdMob(mobs.get(i).mob),
                            "§6Level: §2" + mobs.get(i).lv + "@"
                            + "§6Percent: §2" + mobs.get(i).percent + "@"
                            + "§6Custom name: §2" + mobs.get(i).cName + "@"
                            + "§4Click for remove!")
            );
        }

        return iv;
    }

}
