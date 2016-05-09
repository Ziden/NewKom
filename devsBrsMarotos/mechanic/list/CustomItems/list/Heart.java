/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.CustomItems.list;

import devsBrsMarotos.mechanic.list.CustomItems.CustomItem;
import devsBrsMarotos.mechanic.list.Lang.L;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 *
 * @author User
 */
public class Heart extends CustomItem {

    public Heart() {
        super(Material.SKULL_ITEM, L.m("Extra Life"), L.m("Increase your Life !"));
    }

    @Override
    public void specialThreat(ItemStack ss) {
        ss.getData().setData((byte)3);
        ss.setDurability((short)3);
        SkullMeta meta = (SkullMeta) ss.getItemMeta();
        meta.setOwner("Mauze_");
        ss.setItemMeta(meta);
    }

    @Override
    public void interage(PlayerInteractEvent ev) {

    }

}
