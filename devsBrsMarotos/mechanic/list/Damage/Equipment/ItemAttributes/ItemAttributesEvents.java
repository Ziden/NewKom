/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Damage.Equipment.ItemAttributes;

import devsBrsMarotos.mechanic.Mecanica;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author vntgasl
 */
public class ItemAttributesEvents extends Mecanica {

    @Override
    public String getName() {
        return "Item Attributes";
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void prepareItemCraft(PrepareItemCraftEvent ev) {

        // apllyng default armor
        if (ev.getInventory().getResult() != null) {
            ItemMeta meta = ev.getInventory().getResult().getItemMeta();

            // only apply default armor if the item has no lore !
            if (meta.getLore() == null || meta.getLore().size() == 0) {
                if (ItemDefaultAttributes.isArmor(ev.getInventory().getResult())) {
                    int armorValue = ItemDefaultAttributes.getDefaultArmorValue(ev.getInventory().getResult());
                    if (armorValue != 0) {
                        ItemAttributes.addAttribute(ev.getInventory().getResult(), AttributeEnum.Armor, armorValue);
                    }
                } else {
                    int damage = ItemDefaultAttributes.getDefaultDamage(ev.getInventory().getResult());
                    if(damage != 0)
                         ItemAttributes.addAttribute(ev.getInventory().getResult(), AttributeEnum.Physical_Damage, damage);
                }
            }
        }
    }

}
