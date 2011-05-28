package net.lightstone.net;

import java.util.ArrayDeque;
import java.util.Queue;

import net.lightstone.Server;
import net.lightstone.model.Player;
import net.lightstone.msg.KickMessage;
import net.lightstone.msg.RespawnMessage;
import net.lightstone.msg.Message;
import net.lightstone.msg.handler.HandlerLookupService;
import net.lightstone.msg.handler.MessageHandler;
import net.lightstone.world.World;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;

/**
 * A single connection to the server, which may or may not be associated with a
 * player.
 * @author Graham Edgecombe
 */
public final class Session {

	/**
	 * The number of ticks which are elapsed before a client is disconnected due
	 * to a timeout.
	 */
	private static final int TIMEOUT_TICKS = 300;

	/**
	 * The state this connection is currently in.
	 */
	public enum State {

		/**
		 * In the exchange handshake state, the server is waiting for the client
		 * to send its initial handshake packet.
		 */
		EXCHANGE_HANDSHAKE,

		/**
		 * In the exchange identification state, the server is waiting for the
		 * client to send its identification packet.
		 */
		EXCHANGE_IDENTIFICATION,

		/**
		 * In the game state the session has an associated player.
		 */
		GAME;
	}

	/**
	 * The server this session belongs to.
	 */
	private final Server server;

	/**
	 * The channel associated with this session.
	 */
	private final Channel channel;

	/**
	 * A queue of incoming and unprocessed messages.
	 */
	private final Queue<Message> messageQueue = new ArrayDeque<Message>();

	/**
	 * A timeout counter. This is increment once every tick and if it goes above
	 * a certain value the session is disconnected.
	 */
	private int timeoutCounter = 0;

	/**
	 * The current state.
	 */
	private State state = State.EXCHANGE_HANDSHAKE;

	/**
	 * The player associated with this session (if there is one).
	 */
	private Player player;

	/**
	 * A flag which indicates if this session is pending removal.
	 */
	private boolean pendingRemoval = false;

	private int dimension;

	public Session(Server server, Channel channel){
		this(server, channel, 0);
	}

	/**
	 * Creates a new session.
	 * @param server The server this session belongs to.
	 * @param channel The channel associated with this session.
	 * @param dimension The world (regular or nether) that the session is currently on.
	 */
	public Session(Server server, Channel channel, int dimension) {
		this.server = server;
		this.channel = channel;
		this.dimension = dimension;
	}

	/**
	 * Gets the state of this session.
	 * @return The session's state.
	 */
	public State getState() {
		return state;
	}

	/**
	 * Sets the state of this session.
	 * @param state The new state.
	 */
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * Gets the player associated with this session.
	 * @return The player, or {@code null} if no player is associated with it.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Sets the player associated with this session.
	 * @param player The new player.
	 * @throws IllegalStateException if there is already a player associated
	 * with this session.
	 */
	public void setPlayer(Player player) {
		if (this.player != null)
			throw new IllegalStateException();

		this.player = player;
		player.getWorld().getPlayers().add(player);
	}

	/**
	 * Handles any queued messages for this session and increments the timeout
	 * counter.
	 * @return {@code true} if this session is still active, {@code false} if
	 * it is pending removal.
	 */
	@SuppressWarnings("unchecked")
	public boolean pulse() {
		if (pendingRemoval)
			return false;

		timeoutCounter++;

		Message message;
		while ((message = messageQueue.poll()) != null) {
			MessageHandler<Message> handler = (MessageHandler<Message>) HandlerLookupService.find(message.getClass());
			if (handler != null) {
				handler.handle(this, player, message);
			}
			timeoutCounter = 0;
		}

		if (timeoutCounter >= TIMEOUT_TICKS)
			disconnect("Timed out");

		return true;
	}

	/**
	 * Sends a message to the client.
	 * @param message The message.
	 */
	public void send(Message message) {
		channel.write(message);
	}

	/**
	 * Disconnects the session with the specified reason. This causes a
	 * {@link KickMessage} to be sent. When it has been delivered, the channel
	 * is closed.
	 * @param reason The reason for disconnection.
	 */
	public void disconnect(String reason) {
		channel.write(new KickMessage(reason)).addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * Gets the server associated with this session.
	 * @return The server.
	 */
	public Server getServer() {
		return server;
	}

	public int getDimension() {
		return dimension;
	}

	public void moveToDimension(int dimension) throws IllegalArgumentException {
		World newworld = server.getWorld(dimension);
		if(newworld == null){
			throw new IllegalArgumentException("World " + dimension + " does not exist");
		}
		this.dimension = dimension;
		//remove the current player
		Player oldplayer = player;
		send(new RespawnMessage(dimension));
		oldplayer.destroy();
		player = new Player(this, oldplayer.getName());
		player.getWorld().getPlayers().add(player);
	}
		

	@Override
	public String toString() {
		return Session.class.getName() + " [address=" + channel.getRemoteAddress() + "]";
	}

	/**
	 * Adds a message to the unprocessed queue.
	 * @param message The message.
	 * @param <T> The type of message.
	 */
	<T extends Message> void messageReceived(T message) {
		messageQueue.add(message);
	}

	/**
	 * Flags this session for removal.
	 */
	void flagForRemoval() {
		pendingRemoval = true;
	}

	/**
	 * Disposes of this session by destroying the associated player, if there is
	 * one.
	 */
	void dispose() {
		if (player != null) {
			player.destroy();
			player = null; // in case we are disposed twice
		}
	}

}

