package net.lightstone.cmd;

import net.lightstone.model.Player;


/**
 * A command that saves all known chunks in the ChunkManager.
 * @author Zhuowei Zhang
 */
public final class SaveAllCommand extends Command {

	/**
	 * Creates the {@code /save-all} command.
	 */
	public SaveAllCommand() {
		super("save-all");
	}

	@Override
	public void execute(Player player, String[] args) {
		//Should this start a separate thread instead?
		player.getWorld().getChunks().saveAll();
	}

}

