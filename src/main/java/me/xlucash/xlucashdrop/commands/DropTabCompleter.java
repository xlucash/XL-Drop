package me.xlucash.xlucashdrop.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DropTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        List<String> list = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("drop")) {
            if(commandSender instanceof Player) {
                Player player = (Player) commandSender;
                if (args.length == 1) {
                    if (player.hasPermission("drop.reload")) {
                        list.add("reload");
                    }
                }
            }
        }
        return list;
    }
}
