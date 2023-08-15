package me.xlucash.xlucashdrop.commands;

import me.xlucash.xlucashdrop.DropMain;
import me.xlucash.xlucashdrop.guis.DropGUI;
import me.xlucash.xlucashdrop.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DropCommand implements CommandExecutor {
    private final DropMain plugin;
    private final ConfigManager configManager;
    public DropCommand(DropMain plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
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
            configManager.reloadConfig();
        } else {
            DropGUI dropGui = new DropGUI(plugin);
            dropGui.open(player);
            updateGuiForPlayer(player, dropGui);
        }

        return true;
    }


    private void updateGuiForPlayer(Player player, DropGUI dropGui) {
        for (String item : plugin.getConfig().getConfigurationSection("drops").getKeys(false)) {
            ItemStack guiItem = dropGui.getInventory().getItem(plugin.getConfig().getInt("drops." + item + ".slot"));
            if (guiItem != null && guiItem.getType().name().equals(item)) {
                ItemMeta meta = guiItem.getItemMeta();
                List<String> lore = meta.getLore();
                if (lore == null) lore = new ArrayList<>();
                if (lore.size() == 1) {
                    lore.add(plugin.getDatabaseManager().isDropEnabled(player.getUniqueId(), item) ? "§7Drop: §aWłączony" : "§7Drop: §cWyłączony");
                } else if (lore.size() > 1) {
                    lore.set(1, plugin.getDatabaseManager().isDropEnabled(player.getUniqueId(), item) ? "§7Drop: §aWłączony" : "§7Drop: §cWyłączony");
                }
                meta.setLore(lore);
                guiItem.setItemMeta(meta);
            }
        }
    }

}
