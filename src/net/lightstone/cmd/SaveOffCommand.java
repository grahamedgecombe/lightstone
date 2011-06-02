package net.lightstone.cmd;

import net.lightstone.model.Player;


/**
 * A command that turns automatic saving of chunks off.
 * @author Zhuowei Zhang
 */
public final class SaveOffCommand extends Command {

	/**
	 * Creates the {@code /save-off} command.
	 */
	public SaveOffCommand() {
		super("save-off");
	}

	@Override
	public void execute(Player player, String[] args) {
		player.getSession().getServer().setSaveEnabled(false);
	}

}

