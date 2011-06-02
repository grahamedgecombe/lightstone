package net.lightstone.world;

import net.lightstone.model.Blocks;
import net.lightstone.model.Chunk;

/**
 * A simple {@link WorldGenerator} that generates a flat world made out of netherrack.
 * @author Graham Edgecombe
 * @author Zhuowei Zhang
 */
public class FlatNetherrackWorldGenerator implements WorldGenerator {

	@Override
	public Chunk generate(int chunkX, int chunkZ) {
		Chunk chunk = new Chunk(chunkX, chunkZ);
		for (int x = 0; x < Chunk.WIDTH; x++) {
			for (int z = 0; z < Chunk.HEIGHT; z++) {
				for (int y = 0; y < Chunk.DEPTH; y++) {
					int id = Blocks.TYPE_AIR;
					if (y == 0 || y == 127)
						id = 7; //Bedrock
					else if (y < 64)
						id = Blocks.TYPE_NETHERRACK;

					chunk.setType(x, z, y, id);
					chunk.setMetaData(x, z, y, 0);
					chunk.setBlockLight(x, z, y, 0);
					chunk.setSkyLight(x, z, y, 15);
				}
			}
		}
		return chunk;
	}

}

