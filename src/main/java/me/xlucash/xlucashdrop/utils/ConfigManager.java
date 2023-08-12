package me.xlucash.xlucashdrop.utils;

import me.xlucash.xlucashdrop.DropMain;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final DropMain plugin;
    private FileConfiguration config;

    public ConfigManager(DropMain plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        initConfig();
    }
    private void initConfig() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }
}
