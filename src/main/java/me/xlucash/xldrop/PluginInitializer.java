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

public class PluginInitializer {
    private final DropMain plugin;
    private final ConfigManager configManager;
    private final PluginManager pluginManager;
    private final SuperiorSkyblockHook superiorSkyblockHook;

    public PluginInitializer(DropMain plugin, ConfigManager configManager, SuperiorSkyblockHook superiorSkyblockHook) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.superiorSkyblockHook = superiorSkyblockHook;
        this.pluginManager = plugin.getServer().getPluginManager();
    }

    public void registerCommands() {
        plugin.getCommand("drop").setExecutor(new DropCommand(plugin));
        plugin.getCommand("drop").setTabCompleter(new DropTabCompleter());
    }

    public void registerEvents() {
        pluginManager.registerEvents(new BlockBreakListener(plugin, configManager, superiorSkyblockHook), plugin);
        pluginManager.registerEvents(new InventoryClickListener(plugin, configManager), plugin);
        pluginManager.registerEvents(new GeneratorListener(plugin), plugin);
    }

    public void loadHooks() {
        if (Bukkit.getPluginManager().getPlugin("SuperiorSkyblock2") != null) {
            plugin.getLogger().info(Message.SUPERIORSKYBLOCK_HOOKED.getText());
        } else {
            plugin.getLogger().warning(Message.SUPERIORSKYBLOCK_HOOK_FAILED.getText());
        }
    }
}