package net.lightstone.msg.handler;

import net.lightstone.model.Player;
import net.lightstone.msg.Message;
import net.lightstone.net.Session;

/**
 * A {@link MessageHandler} performs some actions upon the arrival of an
 * incoming {@link Message}.
 * @author Graham Edgecombe
 * @param <T> The type of {@link Message}.
 */
public abstract class MessageHandler<T extends Message> {

	/**
	 * Handles an incoming message.
	 * @param session The session that sent the message.
	 * @param player The player that sent the message, or {@code null} if the
	 * session currently has no player.
	 * @param message The message.
	 */
	public abstract void handle(Session session, Player player, T message);

}

