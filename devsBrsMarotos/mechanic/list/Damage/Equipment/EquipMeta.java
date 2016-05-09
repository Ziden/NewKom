/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Damage.Equipment;

import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.list.Damage.Equipment.ItemAttributes.AttributeEnum;
import java.util.HashMap;
import org.bukkit.entity.Player;

/**
 *
 * @author vntgasl
 */
public class EquipMeta {

    public EquipMeta(HashMap<AttributeEnum, Integer> a) {
        this.attributes = a;
    }

    public HashMap<AttributeEnum, Integer> attributes = new HashMap();

    public static int getAttribute(Player p) {
        return 0;
    }

    public static void subMeta(EquipMeta meta, EquipMeta toSubtract) {
        for (AttributeEnum attr : toSubtract.attributes.keySet()) {
            if (meta.attributes.containsKey(attr)) {
                int actual = meta.attributes.get(attr);
                actual -= toSubtract.attributes.get(attr);
                if (actual > 0) {
                    meta.attributes.put(attr, actual);
                } else {
                    meta.attributes.remove(attr);
                }
            } else {
                NewKom.log.info("[ERROR] TRYNG TO REMOVE ATTRIBUTE FROM NON EXISTANT ATTRIBUTE !!! (Damage.Equipment.EquipMeta.java)");
            }
        }
    }

    public static void addMeta(EquipMeta meta, EquipMeta toAdd) {
        for (AttributeEnum attr : toAdd.attributes.keySet()) {
            if (meta.attributes.containsKey(attr)) {
                int actual = meta.attributes.get(attr);
                actual += toAdd.attributes.get(attr);
                meta.attributes.put(attr, actual);
            } else {
                meta.attributes.put(attr, toAdd.attributes.get(attr));
            }
        }
    }

}
