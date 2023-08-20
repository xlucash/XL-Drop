package me.xlucash.xldrop.handlers;

import me.xlucash.xldrop.DropMain;
import me.xlucash.xldrop.enums.Message;
import me.xlucash.xldrop.guis.DropGui;
import me.xlucash.xldrop.guis.items.DropItemProvider;
import me.xlucash.xldrop.guis.slots.DropSlotManager;
import me.xlucash.xldrop.guis.updaters.DropGuiUpdater;
import me.xlucash.xldrop.config.ConfigManager;
import me.xlucash.xldrop.hooks.SuperiorSkyblockHook;
import me.xlucash.xldrop.utils.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Handles the execution of the "drop" command and its subcommands.
 */
public class DropCommandHandler {
    private final DropMain plugin;
    private final DropItemProvider itemFactory;
    private final DropSlotManager slotManager;
    private final PermissionManager permissionManager;
    private final DropGuiUpdater dropGuiUpdater;
    private final ConfigManager configManager;
    private final SuperiorSkyblockHook superiorSkyblockHook;

    public DropCommandHandler(DropMain plugin) {
        this.plugin = plugin;
        this.permissionManager = new PermissionManager();
        this.configManager = new ConfigManager(plugin);
        this.slotManager = new DropSlotManager();
        this.superiorSkyblockHook = new SuperiorSkyblockHook(plugin, this.configManager);
        this.dropGuiUpdater = new DropGuiUpdater(plugin, this.configManager);
        this.itemFactory = new DropItemProvider(plugin, this.configManager, this.superiorSkyblockHook);
    }

    /**
     * Handles the execution of the "drop" command and its subcommands.
     * @param player The player executing the command.
     * @param args The arguments provided with the command.
     */
    public void handleCommand(Player player, String[] args) {
        // Check if the "reload" subcommand is used.
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            // Check if the player has the required permission.
            if (!permissionManager.hasPermission(player, "drop.reload")) {
                player.sendMessage(Message.NO_PERMISSION_RELOAD.getText());
                return;
            }
            player.sendMessage(Message.PLUGIN_RELOADED_PLAYER.getText());
            Bukkit.getServer().getConsoleSender().sendMessage(Message.PLUGIN_RELOADED_CONSOLE.getText());
            configManager.reloadConfig();
        } else {
            // Otherwise open the drop GUI for the player.
            DropGui dropGui = new DropGui(plugin, itemFactory, slotManager, configManager);
            dropGui.open(player);
            dropGuiUpdater.updateGuiForPlayer(player, dropGui);
        }
    }
}
