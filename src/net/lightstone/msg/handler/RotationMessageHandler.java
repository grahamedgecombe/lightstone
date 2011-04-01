package net.lightstone.msg.handler;

import net.lightstone.model.Player;
import net.lightstone.model.Rotation;
import net.lightstone.msg.RotationMessage;
import net.lightstone.net.Session;

/**
 * A {@link MessageHandler} that updates a {@link Player}'s {@link Rotation}
 * when the server receives a {@link RotationMessage}.
 * @author Graham Edgecombe
 */
public final class RotationMessageHandler extends MessageHandler<RotationMessage> {

	@Override
	public void handle(Session session, Player player, RotationMessage message) {
		if (player == null)
			return;

		player.setRotation(new Rotation(message.getRotation(), message.getPitch()));
	}

}

