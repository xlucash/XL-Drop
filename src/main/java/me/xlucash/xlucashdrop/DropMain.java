package me.xlucash.xlucashdrop;

import me.xlucash.xlucashdrop.commands.DropCommand;
import me.xlucash.xlucashdrop.listeners.BlockBreakListener;
import me.xlucash.xlucashdrop.listeners.InventoryClickListener;
import me.xlucash.xlucashdrop.utils.ConfigManager;
import me.xlucash.xlucashdrop.utils.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.command.defaults.ReloadCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class DropMain extends JavaPlugin {
    public static DropMain plugin;
    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    @Override
    public void onEnable() {
        plugin = this;
        databaseManager = new DatabaseManager(this);
        configManager = new ConfigManager(this);

        configManager.loadConfig();

        registerCommands();
        registerEvents();

        getServer().getConsoleSender().sendMessage("[xlucashDROP] Plugin zostal wlaczony!");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("[xlucashDROP] Plugin zostal wylaczony!");
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
    }

    private void registerCommands() {
        getCommand("drop").setExecutor(new DropCommand(this));
    }
}
