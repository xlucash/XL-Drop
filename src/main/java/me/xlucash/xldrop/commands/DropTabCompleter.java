package me.xlucash.xldrop.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DropTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if(isDropCommand(command) && senderIsPlayerWithPermission(commandSender, "drop.reload")) {
            suggestions.add("reload");
        }

        return suggestions;
    }

    private boolean isDropCommand(Command command) {
        return command.getName().equalsIgnoreCase("drop");
    }

    private boolean senderIsPlayerWithPermission(CommandSender sender, String permission) {
        return sender instanceof Player && sender.hasPermission(permission);
    }
}
