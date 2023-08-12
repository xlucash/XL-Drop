package me.xlucash.xlucashdrop.commands;

import me.xlucash.xlucashdrop.DropMain;
import me.xlucash.xlucashdrop.utils.DropGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DropCommand implements CommandExecutor {

    private final DropMain plugin;
    public DropCommand(DropMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage("Tylko gracze moga uzywac tej komendy!");
            return true;
        }

        Player player = (Player) commandSender;
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (!player.hasPermission("drop.reload")) {
                player.sendMessage("Nie masz uprawnien do przeladowania pluginu!");
                return true;
            }
            player.sendMessage("Plugin zostal przeladowany!");
            Bukkit.getServer().getConsoleSender().sendMessage("[xlucashDROP] Plugin zostal przeladowany!");
            plugin.reloadConfig();
        } else {
            new DropGUI(plugin).open(player);
        }

        return true;
    }

}
