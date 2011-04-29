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
public final class GiveCommand extends Command {

	/**
	 * Creates the {@code /give} command.
	 */
	public GiveCommand() {
		super("give");
	}

	@Override
	public void execute(Player player, String[] args) {
		if (!(args.length >= 2 && args.length <= 4)) {
			player.sendMessage("§eUsage: /give <name> <id> [count] [damage]");
			return;
		}

		World world = player.getWorld();
		String name = args[0];
		int itemId;
		int itemCount = 1;
		int itemDamage = 0;
		try{
			itemId = Integer.parseInt(args[1]);
			if(args.length>=3){
				itemCount = Integer.parseInt(args[2]);
				if(args.length==4){
					itemDamage = Integer.parseInt(args[3]);
					if(itemDamage < 0 || itemDamage >= 16){
						player.sendMessage("§eInvalid damage! ");
						return;
					}
				}
			}
		}
		catch(Exception e){
			player.sendMessage("§eInvalid parameters! ");
			return;
		}
		if(!(itemId>0 && (itemId<=Blocks.NUMBER_OF_BLOCKS||itemId>=0x100))){
			player.sendMessage("Invalid item id: " + itemId);
			return;
		}
		for (Player p : world.getPlayers()) {
			if (p.getName().equalsIgnoreCase(name)) {
				player.sendMessage("§eGiving " + p.getName() + " some " + args[1]);
				p.getInventory().add(new Item(itemId, itemCount, itemDamage));
				return;
			}
		}

		player.sendMessage("§eCan't find user " + name);
	}

}

