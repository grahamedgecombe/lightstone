package net.lightstone.util;

import java.util.Collection;

import org.infinispan.Cache;

import net.lightstone.model.Player;
import net.lightstone.msg.Message;
import net.lightstone.world.World;

/**
 * A class that has common functions used with messages.
 * 
 * @author Joe Pritzel
 * 
 */
public class MsgUtils {
	/**
	 * Broadcasts a message to all the {@link Player}s in the specified
	 * {@link World}.
	 * 
	 * @param world
	 * @param message
	 */
	public static final void broadcastMessageToWorld(final World world,
			final Message message) {
		for (Player p : world.getPlayers()) {
			p.getSession().send(message);
		}
	}

	/**
	 * Broadcasts a message to all the {@link Player}s in the specified
	 * {@link World} that satisfy the {@link Predicate}.
	 * 
	 * @param world
	 * @param message
	 * @param predicate
	 */
	public static final void broadcastMessageToWorld(final World world,
			final Message message, Predicate<Player> predicate) {
		final Collection<Player> players = world.getEntities().filterPlayers(
				predicate);
		for (final Player p : players) {
			p.getSession().send(message);
		}
	}
}
