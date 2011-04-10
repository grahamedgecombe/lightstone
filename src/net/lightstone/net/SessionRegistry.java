package net.lightstone.net;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

/**
 * A list of all the sessions which provides a convenient {@link #pulse()}
 * method to pulse every session in one operation.
 * @author Graham Edgecombe
 */
public final class SessionRegistry {

	/**
	 * An array of sessions that have not yet been added.
	 */
	private final Queue<Session> pending = new ArrayDeque<Session>();

	/**
	 * A list of the sessions.
	 */
	private final List<Session> sessions = new ArrayList<Session>();

	/**
	 * Pulses all the sessions.
	 */
	public void pulse() {
		synchronized (pending) {
			Session session;
			while ((session = pending.poll()) != null) {
				sessions.add(session);
			}
		}

		for (Iterator<Session> it = sessions.iterator(); it.hasNext(); ) {
			Session session = it.next();
			if (!session.pulse()) {
				it.remove();
				session.dispose();
			}
		}
	}

	/**
	 * Adds a new session.
	 * @param session The session to add.
	 */
	void add(Session session) {
		synchronized (pending) {
			pending.add(session);
		}
	}

	/**
	 * Removes a session.
	 * @param session The session to remove.
	 */
	void remove(Session session) {
		session.flagForRemoval();
	}

}

