/*

 ╭╮╭━╮╱╱╭━╮╭━╮
 ┃┃┃╭╯╱╱┃┃╰╯┃┃
 ┃╰╯╯╭━━┫╭╮╭╮┃
 ┃╭╮┃┃╭╮┃┃┃┃┃┃
 ┃┃┃╰┫╰╯┃┃┃┃┃┃
 ╰╯╰━┻━━┻╯╰╯╰╯

 Desenvolvedor: ZidenVentania
 Colaboradores: NeT32, Gabripj
 Patrocionio: MinecraftMania

 */
package devsBrsMarotos.mechanic.list.Dungeon;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BookPortal {

    public static ItemStack criaLivroPortal(Player p) {
        ItemStack livro = new ItemStack(Material.BOOK_AND_QUILL, 1);
        BookUtil.setTitle(livro, "TP");
        BookUtil.setAuthor(livro, "Portal Lord");
        String[] page = {
            p.getWorld().getName(),
            "" + p.getLocation().getBlockX(),
            "" + p.getLocation().getBlockY(),
            "" + p.getLocation().getBlockZ(),
            "" + p.getLocation().getPitch(),
            "" + p.getLocation().getYaw()

        };
        BookUtil.setPages(livro, Arrays.asList(page));
        return livro;
    }

    public static Location getLocationFromBook(ItemStack livro) {
        Location l = null;
        List<String> pages = BookUtil.getPages(livro);
        if (pages == null) {
            return null;
        }
        try {
            String mundo = pages.get(0);
            double x = Double.valueOf(pages.get(1))+0.5;
            double y = Double.valueOf(pages.get(2));
            double z = Double.valueOf(pages.get(3))+0.5;
            l = new Location(Bukkit.getWorld(mundo),x,y,z,0,0);
            if(pages.size()>4) {
                int pitch = Integer.valueOf(pages.get(4));
                int yaw = Integer.valueOf(pages.get(5));
                l.setPitch(pitch); 
                l.setYaw(yaw);
            }
        } catch (Exception e) {
               e.printStackTrace();
        }
        return l;

    }

}
