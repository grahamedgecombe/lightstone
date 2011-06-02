package net.lightstone.cmd;

import net.lightstone.model.Player;
import net.lightstone.world.World;

/**
 * A command that starts or stops serverwide rain.
 * @author Zhuowei Zhang
 */
public final class RainCommand extends Command {

	/**
	 * Creates the {@code /rain} command.
	 */
	public RainCommand() {
		super("rain");
	}

	@Override
	public void execute(Player player, String[] args) {
		if (args.length != 1) {
			player.sendMessage("§eUsage: /rain <on|off>");
			return;
		}

		World world = player.getWorld();
		String action = args[0];
		if(action.equals("on")){
			world.setRaining(true);
		}
		else if(action.equals("off")){
			world.setRaining(false);
		}
		else{
			player.sendMessage("§cInvalid parameter. §eUsage: /rain <on|off>");
		}

	}

}

