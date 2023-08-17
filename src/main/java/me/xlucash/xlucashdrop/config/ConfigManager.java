package me.xlucash.xlucashdrop.config;

import me.xlucash.xlucashdrop.DropMain;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final DropMain plugin;
    private FileConfiguration config;

    public ConfigManager(DropMain plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public double getDouble(String path) {
        return config.getDouble(path);
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }
}
