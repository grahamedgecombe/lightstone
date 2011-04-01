package net.lightstone.msg.handler;

import net.lightstone.model.Player;
import net.lightstone.model.Position;
import net.lightstone.msg.PositionMessage;
import net.lightstone.net.Session;

/**
 * A {@link MessageHandler} that updates a {@link Player}'s {@link Position}
 * when the server receives a {@link PositionMessage}.
 * @author Graham Edgecombe
 */
public final class PositionMessageHandler extends MessageHandler<PositionMessage> {

	@Override
	public void handle(Session session, Player player, PositionMessage message) {
		if (player == null)
			return;

		player.setPosition(new Position(message.getX(), message.getY(), message.getZ()));
	}

}

