package net.lightstone.world;

import java.util.Random;

import net.lightstone.model.Chunk;
import net.lightstone.model.Blocks;

/**
 * A {@link WorldGenerator} that generates nether chunks.
 * @author Zhuowei Zhang
 */
public final class SimpleNetherWorldGenerator extends FlatNetherrackWorldGenerator {

	private static final int MAX_GLOWSTONE_COLUMNS = 6;

	private static final int MAX_GLOWSTONE_HEIGHT = 10;

	private static final int MIN_GLOWSTONE_HEIGHT = 1;

	private static final int MAX_NUM_FIRES = 8;

	private Random random = new Random();

	@Override
	public Chunk generate(int chunkX, int chunkZ) {
		Chunk chunk = super.generate(chunkX, chunkZ);

		/* Generate glowstone stalactites */
		int numColumns = random.nextInt(MAX_GLOWSTONE_COLUMNS + 1);
		for (int i = 0; i < numColumns; i++) {
			int x = random.nextInt(Chunk.WIDTH);
			int z = random.nextInt(Chunk.HEIGHT);
			int height = random.nextInt(MAX_GLOWSTONE_HEIGHT - MIN_GLOWSTONE_HEIGHT) + MIN_GLOWSTONE_HEIGHT;
			for (int y = Chunk.DEPTH - 2; y >= Chunk.DEPTH - 2 - height; y--) {
				chunk.setType(x, z, y, Blocks.TYPE_GLOWSTONE);
				chunk.setBlockLight(x, z, y, 15);
			}
		}
		/* Generate fire on top of the netherrack. */
		int numFires = random.nextInt(MAX_NUM_FIRES + 1);
		for (int i = 0; i < numFires; i++) {
			int x = random.nextInt(Chunk.WIDTH);
			int z = random.nextInt(Chunk.HEIGHT);
			int y = 64;
			chunk.setType(x, z, y, Blocks.TYPE_FIRE);
			chunk.setBlockLight(x, z, y, 15);
		}
		return chunk;
	}

}

