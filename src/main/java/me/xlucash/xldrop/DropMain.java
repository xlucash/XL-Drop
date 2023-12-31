package me.xlucash.xldrop;

import me.xlucash.xldrop.config.ConfigManager;
import me.xlucash.xldrop.database.DatabaseManager;
import me.xlucash.xldrop.enums.Message;
import me.xlucash.xldrop.hooks.SuperiorSkyblockHook;
import me.xlucash.xldrop.utils.RecipeManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class for the Drop plugin. Handles the plugin's lifecycle events.
 */
public final class DropMain extends JavaPlugin {
    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private RecipeManager recipeManager;
    private PluginInitializer pluginInitializer;
    private SuperiorSkyblockHook superiorSkyblockHook;

    /**
     * Called when the plugin is enabled. Initializes and sets up the plugin's components.
     */
    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        recipeManager = new RecipeManager(this);
        superiorSkyblockHook = new SuperiorSkyblockHook(this, configManager);

        configManager.loadConfig();
        recipeManager.registerRecipes();

        databaseManager = new DatabaseManager(this);

        pluginInitializer = new PluginInitializer(this, configManager, superiorSkyblockHook);
        pluginInitializer.registerCommands();
        pluginInitializer.registerEvents();
        pluginInitializer.loadHooks();

        getLogger().info(Message.PLUGIN_ENABLED.getText());
    }

    /**
     * Called when the plugin is disabled. Cleans up resources and connections.
     */
    @Override
    public void onDisable() {
        databaseManager.disconnect();
        getLogger().info(Message.PLUGIN_DISABLED.getText());
    }

    /**
     * Getter for the DatabaseManager instance.
     * @return the DatabaseManager instance.
     */
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
