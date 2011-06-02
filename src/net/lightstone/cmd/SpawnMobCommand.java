package net.lightstone.cmd;

import java.util.Random;

import net.lightstone.model.Player;
import net.lightstone.model.*;
import net.lightstone.model.mob.*;
import net.lightstone.world.World;

/**
 * A command that spawns mobs.
 * @author Zhuowei Zhang
 */
public final class SpawnMobCommand extends Command {

	private static final int MAX_SPAWN_PER_COMMAND = 9000;

	private Random rand = new Random();

	/**
	 * Creates the {@code /spawnmob} command.
	 */
	public SpawnMobCommand() {
		super("spawnmob");
	}

	@Override
	public void execute(Player player, String[] args) {
		if (!(args.length == 1 || args.length == 2)) {
			player.sendMessage("§eUsage: /spawnmob <mobname> [number]");
			return;
		}

		World world = player.getWorld();
		String name = args[0];
		int num = 1;
		if(args.length > 1){
			try{
				num = Integer.parseInt(args[1]);
			}
			catch(Exception e){
				player.sendMessage("Number of mobs invalid.");
				return;
			}
		}
		if(num > MAX_SPAWN_PER_COMMAND){
			player.sendMessage("Please do not spawn more than " + MAX_SPAWN_PER_COMMAND + " at a time.");
			return;
		}
		int mobType;
		try{
			mobType = MobLookup.nameToId(name, false);
		}
		catch(Exception e){
			player.sendMessage("mob name invalid.");
			return;
		}
		for(int i=0;i<num;i++){
			Monster monster = new Monster(world, mobType);
			Position playerPos = player.getPosition();
			monster.setPosition(new Position(playerPos.getX() + (rand.nextInt(10) - 5), playerPos.getY(),
				playerPos.getZ() + (rand.nextInt(10) - 5)));
		}
	}

}

