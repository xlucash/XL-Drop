package me.xlucash.xlucashdrop;

import me.xlucash.xlucashdrop.commands.DropCommand;
import me.xlucash.xlucashdrop.commands.DropTabCompleter;
import me.xlucash.xlucashdrop.config.DropConfig;
import me.xlucash.xlucashdrop.enums.Message;
import me.xlucash.xlucashdrop.hooks.SuperiorSkyblockHook;
import me.xlucash.xlucashdrop.listeners.BlockBreakListener;
import me.xlucash.xlucashdrop.listeners.GeneratorListener;
import me.xlucash.xlucashdrop.listeners.InventoryClickListener;
import me.xlucash.xlucashdrop.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class PluginInitializer {
    private final DropMain plugin;
    private final ConfigManager configManager;
    private final PluginManager pluginManager;
    private final DropConfig dropConfig;
    private final SuperiorSkyblockHook superiorSkyblockHook;

    public PluginInitializer(DropMain plugin, ConfigManager configManager, DropConfig dropConfig, SuperiorSkyblockHook superiorSkyblockHook) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.dropConfig = dropConfig;
        this.superiorSkyblockHook = superiorSkyblockHook;
        this.pluginManager = plugin.getServer().getPluginManager();
    }

    public void registerCommands() {
        plugin.getCommand("drop").setExecutor(new DropCommand(plugin));
        plugin.getCommand("drop").setTabCompleter(new DropTabCompleter());
    }

    public void registerEvents() {
        pluginManager.registerEvents(new BlockBreakListener(plugin, dropConfig, superiorSkyblockHook), plugin);
        pluginManager.registerEvents(new InventoryClickListener(plugin, dropConfig), plugin);
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