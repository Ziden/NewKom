/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.WorldGen;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;


public class FlowerPopulator extends BlockPopulator {
	
	public void populate(World world, Random random, Chunk chunk){
		int x, z;
		Block block;
		
		int chunkX = chunk.getX() * 16;
		int chunkZ = chunk.getZ() * 16;
		
		for (x = 0; x < 16; ++x){
			for (z = 0; z < 16; ++z){
				block = world.getHighestBlockAt(chunkX + x, chunkZ + z);
				if (block.getType() == Material.GRASS){
					block = block.getRelative(BlockFace.UP);
				}
				if (random.nextInt(100) < 30){
					block.setType(Material.LONG_GRASS);
					block.setData((byte) 0x1);
				}else if (random.nextInt(100) < 2){
					block.setType((random.nextInt(100) < 65) ? Material.YELLOW_FLOWER : Material.RED_ROSE);
				}
			}
		}
	}
	
}