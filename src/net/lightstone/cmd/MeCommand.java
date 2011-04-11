package net.lightstone.cmd;

import net.lightstone.model.Player;
import net.lightstone.util.StringUtils;

/**
 * A command that allows users to send a message in a different format that
 * leads to a 'role-playing' style message.
 * @author Graham Edgecombe
 */
public final class MeCommand extends Command {

	/**
	 * Creates the {@code /me} command.
	 */
	public MeCommand() {
		super("me");
	}

	@Override
	public void execute(Player player, String[] args) {
		String message = StringUtils.join(args, " ");
		player.getWorld().broadcastMessage(" * " + player.getName() + " " + message);
	}

}

