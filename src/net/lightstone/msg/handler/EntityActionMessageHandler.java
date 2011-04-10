package net.lightstone.msg.handler;

import net.lightstone.model.Entity;
import net.lightstone.model.Player;
import net.lightstone.msg.EntityActionMessage;
import net.lightstone.net.Session;

/**
 * A {@link MessageHandler} which handles {@link Entity} action messages.
 * @author Zhuowei Zhang
 */
public final class EntityActionMessageHandler extends MessageHandler<EntityActionMessage> {

	@Override
	public void handle(Session session, Player player, EntityActionMessage message) {
		switch (message.getAction()) {
		case EntityActionMessage.ACTION_CROUCH:
			player.setCrouching(true);
			break;
		case EntityActionMessage.ACTION_UNCROUCH:
			player.setCrouching(false);
			break;
		default:
			// TODO: bed support
			return;
		}
	}


}

