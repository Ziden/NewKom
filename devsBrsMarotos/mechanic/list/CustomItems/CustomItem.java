package devsBrsMarotos.mechanic.list.CustomItems;

import devsBrsMarotos.mechanic.list.CustomItems.list.Heart;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author vntgasl
 *
 */
public abstract class CustomItem {

    public static void consome(Player player) {
        if (player.getItemInHand().getAmount() > 1) {
            player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
        } else {
            player.setItemInHand(null);
        }
    }

    public Material m;
    public List<String> lore;
    public String name;

    public CustomItem(Material m, String nome, String descEffect) {
        this.m = m;
        this.lore = new ArrayList<String>();
        this.name = nome;
        this.lore.add(ChatColor.GRAY + "-" + descEffect);
        if (isBound()) {
            lore.add("* Bound *");
        }
        if (isBlessed()) {
            lore.add("* Blessed *");
        }
        this.lore.add(ChatColor.BLACK + ":" + this.name);

    }

    public void specialThreat(ItemStack ss) {

    }
    
    public boolean blockInteract() {
        return true;
    }

    public static void mostra(Player p) {
        Inventory inv = Bukkit.createInventory(p, InventoryType.CHEST, "Custom Items");
        for (CustomItem cu : ItemLoader.customItems.values()) {
            inv.addItem(cu.generateItem(1));
        }
        p.openInventory(inv);
    }

    public abstract void interage(PlayerInteractEvent ev);

    public static CustomItem getCustomItem(ItemStack s) {
        if (s == null) {
            return null;
        }
        ItemMeta m = s.getItemMeta();
        if (m == null || m.getLore() == null || m.getLore().size() == 0) {
            return null;
        }
        String last = m.getLore().get(m.getLore().size() - 1);
        if (last.length() < 3) {
            return null;
        }
        String[] split = last.split(":");
        if (split.length != 2) {
            return null;
        }

        return getItem(split[1]);
    }

    // if /kom items will show
    public boolean displayOnItems() {
        return true;
    }

    // wont loose upon death
    public boolean isBlessed() {
        return false;
    }

    // cannot take off inventory
    public boolean isBound() {
        return false;
    }

    public ItemStack generateItem(int size) {
        ItemStack item = new ItemStack(m, size);

        setItemNameAndLore(item, ChatColor.GOLD + name, lore.toArray(new String[lore.size()]));
        specialThreat(item);
        return item;
    }

    public static CustomItem getItem(Class<? extends CustomItem> item) {
        if (ItemLoader.customItemsClass.containsKey(item)) {
            return ItemLoader.customItemsClass.get(item);
        }
        return null;
    }

    public static CustomItem getItem(String nome) {
        if (ItemLoader.customItems.containsKey(nome)) {
            return ItemLoader.customItems.get(nome);
        }
        return null;
    }

    public static ItemStack setItemNameAndLore(ItemStack item, String name, String... lore) {
        for (int i = 0; i < lore.length; i++) {
            lore[i] = ChatColor.RESET + lore[i];
        }
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(ChatColor.RESET + name);
        im.setLore(Arrays.asList(lore));
        item.setItemMeta(im);
        return item;
    } 
   
    public void onEnable(){
        
        
    }
}
