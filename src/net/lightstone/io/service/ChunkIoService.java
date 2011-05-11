package net.lightstone.io.service;

import java.io.IOException;

import net.lightstone.io.service.impl.McRegionChunkIoService;
import net.lightstone.model.Chunk;
import net.lightstone.model.Coordinate;

/**
 * This abstract class should be extended by classes that wish to provide
 * some way of performing chunk I/O e.g. the {@link McRegionChunkIoService}.
 * This is abstracted away from the implementation because several formats are
 * available.
 * 
 * @author Graham Edgecombe
 * @author Joe Pritzel
 */
public abstract class ChunkIoService extends IoService<Chunk> {

	/**
	 * Reads a single chunk.
	 * 
	 * @param x
	 *            The X coordinate.
	 * @param z
	 *            The Z coordinate.
	 * @return The {@link Chunk} or {@code null} if it does not exist.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public Chunk read(int x, int z) throws IOException {
		return read(new Coordinate(x, 0, z));
	}

	@Override
	protected abstract Chunk read(Object key) throws IOException;

	/**
	 * Writes a single chunk.
	 * 
	 * @param x
	 *            The X coordinate.
	 * @param z
	 *            The Z coordinate.
	 * @param chunk
	 *            The {@link Chunk}.
	 * @return
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public Chunk write(int x, int z, Chunk chunk) throws IOException {
		return write(new Coordinate(x, 0, z), chunk);
	}

	protected abstract Chunk write(Object key, Chunk value) throws IOException;
}
