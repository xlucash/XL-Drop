package me.xlucash.xldrop;

import me.xlucash.xldrop.config.ConfigManager;
import me.xlucash.xldrop.database.DatabaseManager;
import me.xlucash.xldrop.enums.Message;
import me.xlucash.xldrop.hooks.SuperiorSkyblockHook;
import me.xlucash.xldrop.utils.RecipeManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class DropMain extends JavaPlugin {
    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private RecipeManager recipeManager;
    private PluginInitializer pluginInitializer;
    private SuperiorSkyblockHook superiorSkyblockHook;

    @Override
    public void onEnable() {
        databaseManager = new DatabaseManager(this);
        configManager = new ConfigManager(this);
        recipeManager = new RecipeManager(this);
        superiorSkyblockHook = new SuperiorSkyblockHook(this, configManager);

        configManager.loadConfig();
        recipeManager.registerRecipes();

        pluginInitializer = new PluginInitializer(this, configManager, superiorSkyblockHook);
        pluginInitializer.registerCommands();
        pluginInitializer.registerEvents();
        pluginInitializer.loadHooks();

        getLogger().info(Message.PLUGIN_ENABLED.getText());
    }

    @Override
    public void onDisable() {
        databaseManager.disconnect();
        getLogger().info(Message.PLUGIN_DISABLED.getText());
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
