/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Dungeon;

import devsBrsMarotos.mechanic.Mecanica;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author vntgasl
 */
public class BookStuff extends Mecanica {

    @Override
    public String getName() {
        return "Book Stuff";
    }
    
    public void interage(PlayerInteractEvent ev) {
        if (ev.getAction().equals(Action.PHYSICAL)) {
            if (ev.getClickedBlock().getType() == Material.WOOD_PLATE) {
                BookUtil.acaoLivroEmPressurePlate(ev);
                return;
            }
        }
        if (ev.getClickedBlock() != null && ev.getClickedBlock().getType() == Material.BOOKSHELF) {
            BookUtil.getBookOnBookshelf(ev);
            return;
        }
    }
    
}
