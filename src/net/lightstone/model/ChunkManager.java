package net.lightstone.model;

import java.io.IOException;
import java.util.Map;

import net.lightstone.Server;
import net.lightstone.cache.ConcurrentLRUMap;
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
	 * A map of chunks currently loaded in memory.
	 */
	// TODO: Lower max size when players log out, or after x amount of time to
	// prevent the max chunks allowed to grow based on most players online at a
	// time, and never lower when the amount of players online drops.
	private final Map<Chunk.Key, Chunk> chunks = new ConcurrentLRUMap<Chunk.Key, Chunk>(
			0) {

		// Writes the chunk to disk.
		// TODO: Do I/O on a different thread.
		public void dispose(Chunk.Key k, Chunk v) {
			try {
				service.write(v.getX(), v.getZ(), v);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Doesn't remove the Chunk if a player knows about it.
		public boolean shouldRemove(Chunk.Key k, Chunk v) {
			for (final Player p : Server.getServer(service.getWorldName())
					.getWorld().getPlayers()) {
				if (p.getKnownChunks().contains(k)) {
					return false;
				}
			}
			return true;
		}

		// Increases the max size of the LRU map if there are no Chunks that can
		// be un-loaded.
		public boolean shouldRemoveLRU(Chunk.Key ck, Chunk c) {
			boolean ret = true;
			for (final Player p : Server.getServer(service.getWorldName())
					.getWorld().getPlayers()) {
				if (p.getKnownChunks().contains(ck)) {
					ret = false;
					break;
				}
			}
			if (!ret) {
				for (Map.Entry<Key, Chunk> e : entrySet()) {
					if (tryRemove(e.getKey(), e.getValue()))
						return false;
				}
				return false;
			}
			return true;
		}
	};

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
