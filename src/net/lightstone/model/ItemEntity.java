package net.lightstone.model;

import net.lightstone.msg.Message;
import net.lightstone.msg.SpawnItemMessage;
import net.lightstone.world.World;

/**
 * Represents an item that is also an {@link Entity} within the world.
 * @author Graham Edgecombe
 */
public final class ItemEntity extends Entity {

	/**
	 * The item.
	 */
	private final Item item;

	/**
	 * Creates a new item entity.
	 * @param world The world.
	 * @param item The item.
	 */
	public ItemEntity(World world, Item item) {
		super(world);
		this.item = item;
	}

	/**
	 * Gets the item that this {@link ItemEntity} represents.
	 * @return The item.
	 */
	public Item getItem() {
		return item;
	}

	@Override
	public Message createSpawnMessage() {
		int x = position.getPixelX();
		int y = position.getPixelY();
		int z = position.getPixelZ();

		int yaw = rotation.getIntYaw();
		int pitch = rotation.getIntPitch();
		int roll = rotation.getIntRoll();

		return new SpawnItemMessage(id, item, x, y, z, yaw, pitch, roll);
	}

	@Override
	public Message createUpdateMessage() {
		// TODO we can probably use some generic implementation for all of
		// these
		return null;
	}

}

