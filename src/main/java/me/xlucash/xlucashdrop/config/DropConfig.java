package me.xlucash.xlucashdrop.config;

import me.xlucash.xlucashdrop.DropMain;

import java.util.Set;

public class DropConfig {
    private final DropMain plugin;

    public DropConfig(DropMain plugin) {
        this.plugin = plugin;
    }

    public double getChanceForItem(String itemKey) {
        return plugin.getConfig().getDouble("drops." + itemKey + ".chance");
    }

    public double getDoubleForPath(String pathKey) {
        return plugin.getConfig().getDouble(pathKey);
    }

    public int getIntForPath(String pathKey) {
        return plugin.getConfig().getInt(pathKey);
    }

    public String getStringForPath(String pathKey) {
        return plugin.getConfig().getString(pathKey);
    }

    public Set<String> getConfigurationSection(String pathKey) {
        return plugin.getConfig().getConfigurationSection(pathKey).getKeys(false);
    }

    public int getSlotForItem(String itemKey) {
        return plugin.getConfig().getInt("drops." + itemKey + ".slot");
    }

    public String getDisplayNameForItem(String itemKey) {
        return plugin.getConfig().getString("drops." + itemKey + ".displayName");
    }
}
