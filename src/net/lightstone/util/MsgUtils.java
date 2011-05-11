package net.lightstone.util;

import java.util.Collection;

import net.lightstone.model.Player;
import net.lightstone.msg.Message;
import net.lightstone.world.World;

public class MsgUtils {
	public static final void broadcastMessageToWorld(World w, Message m) {
		for (Player p : w.getPlayers()) {
			p.getSession().send(m);
		}
	}

	public static final void broadcastMessageToWorld(World w, Message m,
			Predicate<Player> f) {
		final Collection<Player> players = w.getEntities().filterPlayers(f);
		for (final Player p : players) {
			p.getSession().send(m);
		}
	}
}
