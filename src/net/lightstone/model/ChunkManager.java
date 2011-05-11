package net.lightstone.model;

import java.io.IOException;

import org.infinispan.Cache;

import net.lightstone.cache.GenericCache;
import net.lightstone.io.ChunkIoService;
import net.lightstone.model.Chunk.Key;
import net.lightstone.world.WorldGenerator;

/**
 * A class which manages the {@link Chunk}s currently loaded in memory.
 * 
 * @author Graham Edgecombe
 * @author Joe Pritzel
 */
public final class ChunkManager {

	/**
	 * The chunk I/O service used to read chunks from the disk and write them to
	 * the disk.
	 */
	private final ChunkIoService service;

	/**
	 * The world generator used to generate new chunks.
	 */
	private final WorldGenerator generator;

	/**
	 * The chunk cache for this ChunkManager.
	 */
	private final Cache<Chunk.Key, Chunk> chunks;

	/**
	 * Creates a new chunk manager with the specified I/O service and world
	 * generator.
	 * 
	 * @param service
	 *            The I/O service.
	 * @param generator
	 *            The world generator.
	 */
	public ChunkManager(ChunkIoService service, WorldGenerator generator) {
		this.service = service;
		this.generator = generator;
		this.chunks = (Cache<Key, Chunk>) new GenericCache().getCache();
	}

	/**
	 * Gets the chunk at the specified X and Z coordinates, loading it from the
	 * disk or generating it if necessary.
	 * 
	 * @param x
	 *            The X coordinate.
	 * @param z
	 *            The Z coordinate.
	 * @return The chunk.
	 */
	public Chunk getChunk(int x, int z) {
		Chunk.Key key = new Chunk.Key(x, z);
		Chunk chunk = (Chunk) chunks.get(key);
		if (chunk == null) {
			try {
				chunk = service.read(x, z);
			} catch (IOException e) {
				chunk = null;
			}

			if (chunk == null) {
				chunk = generator.generate(x, z);
			}

			chunks.put(key, chunk);
		}
		return chunk;
	}
	
	public static int getChunkX(int x) {
		return x / Chunk.WIDTH + ((x < 0 && x % Chunk.WIDTH != 0) ? -1 : 0);
	}

	public static int getChunkZ(int z) {
		return z / Chunk.HEIGHT + ((z < 0 && z % Chunk.HEIGHT != 0) ? -1 : 0);
	}

}
