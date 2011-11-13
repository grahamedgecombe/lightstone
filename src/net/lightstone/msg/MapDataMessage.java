package net.lightstone.msg;

/**
 * A message to update a map in the player's inventory.
 */

public final class MapDataMessage extends Message {

	private final int id;

	private final byte[] data;

	/**
	 * @param id The id of the map to update.
	 * @param data The new map data.
	 */
	public MapDataMessage(int id, byte[] data) {
		this.id = id;
		this.data = data;
	}

	public int getId() {
		return id;
	}

	public byte[] getData() {
		return data;
	}

}

