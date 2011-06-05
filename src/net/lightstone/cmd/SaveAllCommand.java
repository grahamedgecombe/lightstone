package net.lightstone.cmd;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.lightstone.model.Player;

/**
 * A command that saves all known chunks in the ChunkManager.
 * @author Zhuowei Zhang
 */
public final class SaveAllCommand extends Command {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(SaveAllCommand.class.getName());

	/**
	 * Creates the {@code /save-all} command.
	 */
	public SaveAllCommand() {
		super("save-all");
	}

	@Override
	public void execute(Player player, String[] args) {
		// Should this start a separate thread instead?
		try {
			player.getWorld().getChunks().saveAll();
		} catch (IOException e) {
			logger.log(Level.WARNING, "Failed to save some chunks.", e);
			player.sendMessage("§eFailed to save some chunks, see server log for more details.");
		}
	}

}

