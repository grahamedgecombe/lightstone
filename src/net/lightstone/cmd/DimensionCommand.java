package net.lightstone.cmd;

import net.lightstone.model.Player;
import net.lightstone.model.Blocks;
import net.lightstone.world.World;
import net.lightstone.model.Inventory;
import net.lightstone.model.Item;

/**
 * A command that gives a user a number of items.
 * @author Zhuowei Zhang
 */
public final class DimensionCommand extends Command {

	/**
	 * Creates the {@code /dimension} command.
	 */
	public DimensionCommand() {
		super("dimension");
	}

	@Override
	public void execute(Player player, String[] args) {
		if (args.length != 1) {
			player.sendMessage("§eUsage: /dimension <normal|nether>");
			return;
		}
		int newDim;
		if(args[0].equals("normal")){
			newDim = 0;
		}
		else if(args[0].equals("nether")){
			newDim = -1;
		}
		else{
			player.sendMessage(args[0] + " is not one of <normal|nether>.");
			return;
		}
		player.getSession().moveToDimension(newDim);
				
	}

}

