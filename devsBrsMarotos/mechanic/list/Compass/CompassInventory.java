package devsBrsMarotos.mechanic.list.Compass;

import devsBrsMarotos.DB.Cache;
import devsBrsMarotos.NewKom;
import devsBrsMarotos.mechanic.Mecanica;
import devsBrsMarotos.mechanic.list.Lang.L;
import devsBrsMarotos.util.ItemUtils;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Carlos
 */
public class CompassInventory extends Mecanica {

    @Override
    public String getName() {
        return "Compass Inventory";
    }

    public static Cache<Inventory> invs = new Cache<Inventory>();
    public static Cache<CompassLocation> locs = new Cache<CompassLocation>();

    public static void open(Player p) {
        if (invs.hasCache(p.getUniqueId())) {
            p.openInventory(invs.getCached(p.getUniqueId()));
            return;
        }
        Inventory i = Bukkit.createInventory(null, 54, "§lCompass");
        enche(i, p);
        invs.set(p.getUniqueId(), i);
        p.openInventory(i);
    }

    public static void enche(Inventory i, Player p) {
        i.clear();
        List<CompassLocation> list = NewKom.database.compass.getLocations(p);
        for (CompassLocation cl : list) {
            i.addItem(createItem(cl, p));
        }
    }

    public static ItemStack createItem(CompassLocation loc, Player p) {
        ItemStack it = new ItemStack(Material.COMPASS);
        if (locs.hasCache(p.getUniqueId())) {
            if (locs.getCached(p.getUniqueId()).nome.equalsIgnoreCase(loc.nome)) {
                ItemUtils.SetItemName(it, "§c>> §b§l" + loc.nome);
                ItemUtils.AddLore(it, "§e§lSELECTED");
                return it;
            }
        }
        ItemUtils.SetItemName(it, "§c>> §a§l" + loc.nome);
        return it;
    }

    @EventHandler
    public void click(InventoryClickEvent ev) {
        if (ev.getClickedInventory() != null && ev.getClickedInventory().getTitle() != null && ev.getClickedInventory().getTitle().equalsIgnoreCase("§lcompass")) {
            ev.setCancelled(true);
            if (ev.getCurrentItem() != null && ev.getCurrentItem().getType() == Material.COMPASS) {
                if (ev.getCurrentItem().hasItemMeta() && ev.getCurrentItem().getItemMeta().hasDisplayName()) {
                    String nome = ChatColor.stripColor(ev.getCurrentItem().getItemMeta().getDisplayName());
                    if (nome.startsWith(">>")) {
                        nome = nome.split(">>")[1].trim();
                        if (ev.getCurrentItem().getItemMeta().getLore() != null && ev.getCurrentItem().getItemMeta().getLore().contains("§e§lSELECTED")) {
                            ev.getWhoClicked().sendMessage(NewKom.alertTag + L.m("Now your compass is pointing to spawn!"));
                            ((Player) ev.getWhoClicked()).setCompassTarget(ev.getWhoClicked().getWorld().getSpawnLocation());
                            locs.remove(ev.getWhoClicked().getUniqueId());
                            return;
                        }
                        for (CompassLocation cp : NewKom.database.compass.getLocations((Player) ev.getWhoClicked())) {
                            if (cp.nome.equalsIgnoreCase(nome)) {
                                ((Player) ev.getWhoClicked()).setCompassTarget(cp.l);
                                locs.set(ev.getWhoClicked().getUniqueId(), cp);
                                ev.getWhoClicked().sendMessage(NewKom.alertTag + L.m("Now your compass is pointing to " + cp.nome + "!"));
                                ev.getWhoClicked().closeInventory();
                                enche(invs.getCached(ev.getWhoClicked().getUniqueId()), (Player) ev.getWhoClicked());
                                break;
                            }
                        }
                    }
                }
            }

        }
    }

}
