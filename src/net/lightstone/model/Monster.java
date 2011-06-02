package net.lightstone.model;

import java.util.ArrayList;
import java.util.List;

import net.lightstone.msg.Message;
import net.lightstone.msg.SpawnMobMessage;
import net.lightstone.util.Parameter;
import net.lightstone.world.World;
import net.lightstone.model.mob.ai.AI;
import net.lightstone.model.mob.ai.DummyAI;
import net.lightstone.model.mob.MobLookup;

/**
 * Represents a monster such as a creeper.
 * @author Graham Edgecombe
 */
public final class Monster extends Mob {

	/**
	 * The type of monster.
	 */
	private final int type;
	private final AI ai;




	/**
	 * Creates a new monster.
	 * @param world The world this monster is in.
	 * @param type The type of monster.
	 */
	public Monster(World world, int type) {
		super(world);
		this.type = type;
		this.ai = new DummyAI(this);
	}

	/**
	 * Gets the type of monster.
	 * @return The type of monster.
	 */
	public int getType() {
		return type;
	}

	public AI getAI(){
		return ai;
	}

	@Override
	public void pulse(){
		super.pulse();
		getAI().pulse();
	}

	@Override
	public Message createSpawnMessage() {
		int x = position.getPixelX();
		int y = position.getPixelY();
		int z = position.getPixelZ();
		int yaw = rotation.getIntYaw();
		int pitch = rotation.getIntPitch();
		return new SpawnMobMessage(id, type, x, y, z, yaw, pitch, metadata);
	}

}

