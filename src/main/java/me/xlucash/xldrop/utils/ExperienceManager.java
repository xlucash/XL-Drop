package me.xlucash.xldrop.utils;

import me.xlucash.xldrop.config.ConfigManager;
import org.bukkit.entity.Player;

public class ExperienceManager {
    private final ConfigManager configManager;

    public ExperienceManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public void giveExperience(Player player) {
        int playerLevel = player.getLevel();
        double baseExpAmount = configManager.getDoubleForPath("stone_exp_drop");
        double adjustedExpAmount = baseExpAmount * (1 + (playerLevel * 0.1));
        player.giveExp((int) adjustedExpAmount);
    }
}
