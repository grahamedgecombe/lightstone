package net.lightstone;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.io.File;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.lightstone.cmd.CommandManager;
import net.lightstone.io.McRegionChunkIoService;
import net.lightstone.net.MinecraftPipelineFactory;
import net.lightstone.net.Session;
import net.lightstone.net.SessionRegistry;
import net.lightstone.task.TaskScheduler;
import net.lightstone.world.ForestWorldGenerator;
import net.lightstone.world.SimpleNetherWorldGenerator;
import net.lightstone.world.World;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * The core class of the Lightstone server.
 * @author Graham Edgecombe
 */
public final class Server {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(Server.class.getName());

	/**
	 * Creates a new server on TCP port 25565 and starts listening for
	 * connections.
	 * @param args The command-line arguments.
	 */
	public static void main(String[] args) {
		try {
			Server server = new Server();
			server.bind(new InetSocketAddress(25565));
			server.start();
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "Error during server startup.", t);
		}
	}

	/**
	 * The {@link ServerBootstrap} used to initialize Netty.
	 */
	private final ServerBootstrap bootstrap = new ServerBootstrap();

	/**
	 * A group containing all of the channels.
	 */
	private final ChannelGroup group = new DefaultChannelGroup();

	/**
	 * The network executor service - Netty dispatches events to this thread
	 * pool.
	 */
	private final ExecutorService executor = Executors.newCachedThreadPool();

	/**
	 * A list of all the active {@link Session}s.
	 */
	private final SessionRegistry sessions = new SessionRegistry();

	/**
	 * The task scheduler used by this server.
	 */
	private final TaskScheduler scheduler = new TaskScheduler(this);

	/**
	 * The command manager.
	 */
	private final CommandManager commandManager = new CommandManager();

	/**
	 * The worlds this server is managing.
	 */
	private final Map<Integer, World> worlds = new HashMap<Integer, World>();


	/**
	 * Creates a new server.
	 */
	public Server() {
		logger.info("Starting Lightstone...");
		worlds.put(new Integer(0), new World(new McRegionChunkIoService(), new ForestWorldGenerator()));
		worlds.put(new Integer(-1), new World(new McRegionChunkIoService(new File("world" + File.separator + "DIM-1")),
			new SimpleNetherWorldGenerator()));
		init();
	}

	/**
	 * Initializes the channel and pipeline factories.
	 */
	private void init() {
		ChannelFactory factory = new NioServerSocketChannelFactory(executor, executor);
		bootstrap.setFactory(factory);

		ChannelPipelineFactory pipelineFactory = new MinecraftPipelineFactory(this);
		bootstrap.setPipelineFactory(pipelineFactory);
	}

	/**
	 * Binds this server to the specified address.
	 * @param address The addresss.
	 */
	public void bind(SocketAddress address) {
		logger.info("Binding to address: " + address + "...");
		group.add(bootstrap.bind(address));
	}

	/**
	 * Starts this server.
	 */
	public void start() {
		scheduler.start();
		logger.info("Ready for connections.");
	}

	/**
	 * Gets the channel group.
	 * @return The {@link ChannelGroup}.
	 */
	public ChannelGroup getChannelGroup() {
		return group;
	}

	/**
	 * Gets the session registry.
	 * @return The {@link SessionRegistry}.
	 */
	public SessionRegistry getSessionRegistry() {
		return sessions;
	}

	/**
	 * Gets the task scheduler.
	 * @return The {@link TaskScheduler}.
	 */
	public TaskScheduler getScheduler() {
		return scheduler;
	}

	/**
	 * Gets the command manager.
	 * @return The {@link CommandManager}.
	 */
	public CommandManager getCommandManager() {
		return commandManager;
	}

	/**
	 * Gets a world managed by this server.
	 * @return a {@link World} this server manages.
	 */
	public World getWorld(int index){
		return worlds.get(new Integer(index));
	}

	public Map<Integer, World> getWorlds(){
		return Collections.unmodifiableMap(worlds);
	}

}

