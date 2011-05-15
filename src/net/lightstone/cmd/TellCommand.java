package net.lightstone.cmd;

import java.util.Arrays;

import net.lightstone.model.Player;
import net.lightstone.world.World;
import net.lightstone.util.StringUtils;

/**
 * A command that sends a private message to a player.
 * @author Zhuowei Zhang
 */
public final class TellCommand extends Command {

	/**
	 * Creates the {@code /tell} command.
	 */
	public TellCommand() {
		super("tell");
	}

	@Override
	public void execute(Player player, String[] args) {

		if (args.length < 2) {
			player.sendMessage("§eUsage: /tell <username> <message>");
			return;
		}

		World world = player.getWorld();
		String name = args[0];

		for (Player p : world.getPlayers()) {
			if (p.getName().equalsIgnoreCase(name)) {
				p.sendMessage("§7" + player.getName() + " whispers " + 
					StringUtils.join(Arrays.copyOfRange(args, 1, args.length) , " "));
				return;
			}
		}

		player.sendMessage("§eCan't find user " + name + ".");
	}

}

