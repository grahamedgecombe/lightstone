package net.lightstone.cmd;

import net.lightstone.model.Player;
import net.lightstone.msg.ChangeStateMessage;
import net.lightstone.world.World;

/**
 * A command that starts or stops serverwide rain.
 * @author Zhuowei Zhang
 */
public final class RainCommand extends Command {

	/**
	 * Creates the {@code /kick} command.
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
		int state;
		if(action.equals("on")){
			state = ChangeStateMessage.START_RAINING;
		}
		else if(action.equals("off")){
			state = ChangeStateMessage.STOP_RAINING;
		}
		else{
			player.sendMessage("§cInvalid parameter. §eUsage: /rain <on|off>");
			return;
		}

		ChangeStateMessage msg = new ChangeStateMessage(state);
		/* Of course, new players won't get rained on. However, this is just a test anyway. */
		for (Player p : world.getPlayers()) {
			p.getSession().send(msg);
		}

	}

}

