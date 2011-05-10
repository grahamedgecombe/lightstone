package net.lightstone.msg.handler;

import net.lightstone.model.Player;
import net.lightstone.msg.IdentificationMessage;
import net.lightstone.net.Session;
import net.lightstone.net.Session.State;

/**
 * A {@link MessageHandler} which performs the initial identification with
 * clients.
 * @author Graham Edgecombe
 */
public final class IdentificationMessageHandler extends MessageHandler<IdentificationMessage> {

	@Override
	public void handle(Session session, Player player, IdentificationMessage message) {
		Session.State state = session.getState();
		if (state == Session.State.EXCHANGE_IDENTIFICATION) {
			session.setState(State.GAME);
			session.send(new IdentificationMessage(0, "", 0, 0));
			session.setPlayer(new Player(session, message.getName())); // Don't touch the case...  You're losing data that could be useful.
		} else {
			boolean game = state == State.GAME;
			session.disconnect(game ? "Identification already exchanged." : "Handshake not yet exchanged.");
		}
	}

}