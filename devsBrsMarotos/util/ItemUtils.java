package devsBrsMarotos.util;

import devsBrsMarotos.mechanic.list.CustomItems.CustomItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 *
 * @author Carlos
 */
public class ItemUtils {

    public static ItemStack getHead(String name) {
        ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta meta = (SkullMeta) is.getItemMeta();
        meta.setOwner(name);
        is.setItemMeta(meta);
        return is;

    }

    public static ItemStack SetItemName(ItemStack i, String name) {
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(name);
        i.setItemMeta(im);
        return i;
    }

    public static String GetItemName(ItemStack i) {
        if (i == null) {
            return null;
        }
        String itemname = "";
        ItemMeta im = i.getItemMeta();
        if (im == null) {
            return null;
        }
        if (!i.hasItemMeta()) {
            itemname = i.getType().name();
        } else if (im.getDisplayName() != null) {
            itemname = im.getDisplayName();
        } else {
            itemname = i.getType().name();
        }
        return itemname;
    }

    public static boolean removeCustomItem(Inventory inv, Material it, int qtd) {
        if (qtd == 0) {
            return true;
        }
        int x = 0;
        int falta = qtd - x;
        for (int y = 0; y < inv.getContents().length; y++) {
            ItemStack itm = inv.getItem(y);
            if (itm != null && itm.getType().equals(it)) {
                if (x >= qtd) {
                    break;
                }
                if (itm.getAmount() > falta) {
                    itm.setAmount(itm.getAmount() - falta);
                    x += qtd;
                } else if (itm.getAmount() < falta) {
                    x += itm.getAmount();
                    inv.setItem(y, null);
                } else if (itm.getAmount() == falta) {
                    x += itm.getAmount();
                    inv.setItem(y, null);

                }

            }
        }
        return x >= qtd;
    }

    public static boolean removeCustomItem(Inventory inv, CustomItem item, int qtd) {
        if (qtd == 0) {
            return true;
        }
        int x = 0;
        int falta = qtd - x;
        for (int y = 0; y < inv.getContents().length; y++) {
            ItemStack itm = inv.getItem(y);
            if (itm != null) {
                CustomItem ci = CustomItem.getCustomItem(itm);
                if (ci != null && ci.getClass().isAssignableFrom(item.getClass())) {
                    if (x >= qtd) {
                        break;
                    }
                    if (itm.getAmount() > falta) {
                        itm.setAmount(itm.getAmount() - falta);
                        x += qtd;
                    } else if (itm.getAmount() < falta) {
                        x += itm.getAmount();
                        inv.setItem(y, null);
                    } else if (itm.getAmount() == falta) {
                        x += itm.getAmount();
                        inv.setItem(y, null);

                    }
                }
            }
        }
        return x >= qtd;
    }

    public static boolean containsInv(Inventory i, Material item) {
        for (ItemStack it : i.getContents()) {
            if (it != null && it.getType().equals(item)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsInv(Inventory i, Material item, int qtd) {
        if (qtd == 0) {
            return true;
        }
        int x = 0;
        for (ItemStack it : i.getContents()) {
            if (it != null && item.equals(it.getType())) {
                x += it.getAmount();
            }
        }
        return x >= qtd;
    }

    public static ItemStack AddLore(ItemStack i, String lore) {
        ItemMeta im = i.getItemMeta();
        List<String> aux = new ArrayList();
        if (im.getLore() == null || im.getLore().size() == 0) {
            aux.add(lore);
            im.setLore(aux);
            i.setItemMeta(im);
            return i;
        } else {
            aux = im.getLore();
            aux.add(lore);
            im.setLore(aux);
            i.setItemMeta(im);
            return i;
        }
    }

    public static List<String> GetLore(ItemStack i) {
        ItemMeta im = i.getItemMeta();
        return im.getLore();
    }

    public static ItemStack SetLore(ItemStack i, List<String> l) {
        ItemMeta im = i.getItemMeta();
        im.setLore(l);
        i.setItemMeta(im);
        return i;
    }

    public static ItemStack ClearLore(ItemStack i) {
        List<String> aux = new ArrayList();
        ItemMeta im = i.getItemMeta();
        im.setLore(aux);
        i.setItemMeta(im);
        return i;
    }

    public static ItemStack addColor(ItemStack item, Color cor) {
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(cor);
        item.setItemMeta(meta);
        return item;
    }

    public static String getItemName(ItemStack is) {
        if (is == null) {
            return null;
        }
        if (is.getItemMeta() == null) {
            return null;
        }
        if (is.getItemMeta().getDisplayName() == null) {
            return null;
        }
        return is.getItemMeta().getDisplayName();
    }

    /*
     MineP    
     */
    public static org.bukkit.inventory.ItemStack CreateStack(Material type) {
        return CreateStack(type.getId(), (byte) 0, 1, (short) 0, null, new String[0], null);
    }

    public static org.bukkit.inventory.ItemStack CreateStack(int id) {
        return CreateStack(id, (byte) 0, 1, (short) 0, null, new String[0], null);
    }

    public static org.bukkit.inventory.ItemStack CreateStack(Material type, int amount) {
        return CreateStack(type.getId(), (byte) 0, amount, (short) 0, null, new String[0], null);
    }

    public static org.bukkit.inventory.ItemStack CreateStack(int id, int amount) {
        return CreateStack(id, (byte) 0, amount, (short) 0, null, new String[0], null);
    }

    public static org.bukkit.inventory.ItemStack CreateStack(Material type, byte data) {
        return CreateStack(type.getId(), data, 1, (short) 0, null, new String[0], null);
    }

    public static org.bukkit.inventory.ItemStack CreateStack(int id, byte data) {
        return CreateStack(id, data, 1, (short) 0, null, new String[0], null);
    }

    public static org.bukkit.inventory.ItemStack CreateStack(Material type, byte data, int amount) {
        return CreateStack(type.getId(), data, amount, (short) 0, null, new String[0], null);
    }

    public static org.bukkit.inventory.ItemStack CreateStack(int id, byte data, int amount) {
        return CreateStack(id, data, amount, (short) 0, null, new String[0], null);
    }

    public static org.bukkit.inventory.ItemStack CreateStack(Material type, byte data, int amount, String name) {
        return CreateStack(type.getId(), data, amount, (short) 0, name, new String[0], null);
    }

    public static org.bukkit.inventory.ItemStack CreateStack(int id, byte data, int amount, String name) {
        return CreateStack(id, data, amount, (short) 0, name, new String[0], null);
    }

    public static org.bukkit.inventory.ItemStack CreateStack(Material type, byte data, int amount, String name, List<String> lore) {
        return CreateStack(type.getId(), data, amount, (short) 0, name, lore, null);
    }

    public static org.bukkit.inventory.ItemStack CreateStack(int id, byte data, int amount, String name, List<String> lore) {
        return CreateStack(id, data, amount, (short) 0, name, lore, null);
    }

    public static org.bukkit.inventory.ItemStack CreateStack(Material type, byte data, int amount, String name, String[] lore) {
        return CreateStack(type.getId(), data, amount, (short) 0, name, ArrayToList(lore), null);
    }

    public static org.bukkit.inventory.ItemStack CreateStack(int id, byte data, int amount, String name, String[] lore) {
        return CreateStack(id, data, amount, (short) 0, name, ArrayToList(lore), null);
    }

    public static org.bukkit.inventory.ItemStack CreateStack(Material type, byte data, int amount, short damage, String name, String[] lore) {
        return CreateStack(type.getId(), data, amount, damage, name, ArrayToList(lore), null);
    }

    public static org.bukkit.inventory.ItemStack CreateStack(int id, byte data, int amount, short damage, String name, String[] lore) {
        return CreateStack(id, data, amount, damage, name, ArrayToList(lore), null);
    }

    public static org.bukkit.inventory.ItemStack CreateStack(Material type, byte data, int amount, short damage, String name, List<String> lore) {
        return CreateStack(type.getId(), data, amount, damage, name, lore, null);
    }

    public static org.bukkit.inventory.ItemStack CreateStack(Material type, byte data, int amount, String name, List<String> lore, String owner) {
        return CreateStack(type.getId(), data, amount, (short) 0, name, lore, owner);
    }

    public static org.bukkit.inventory.ItemStack CreateStack(int id, byte data, int amount, String name, List<String> lore, String owner) {
        return CreateStack(id, data, amount, (short) 0, name, lore, owner);
    }

    public static org.bukkit.inventory.ItemStack CreateStack(Material type, byte data, int amount, String name, String[] lore, String owner) {
        return CreateStack(type.getId(), data, amount, (short) 0, name, ArrayToList(lore), owner);
    }

    public static org.bukkit.inventory.ItemStack CreateStack(int id, byte data, int amount, String name, String[] lore, String owner) {
        return CreateStack(id, data, amount, (short) 0, name, ArrayToList(lore), owner);
    }

    public static org.bukkit.inventory.ItemStack CreateStack(Material type, byte data, int amount, short damage, String name, String[] lore, String owner) {
        return CreateStack(type.getId(), data, amount, damage, name, ArrayToList(lore), owner);
    }

    public static org.bukkit.inventory.ItemStack CreateStack(int id, byte data, int amount, short damage, String name, String[] lore, String owner) {
        return CreateStack(id, data, amount, damage, name, ArrayToList(lore), owner);
    }

    public static org.bukkit.inventory.ItemStack CreateStack(Material type, byte data, int amount, short damage, String name, List<String> lore, String owner) {
        return CreateStack(type.getId(), data, amount, damage, name, lore, owner);
    }

    public static org.bukkit.inventory.ItemStack CreateStack(int id, byte data, int amount, short damage, String name, List<String> lore, String owner) {
        org.bukkit.inventory.ItemStack stack;
        if (data == 0) {
            stack = new org.bukkit.inventory.ItemStack(id, amount, damage);
        } else {
            stack = new org.bukkit.inventory.ItemStack(id, amount, damage, Byte.valueOf(data));
        }
        ItemMeta itemMeta = stack.getItemMeta();
        if (itemMeta == null) {
            return null;
        }
        boolean setMeta = false;
        if (name != null) {
            itemMeta.setDisplayName(name);
            setMeta = true;
        }
        if (lore != null) {
            if (itemMeta.getLore() != null) {
                itemMeta.setLore(CombineLore(itemMeta.getLore(), lore));
            } else {
                itemMeta.setLore(lore);
            }
            setMeta = true;
        }
        if (setMeta) {
            stack.setItemMeta(itemMeta);
        }
        return stack;
    }

    private static List<String> CombineLore(List<String> A, List<String> B) {
        for (String b : B) {
            A.add(b);
        }
        return A;
    }

    public static List<String> ArrayToList(String[] array) {
        if (array.length == 0) {
            return null;
        }
        List list = new ArrayList();
        for (String cur : array) {
            list.add(cur);
        }
        return list;
    }

    public static String GetLoreVar(org.bukkit.inventory.ItemStack stack, String var) {
        if (stack == null) {
            return null;
        }
        ItemMeta meta = stack.getItemMeta();
        if (meta == null) {
            return null;
        }
        if (meta.getLore() == null) {
            return null;
        }
        for (String cur : meta.getLore()) {
            if (cur.contains(var)) {
                int index = var.split(" ").length;
                String[] tokens = cur.split(" ");
                String out = "";
                for (int i = index; i < tokens.length; i++) {
                    out = out + tokens[i] + " ";
                }
                if (out.length() > 0) {
                    out = out.substring(0, out.length() - 1);
                }
                return out;
            }
        }
        return null;
    }

    public static int GetLoreVar(org.bukkit.inventory.ItemStack stack, String var, int empty) {
        if (stack == null) {
            return empty;
        }
        ItemMeta meta = stack.getItemMeta();
        if (meta == null) {
            return 0;
        }
        if (meta.getLore() == null) {
            return 0;
        }
        for (String cur : meta.getLore()) {
            if (cur.contains(var)) {
                String[] tokens = cur.split(" ");
                try {
                    return Integer.parseInt(tokens[(tokens.length - 1)]);
                } catch (Exception e) {
                    return empty;
                }
            }
        }
        return 0;
    }

    public static ItemStack addGlow(ItemStack item) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag()) {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        if (tag == null) {
            tag = nmsStack.getTag();
        }
        NBTTagList ench = new NBTTagList();
        tag.set("ench", ench);
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);
    }

    public static List<String> formatLore(String text) {
        List<String> lore = new ArrayList<String>();
        lore.addAll(Arrays.asList(text.split("@")));
        return lore;
    }

}
