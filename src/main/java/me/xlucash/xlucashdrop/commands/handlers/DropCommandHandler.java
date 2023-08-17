package me.xlucash.xlucashdrop.commands.handlers;

import me.xlucash.xlucashdrop.DropMain;
import me.xlucash.xlucashdrop.config.DropConfig;
import me.xlucash.xlucashdrop.enums.Message;
import me.xlucash.xlucashdrop.guis.DropGui;
import me.xlucash.xlucashdrop.guis.items.DropItemProvider;
import me.xlucash.xlucashdrop.guis.slots.DropSlotManager;
import me.xlucash.xlucashdrop.guis.updaters.DropGuiUpdater;
import me.xlucash.xlucashdrop.config.ConfigManager;
import me.xlucash.xlucashdrop.hooks.SuperiorSkyblockHook;
import me.xlucash.xlucashdrop.utils.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DropCommandHandler {
    private final DropMain plugin;
    private final DropItemProvider itemFactory;
    private final DropSlotManager slotManager;
    private final PermissionManager permissionManager;
    private final DropGuiUpdater dropGuiUpdater;
    private final ConfigManager configManager;
    private final DropConfig dropConfig;
    private final SuperiorSkyblockHook superiorSkyblockHook;

    public DropCommandHandler(DropMain plugin) {
        this.plugin = plugin;
        this.permissionManager = new PermissionManager();
        this.configManager = new ConfigManager(plugin);
        this.slotManager = new DropSlotManager();
        this.dropConfig = new DropConfig(plugin);
        this.superiorSkyblockHook = new SuperiorSkyblockHook(plugin, this.dropConfig);
        this.dropGuiUpdater = new DropGuiUpdater(plugin, this.dropConfig);
        this.itemFactory = new DropItemProvider(plugin, this.dropConfig, this.superiorSkyblockHook);
    }

    public void handleCommand(Player player, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (!permissionManager.hasPermission(player, "drop.reload")) {
                player.sendMessage(Message.NO_PERMISSION_RELOAD.getText());
                return;
            }
            player.sendMessage(Message.PLUGIN_RELOADED_PLAYER.getText());
            Bukkit.getServer().getConsoleSender().sendMessage(Message.PLUGIN_RELOADED_CONSOLE.getText());
            configManager.reloadConfig();
        } else {
            DropGui dropGui = new DropGui(plugin, itemFactory, slotManager, dropConfig);
            dropGui.open(player);
            dropGuiUpdater.updateGuiForPlayer(player, dropGui);
        }
    }
}
