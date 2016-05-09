package devsBrsMarotos.mechanic.list.Skills.SkillList.Lumberjacking;

import devsBrsMarotos.util.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Carlos
 */
public enum WoodType {

    NORMAL(Material.LOG, (byte) 0, ChatColor.YELLOW, "Common"),
    OAK(Material.LOG, (byte) 3, ChatColor.GRAY, "Oak"),
    WILLOW(Material.LOG, (byte) 1, ChatColor.RED, "Willow"),
    MAPPLE(Material.LOG_2, (byte) 0, ChatColor.GOLD, "Mapple"),
    EUCALIPTUS(Material.LOG, (byte) 2, ChatColor.WHITE, "Eucalyptus"),
    YEW(Material.LOG_2, (byte) 1, ChatColor.BLUE, "Yew");

    Material m;
    byte data;
    ChatColor ch;
    String name;

    private WoodType(Material m, byte data, ChatColor ch, String name) {
        this.m = m;
        this.data = data;
        this.ch = ch;
        this.name = name;
    }

    public ItemStack gera(int qtd) {
        ItemStack i = new ItemStack(m, qtd, data);
        ItemUtils.SetItemName(i, ch + name + " Wood");
        return i;
    }

}
