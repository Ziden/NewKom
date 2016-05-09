/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.WorldGen;


import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class TreePopulator extends BlockPopulator {
	
	public void populate(World world, Random random, Chunk chunk){
		if (random.nextInt(100) < 10){
			int x = chunk.getX() * 16 + random.nextInt(5) + 6;
			int z = chunk.getZ() * 16 + random.nextInt(5) + 6;
			int y = world.getHighestBlockYAt(x, z);
			world.generateTree(new Location(world, x, y, z), TreeType.TREE);
		}
	}
	
}