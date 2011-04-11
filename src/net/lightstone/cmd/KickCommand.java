package net.lightstone.cmd;

import net.lightstone.model.Player;
import net.lightstone.msg.KickMessage;
import net.lightstone.world.World;

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
		// TODO check if the player executing this command is an admin
		if (args.length != 1) {
			player.sendMessage("§eUsage: /kick <username>");
			return;
		}

		World world = player.getWorld();
		String name = args[0];

		for (Player p : world.getPlayers()) {
			if (p.getName().equalsIgnoreCase(name)) {
				player.sendMessage("§eKicking " + p.getName());
				p.getSession().send(new KickMessage("Kicked by " + player.getName()));
				return;
			}
		}

		player.sendMessage("§eCan't find user " + name + ". No kick.");
	}

}

