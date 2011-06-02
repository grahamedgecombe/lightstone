package net.lightstone.model;

import java.util.ArrayList;
import java.util.List;

import net.lightstone.msg.EntityRotationMessage;
import net.lightstone.msg.EntityTeleportMessage;
import net.lightstone.msg.Message;
import net.lightstone.msg.RelativeEntityPositionMessage;
import net.lightstone.msg.RelativeEntityPositionRotationMessage;
import net.lightstone.msg.EntityMetadataMessage;
import net.lightstone.world.World;
import net.lightstone.util.Parameter;

/**
 * A Mob is a {@link Player} or {@link Monster}.
 * @author Graham Edgecombe.
 */
public abstract class Mob extends Entity {


	/**
	 * The mob's metadata.
	 */
	protected final List<Parameter<?>> metadata = new ArrayList<Parameter<?>>();

	/**
	 * Creates a mob within the specified world.
	 * @param world The world.
	 */
	public Mob(World world) {
		super(world);
	}

	@Override
	public Message createUpdateMessage() {
		boolean moved = !position.equals(previousPosition);
		boolean rotated = !rotation.equals(previousRotation);

		int x = position.getPixelX();
		int y = position.getPixelY();
		int z = position.getPixelZ();

		int dx = x - previousPosition.getPixelX();
		int dy = y - previousPosition.getPixelY();
		int dz = z - previousPosition.getPixelZ();

		boolean teleport = dx > Byte.MAX_VALUE || dy > Byte.MAX_VALUE || dz > Byte.MAX_VALUE || dx < Byte.MIN_VALUE || dy < Byte.MIN_VALUE || dz < Byte.MIN_VALUE;

		int yaw = rotation.getIntYaw();
		int pitch = rotation.getIntPitch();

		if (moved && teleport) {
			return new EntityTeleportMessage(id, x, y, z, yaw, pitch);
		} else if (moved && rotated) {
			return new RelativeEntityPositionRotationMessage(id, dx, dy, dz, yaw, pitch);
		} else if (moved) {
			return new RelativeEntityPositionMessage(id, dx, dy, dz);
		} else if (rotated) {
			return new EntityRotationMessage(id, yaw, pitch);
		}

		return null;
	}

	public Parameter<?> getMetadata(int index){
		return metadata.get(index);
	}

	public void setMetadata(Parameter<?> data){
		if(data.getIndex() < metadata.size()){
			metadata.set(data.getIndex(), data);
		}
		else{
			metadata.add(data);
		}
	}


}

