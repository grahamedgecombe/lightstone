package net.lightstone.world;

import static org.junit.Assert.*;

import net.lightstone.model.Chunk;

import org.junit.Test;

/**
 * A unit test for the {@link FlatGrassWorldGenerator} class.
 * @author Graham Edgecombe
 */
public final class FlatGrassWorldGeneratorTest {

	/**
	 * Tests the {@link FlatGrassWorldGenerator#generate(int, int)} method.
	 */
	@Test
	public void testGenerate() {
		WorldGenerator generator = new FlatGrassWorldGenerator();
		Chunk chunk = generator.generate(17, 5);

		for (int x = 0; x < Chunk.WIDTH; x++) {
			for (int z = 0; z < Chunk.HEIGHT; z++) {
				assertEquals(2, chunk.getType(x, z, 60));

				assertEquals(7, chunk.getType(x, z, 0));

				for (int y = 1; y < 55; y++)
					assertEquals(1, chunk.getType(x, z, y));

				for (int y = 55; y < 60; y++)
					assertEquals(3, chunk.getType(x, z, y));

				for (int y = 61; y < Chunk.HEIGHT; y++)
					assertEquals(0, chunk.getType(x, z, y));
			}
		}
	}

}

