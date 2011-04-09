package net.lightstone.msg.handler;

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
			// TODO process command
			processCommand(session, player, text);
		} else {
			player.getWorld().broadcastMessage("<" + player.getName() + "> " + text);
		}
	}

	private void processCommand(Session session, Player player, String message){
		String[] command = message.substring(1).split(" ");
		String action = command[0];
		if(action.equals("time")){
			if(command.length!=3){
				session.send(new ChatMessage("Expected two parameters"));
				return;
			}
			try{
				long newTime;
				if(command[1].equals("set")){
					newTime=Long.parseLong(command[2]);
				}
				else if(command[1].equals("add")){
					newTime=player.getWorld().getTimeOfDay() + Long.parseLong(command[2]);
				}
				else{
					session.send(new ChatMessage("Invalid option for time command"));
					return;
				}
				player.getWorld().setTimeOfDay(newTime);
			}
			catch(NumberFormatException numex){
				session.send(new ChatMessage("The time you have entered is not a valid number."));
			}
		}
		else if(action.equals("help")){
			session.send(new ChatMessage("Commands: time"));
		}
		else{
			session.send(new ChatMessage("Unknown command."));
		}
	}

}

