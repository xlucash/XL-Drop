package me.xlucash.xldrop.commands;

import me.xlucash.xldrop.DropMain;
import me.xlucash.xldrop.handlers.DropCommandHandler;
import me.xlucash.xldrop.enums.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles the execution of the "drop" command.
 */
public class DropCommand implements CommandExecutor {
    private final DropMain plugin;
    private final DropCommandHandler dropCommandHandler;
    public DropCommand(DropMain plugin) {
        this.plugin = plugin;
        this.dropCommandHandler = new DropCommandHandler(plugin);
    }

    /**
     * Executes the "drop" command.
     * @param commandSender The sender of the command.
     * @param command       The command being executed.
     * @param label         The alias used for the command.
     * @param args          The arguments provided by the sender.
     * @return True if the command was executed successfully, false otherwise.
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        // Check if the command sender is not a player.
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(Message.COMMAND_ONLY_FOR_PLAYERS.getText());
            return true;
        }

        Player player = (Player) commandSender;
        dropCommandHandler.handleCommand(player, args);

        return true;
    }
}
