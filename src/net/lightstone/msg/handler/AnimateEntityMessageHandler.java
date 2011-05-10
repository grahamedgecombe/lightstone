package net.lightstone.msg.handler;

import net.lightstone.model.Entity;
import net.lightstone.model.Player;
import net.lightstone.msg.AnimateEntityMessage;
import net.lightstone.net.Session;

/**
 * A {@link MessageHandler} which handles {@link Entity} animations.
 * @author Zhuowei Zhang
 * @author Joe Pritzel
 */
public final class AnimateEntityMessageHandler extends MessageHandler<AnimateEntityMessage> {

	@Override
	public void handle(Session session, Player player, AnimateEntityMessage message) {
		if(!message.isValid()) {
			return;
		}
		
		message = new AnimateEntityMessage(player.getId(), message.getAnimation());
		for (Player p : player.getWorld().getPlayers()) {
			if (p != player) {
				p.getSession().send(message);
			}
		}
	}

}
