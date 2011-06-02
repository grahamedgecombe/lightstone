package net.lightstone.cmd;

import net.lightstone.model.Player;
import net.lightstone.msg.KickMessage;
import net.lightstone.world.World;
import net.lightstone.model.Position;
import net.lightstone.msg.PositionMessage;

/**
 * A command that teleport one player to another.
 * @author Zhuowei Zhang
 */
public final class TeleportCommand extends Command {

	/**
	 * Creates the {@code /tp} command.
	 */
	public TeleportCommand() {
		super("tp");
	}

	@Override
	public void execute(Player player, String[] args) {
		// TODO check if the player executing this command is an admin
		if (args.length != 2) {
			player.sendMessage("§eUsage: /tp <player1> <player2>");
			return;
		}

		String name = args[0];
		String destName = args[1];
		if(name.equals(destName)){
			player.sendMessage("Player1 and Player2 must be different");
			return;
		}
		World world = player.getWorld();
		Player p1 = null;
		Player p2 = null;

		for (Player p : world.getPlayers()) {
			if (p.getName().equalsIgnoreCase(name)) {
				p1 = p;
			}
			else if(p.getName().equalsIgnoreCase(destName)){
				p2 = p;
			}
			if(p1 != null && p2 != null){
				Position newpos = p2.getPosition();
				p1.getSession().send(new PositionMessage(newpos.getX(), newpos.getY() + (Player.NORMAL_EYE_HEIGHT*2), newpos.getZ(), newpos.getY() + (Player.NORMAL_EYE_HEIGHT*3), true));
				return;
			}
		}
		if(p1 == null){
			player.sendMessage("§eCan't find user " + name + ".");
		}
		if(p2 == null){
			player.sendMessage("§eCan't find user " + destName + ".");
		}
	}

}

