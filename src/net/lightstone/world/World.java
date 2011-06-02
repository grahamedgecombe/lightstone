package net.lightstone.world;

import java.util.Collection;

import net.lightstone.io.ChunkIoService;
import net.lightstone.model.ChunkManager;
import net.lightstone.model.Entity;
import net.lightstone.model.EntityManager;
import net.lightstone.model.Player;
import net.lightstone.model.Position;
import net.lightstone.msg.ChatMessage;
import net.lightstone.msg.TimeMessage;
import net.lightstone.msg.ChangeStateMessage;

/**
 * A class which represents the in-game world.
 * @author Graham Edgecombe
 */
public class World {

	/** The dimension of the normal game world. */
	public static final int DIM_NORMAL = 0;

	/** The dimension number of the nether world. */
	public static final int DIM_NETHER = -1;


	/**
	 * The number of pulses in a Minecraft day.
	 */
	private static final int PULSES_PER_DAY = 24000;

	/**
	 * The spawn position.
	 */
	private final Position spawnPosition = new Position(0, 120, 0);

	/**
	 * The chunk manager.
	 */
	private final ChunkManager chunks;

	/**
	 * The entity manager.
	 */
	private final EntityManager entities = new EntityManager();

	/**
	 * The current time.
	 */
	private long time = 0;

	/** Whether it is raining. */

	private boolean raining = false;

	/**
	 * Creates a new world with the specified chunk I/O service and world
	 * generator.
	 * @param service The chunk I/O service.
	 * @param generator The world generator.
	 */
	public World(ChunkIoService service, WorldGenerator generator) {
		chunks = new ChunkManager(service, generator);
	}

	/**
	 * Updates all the entities within this world.
	 */
	public void pulse() {
		for (Entity entity : entities)
			entity.pulse();

		for (Entity entity : entities)
			entity.reset();

		advanceTime();
	}

	/**
	 * Gets the chunk manager.
	 * @return The chunk manager.
	 */
	public ChunkManager getChunks() {
		return chunks;
	}

	/**
	 * Gets the entity manager.
	 * @return The entity manager.
	 */
	public EntityManager getEntities() {
		return entities;
	}

	/**
	 * Gets a collection of all the players within this world.
	 * @return A {@link Collection} of {@link Player} objects.
	 */
	public Collection<Player> getPlayers() {
		return entities.getAll(Player.class);
	}

	/**
	 * Gets the spawn position.
	 * @return The spawn position.
	 */
	public Position getSpawnPosition() {
		return spawnPosition;
	}

	/**
	 * Broadcasts a message to every player.
	 * @param text The message text.
	 */
	public void broadcastMessage(String text) {
		ChatMessage message = new ChatMessage(text);
		for (Player player : getPlayers())
			player.getSession().send(message);
	}

	/**
	 * Gets the current time.
	 * @return The current time.
	 */
	public long getTime() {
		return time;
	}

	/**
	 * Sets the current time.
	 * @param time The current time.
	 */
	public void setTime(long time){
		this.time = time % PULSES_PER_DAY;

		TimeMessage msg = new TimeMessage(time);
		for (Player player : getPlayers())
			player.getSession().send(msg);
	}

	/**
	 * Advances the time forwards, should be called every pulse.
	 */
	private void advanceTime() {
		time = (time + 1) % PULSES_PER_DAY;
		// TODO: every now and again we should broadcast the time to all
		//       players to keep things in sync
	}

	public void setRaining(boolean raining){
		this.raining = raining;
		ChangeStateMessage msg = new ChangeStateMessage(raining? ChangeStateMessage.START_RAINING: ChangeStateMessage.STOP_RAINING);
		for (Player player : getPlayers())
			player.getSession().send(msg);
	}
	public boolean isRaining(){
		return raining;
	}

}

