package net.lightstone.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.lightstone.Server;
import net.lightstone.world.World;

/**
 * A class which schedules {@link Task}s.
 * @author Graham Edgecombe
 */
public final class TaskScheduler {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(TaskScheduler.class.getName());

	/**
	 * The number of milliseconds between pulses.
	 */
	private static final int PULSE_EVERY = 50;

	/**
	 * The server.
	 */
	private final Server server;

	/**
	 * The scheduled executor service which backs this scheduler.
	 */
	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	/**
	 * A list of new tasks to be added.
	 */
	private final List<Task> newTasks = new ArrayList<Task>();

	/**
	 * A list of active tasks.
	 */
	private final List<Task> tasks = new ArrayList<Task>();

	/**
	 * Creates a new task scheduler.
	 * @param server The server.
	 */
	public TaskScheduler(Server server) {
		this.server = server;
	}

	/**
	 * Starts the task scheduler.
	 */
	public void start() {
		executor.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					pulse();
				} catch (Throwable t) {
					logger.log(Level.SEVERE, "Uncaught exception in task scheduler.", t);
					// TODO in the future consider shutting down the server at this point?
				}
			}
		}, 0, PULSE_EVERY, TimeUnit.MILLISECONDS);
	}

	/**
	 * Schedules the specified task.
	 * @param task The task.
	 */
	public void schedule(Task task) {
		synchronized (newTasks) {
			newTasks.add(task);
		}
	}

	/**
	 * Adds new tasks and updates existing tasks, removing them if necessary.
	 */
	private void pulse() {
		// handle incoming messages
		server.getSessionRegistry().pulse();

		// handle tasks
		synchronized (newTasks) {
			for (Task task : newTasks) {
				tasks.add(task);
			}
			newTasks.clear();
		}

		for (Iterator<Task> it = tasks.iterator(); it.hasNext(); ) {
			Task task = it.next();
			if (!task.pulse()) {
				it.remove();
			}
		}

		// handle general game logic
		for(World w: server.getWorlds().values()){
			w.pulse();
		}
	}

}

