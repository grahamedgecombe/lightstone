package net.lightstone.model;

import java.io.IOException;
import java.util.Map;

import net.lightstone.io.ChunkIoService;
import net.lightstone.world.WorldGenerator;

import org.apache.commons.collections.map.ReferenceMap;

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
	 * A map of chunks currently loaded in memory.
	 */
	// ReferenceMap's 'generics' weren't done properly...
	@SuppressWarnings("unchecked")
	private final Map<Chunk.Key, Chunk> chunks = new ReferenceMap(
			ReferenceMap.SOFT, ReferenceMap.SOFT);

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
		Chunk chunk = chunks.get(key);
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

}
