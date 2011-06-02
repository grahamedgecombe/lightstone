package net.lightstone.cmd;

import net.lightstone.model.Player;


/**
 * A command that turns on automatic chunk saving.
 * @author Zhuowei Zhang
 */
public final class SaveOnCommand extends Command {

	/**
	 * Creates the {@code /save-on} command.
	 */
	public SaveOnCommand() {
		super("save-on");
	}

	@Override
	public void execute(Player player, String[] args) {
		player.getSession().getServer().setSaveEnabled(true);
	}

}

