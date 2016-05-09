/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.Dungeon;

import devsBrsMarotos.mechanic.Mecanica;
import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 *
 * @author vntgasl
 */
public class Rewind extends Mecanica {

    private static HashMap<Block, Material> rewind = new HashMap<Block, Material>();
     
    public static void add(Block b, Material m) {
        rewind.put(b, m);
    }
    
    @Override
    public void onDisable() {
        for (Block b : rewind.keySet())
        {
            b.setType(rewind.get(b));
        }
    }

    @Override
    public String getName() {
        return "Rewind";
    }
     
    
}
