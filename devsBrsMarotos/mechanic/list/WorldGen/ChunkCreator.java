/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devsBrsMarotos.mechanic.list.WorldGen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

/**
 *
 * @author User
 */
public class ChunkCreator extends ChunkGenerator {
    
	@Override
	public List<BlockPopulator> getDefaultPopulators(World world){
		ArrayList<BlockPopulator> populators = new ArrayList<BlockPopulator>();
		
		populators.add(new FlowerPopulator());
		populators.add(new TreePopulator());
			
		return populators;
	}
	
	private void setBlockAt(byte[][] chunk, int x, int y, int z, Material type){
		int section = y >> 4;
		int position = ((y & 0xF) << 8) | (z << 4) | x;
		
		if (chunk[section] == null){
			chunk[section] = new byte[4096];
		}
		
		chunk[section][position] = (byte) type.getId();
	}
	
	@Override
	public byte[][] generateBlockSections(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomes){
	    byte[][] chunk = new byte[world.getMaxHeight() / 16][];
	    
	    SimplexOctaveGenerator gen = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
		
		gen.setScale(1 / 64.0);
		
		for (int x = 0; x < 16; ++x){
			for (int z = 0; z < 16; ++z){
				this.setBlockAt(chunk, x, 0, z, Material.BEDROCK);
				
				int height = (int) ((gen.noise(x + chunkX * 16, z + chunkZ * 16, 0.5, 0.5) / 0.75) + 40);
				
				for (int y = 1; y < height - 4; ++y){
					this.setBlockAt(chunk, x, y, z, Material.STONE);
				}
				
				for (int y = height - 4; y < height; ++y){
					this.setBlockAt(chunk, x, y, z, Material.DIRT);
				}
				
				this.setBlockAt(chunk, x, height, z, Material.GRASS);
				
				biomes.setBiome(x, z, Biome.PLAINS);
			}
		}
		
	    return chunk;
	}
    
}
