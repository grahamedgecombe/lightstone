package net.lightstone.cmd;

import net.lightstone.model.Player;
import net.lightstone.world.World;

/**
 * A command that sets or increments the current time.
 * @author Zhuowei Zhang
 * @author Graham Edgecombe
 */
public final class TimeCommand extends Command {

	/**
	 * Creates the {@code /time} command.
	 */
	public TimeCommand() {
		super("time");
	}

	@Override
	public void execute(Player player, String[] args) {
		if (args.length != 2) {
			player.sendMessage("§eUsage: /time <add|set> <value>");
			return;
		}

		String mode = args[0];
		int value;
		try {
			value = Integer.parseInt(args[1]);
		} catch (NumberFormatException ex) {
			player.sendMessage("§eThat doesn't appear to be a valid number.");
			return;
		}

		World world = player.getWorld();
		if (mode.equals("add")) {
			world.setTime(world.getTime() + value);
		} else if (mode.equals("set")) {
			world.setTime(value);
		} else {
			player.sendMessage("&eInvalid mode - try 'add' or 'set'.");
		}
	}

}

