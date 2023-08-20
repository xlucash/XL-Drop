package me.xlucash.xldrop.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides tab completion suggestions for the "drop" command.
 */
public class DropTabCompleter implements TabCompleter {
    /**
     * Returns a list of tab completion suggestions based on the given command and arguments.
     * @param commandSender The sender of the command.
     * @param command       The command being executed.
     * @param label         The alias used for the command.
     * @param args          The arguments provided by the sender.
     * @return A list of tab completion suggestions.
     */
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if(isDropCommand(command) && senderIsPlayerWithPermission(commandSender, "drop.reload")) {
            suggestions.add("reload");
        }

        return suggestions;
    }

    /**
     * Checks if the given command is the "drop" command.
     * @param command The command to check.
     * @return True if the command is "drop", false otherwise.
     */
    private boolean isDropCommand(Command command) {
        return command.getName().equalsIgnoreCase("drop");
    }

    /**
     * Checks if the command sender is a player with the specified permission.
     * @param sender     The command sender to check.
     * @param permission The permission to check for.
     * @return True if the sender is a player with the given permission, false otherwise.
     */
    private boolean senderIsPlayerWithPermission(CommandSender sender, String permission) {
        return sender instanceof Player && sender.hasPermission(permission);
    }
}
