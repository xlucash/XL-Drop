package me.xlucash.xldrop;

import me.xlucash.xldrop.commands.DropCommand;
import me.xlucash.xldrop.commands.DropTabCompleter;
import me.xlucash.xldrop.enums.Message;
import me.xlucash.xldrop.hooks.SuperiorSkyblockHook;
import me.xlucash.xldrop.listeners.BlockBreakListener;
import me.xlucash.xldrop.listeners.GeneratorListener;
import me.xlucash.xldrop.listeners.InventoryClickListener;
import me.xlucash.xldrop.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

/**
 * Initializes and sets up the plugin's commands, events, and hooks.
 */
public class PluginInitializer {
    private final DropMain plugin;
    private final ConfigManager configManager;
    private final PluginManager pluginManager;
    private final SuperiorSkyblockHook superiorSkyblockHook;

    /**
     * Constructor for the PluginInitializer.
     *
     * @param plugin Main plugin instance.
     * @param configManager Manages the plugin's configuration.
     * @param superiorSkyblockHook Hook for the SuperiorSkyblock plugin.
     */
    public PluginInitializer(DropMain plugin, ConfigManager configManager, SuperiorSkyblockHook superiorSkyblockHook) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.superiorSkyblockHook = superiorSkyblockHook;
        this.pluginManager = plugin.getServer().getPluginManager();
    }

    /**
     * Registers the plugin's commands.
     */
    public void registerCommands() {
        plugin.getCommand("drop").setExecutor(new DropCommand(plugin));
        plugin.getCommand("drop").setTabCompleter(new DropTabCompleter());
    }

    /**
     * Registers the plugin's event listeners.
     */
    public void registerEvents() {
        pluginManager.registerEvents(new BlockBreakListener(plugin, configManager, superiorSkyblockHook), plugin);
        pluginManager.registerEvents(new InventoryClickListener(plugin, configManager), plugin);
        pluginManager.registerEvents(new GeneratorListener(plugin, configManager), plugin);
    }

    /**
     * Loads and checks for the presence of external plugin hooks, specifically SuperiorSkyblock.
     */
    public void loadHooks() {
        if (Bukkit.getPluginManager().getPlugin("SuperiorSkyblock2") != null) {
            plugin.getLogger().info(Message.SUPERIORSKYBLOCK_HOOKED.getText());
        } else {
            plugin.getLogger().warning(Message.SUPERIORSKYBLOCK_HOOK_FAILED.getText());
        }
    }
}