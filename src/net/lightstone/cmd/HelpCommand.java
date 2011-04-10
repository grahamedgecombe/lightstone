package net.lightstone.cmd;

import net.lightstone.model.Player;

/**
 * A command that displays help on using other commands.
 * @author Graham Edgecombe
 */
public final class HelpCommand extends Command {

	/**
	 * The command manager.
	 */
	private final CommandManager manager;

	/**
	 * Creates the {@code /help} command.
	 * @param manager The command manager.
	 */
	public HelpCommand(CommandManager manager) {
		super("help");
		this.manager = manager;
	}

	@Override
	public void execute(Player player, String[] args) {
		StringBuilder builder = new StringBuilder();
		builder.append("§eCommands:");

		for (Command command : manager.getCommands()) {
			builder.append(" /");
			builder.append(command.getCommand());
		}

		player.sendMessage(builder.toString());
	}

}

