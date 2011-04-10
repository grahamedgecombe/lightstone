package net.lightstone.msg.handler;

import net.lightstone.cmd.CommandManager;
import net.lightstone.model.Player;
import net.lightstone.msg.ChatMessage;
import net.lightstone.net.Session;

/**
 * A {@link MessageHandler} which handles {@link ChatMessage}s by processing
 * commands or broadcasting messages to every player in the server.
 * @author Graham Edgecombe
 */
public final class ChatMessageHandler extends MessageHandler<ChatMessage> {

	@Override
	public void handle(Session session, Player player, ChatMessage message) {
		if (player == null)
			return;

		String text = message.getMessage();
		if (text.length() > 100) {
			session.disconnect("Chat message too long.");
		} else if (text.startsWith("/")) {
			CommandManager manager = session.getServer().getCommandManager();
			manager.execute(player, text);
		} else {
			player.getWorld().broadcastMessage("<" + player.getName() + "> " + text);
		}
	}

}

