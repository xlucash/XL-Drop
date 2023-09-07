package me.xlucash.xldrop.utils;

import me.xlucash.xldrop.config.ConfigManager;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Manages the experience points given to players in the Drop plugin.
 */
public class ExperienceManager {
    private final ConfigManager configManager;

    public ExperienceManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    /**
     * Gives experience to the player based on their level and the configured base experience amount.
     * The experience given is adjusted based on the player's level.
     * @param player The player to whom the experience should be given.
     */
    public void giveExperience(Player player) {
        int playerLevel = player.getLevel();
        double baseExpAmount = configManager.getDoubleForPath("stone_exp_drop");
        double adjustedExpAmount = baseExpAmount * (1 + (playerLevel * 0.1));

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.containsEnchantment(Enchantment.MENDING) && itemInHand.getDurability() > 0) {
            // Repair pickaxe with certain amount of exp
            double repairAmount = Math.min(adjustedExpAmount, itemInHand.getType().getMaxDurability() - itemInHand.getDurability()) / 2;
            itemInHand.setDurability((short) (itemInHand.getDurability() - repairAmount));
            adjustedExpAmount -= repairAmount;
        }

        player.giveExp((int) adjustedExpAmount);
    }
}
