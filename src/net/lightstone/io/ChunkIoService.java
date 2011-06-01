package net.lightstone.io;

import java.io.IOException;

import net.lightstone.model.Chunk;

/**
 * This interface should be implemented by classes which wish to provide some
 * way of performing chunk I/O e.g. the {@link McRegionChunkIoService}. This
 * interface is abstracted away from the implementation because several
 * formats are available.
 * @author Graham Edgecombe
 */
public interface ChunkIoService {

	/**
	 * Reads a single chunk.
	 * @param x The X coordinate.
	 * @param z The Z coordinate.
	 * @return The {@link Chunk} or {@code null} if it does not exist.
	 * @throws IOException if an I/O error occurs.
	 */
	public Chunk read(int x, int z) throws IOException;

	/**
	 * Writes a single chunk.
	 * @param x The X coordinate.
	 * @param z The Z coordinate.
	 * @param chunk The {@link Chunk}.
	 * @throws IOException if an I/O error occurs.
	 */
	public void write(int x, int z, Chunk chunk) throws IOException;

}

