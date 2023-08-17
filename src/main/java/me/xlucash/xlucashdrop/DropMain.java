package me.xlucash.xlucashdrop;

import me.xlucash.xlucashdrop.config.DropConfig;
import me.xlucash.xlucashdrop.enums.Message;
import me.xlucash.xlucashdrop.config.ConfigManager;
import me.xlucash.xlucashdrop.database.DatabaseManager;
import me.xlucash.xlucashdrop.hooks.SuperiorSkyblockHook;
import me.xlucash.xlucashdrop.utils.RecipeManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class DropMain extends JavaPlugin {
    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private RecipeManager recipeManager;
    private PluginInitializer pluginInitializer;
    private DropConfig dropConfig;
    private SuperiorSkyblockHook superiorSkyblockHook;

    @Override
    public void onEnable() {
        databaseManager = new DatabaseManager(this);
        configManager = new ConfigManager(this);
        recipeManager = new RecipeManager(this);
        dropConfig = new DropConfig(this);
        superiorSkyblockHook = new SuperiorSkyblockHook(this, dropConfig);

        configManager.loadConfig();
        recipeManager.registerRecipes();

        pluginInitializer = new PluginInitializer(this, configManager, dropConfig, superiorSkyblockHook);
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
