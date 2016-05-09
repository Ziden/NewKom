/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.CustomItems.list;

import devsBrsMarotos.mechanic.list.CustomItems.CustomItem;
import devsBrsMarotos.mechanic.list.Skills.SkillList.Alchemy.CustomPotions.CustomPotion;
import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author User
 */
public class PotionExtract extends CustomItem {

    public PotionExtract() {
        super(Material.POTION, "Potion Extract", "Brew on a Brewing Stand");
    }
    
    public ItemStack createPotionExtract(CustomPotion pot) {
        ItemStack extract = this.generateItem(1);
        ItemMeta meta = extract.getItemMeta();
        List<String> lore = new ArrayList<String>(meta.getLore());
        lore.add(0, ChatColor.BLUE+"Extract for:"+pot.name);
        meta.setLore(lore);
        extract.setItemMeta(meta);
        return extract;
    }
    
    public CustomPotion getPotionFromExtract(ItemStack extract) {
        ItemMeta meta = extract.getItemMeta();
        String potionName = meta.getLore().get(0).split(":")[1];
        CustomPotion pot = CustomPotion.getItem(potionName);
        return pot;
    }

    @Override
    public void interage(PlayerInteractEvent ev) {
       
    }
    
    public boolean displayOnItems() {
        return false;
    }
    
}
