package net.lightstone.cmd;

import net.lightstone.model.Player;
import net.lightstone.world.World;
import net.lightstone.msg.KickMessage;

/**
 * A command that kicks a user off the server.
 * @author Zhuowei Zhang
 */
public final class KickCommand extends Command {

	/**
	 * Creates the {@code /kick} command.
	 */
	public KickCommand() {
		super("kick");
	}

	@Override
	public void execute(Player player, String[] args) {
		if (args.length != 1) {
			player.sendMessage("§eUsage: /kick [username]");
			return;
		}

		String username = args[0].toLowerCase();

		for(Player p: player.getWorld().getPlayers()){
			if(p.getName().toLowerCase().equals(username)){
				player.getWorld().broadcastMessage("§eKicking " + p.getName());
				p.getSession().send(new KickMessage("Kicked by " + player.getName()));
				return;
			}
		}
		player.sendMessage("User not found.");
	}

}

