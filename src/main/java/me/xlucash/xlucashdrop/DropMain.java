package me.xlucash.xlucashdrop;

import me.xlucash.xlucashdrop.commands.DropCommand;
import me.xlucash.xlucashdrop.commands.DropTabCompleter;
import me.xlucash.xlucashdrop.listeners.BlockBreakListener;
import me.xlucash.xlucashdrop.listeners.GeneratorListener;
import me.xlucash.xlucashdrop.listeners.InventoryClickListener;
import me.xlucash.xlucashdrop.utils.ConfigManager;
import me.xlucash.xlucashdrop.utils.DatabaseManager;
import me.xlucash.xlucashdrop.utils.RecipeManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class DropMain extends JavaPlugin {
    public static DropMain plugin;
    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private RecipeManager recipeManager;

    public static final String GENERATOR_NAME = ChatColor.GOLD + "Generator Stone";
    @Override
    public void onEnable() {
        plugin = this;
        databaseManager = new DatabaseManager(this);
        configManager = new ConfigManager(this);
        recipeManager = new RecipeManager(this);

        configManager.loadConfig();
        recipeManager.registerRecipes();

        registerCommands();
        registerEvents();

        getServer().getConsoleSender().sendMessage("[xlucashDrop] Plugin zostal wlaczony!");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("[xlucashDrop] Plugin zostal wylaczony!");
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        getServer().getPluginManager().registerEvents(new GeneratorListener(this), this);
    }

    private void registerCommands() {
        getCommand("drop").setExecutor(new DropCommand(this, configManager));
        getCommand("drop").setTabCompleter(new DropTabCompleter());
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
