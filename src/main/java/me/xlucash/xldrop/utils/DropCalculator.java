package me.xlucash.xldrop.utils;

import me.xlucash.xldrop.DropMain;
import me.xlucash.xldrop.config.ConfigManager;
import me.xlucash.xldrop.hooks.SuperiorSkyblockHook;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Random;

/**
 * Calculates and manages the drops for players in the Drop plugin.
 */
public class DropCalculator {
    private final ConfigManager configManager;
    private final SuperiorSkyblockHook superiorSkyblockHook;
    private final DropMain plugin;
    private final Random random = new Random();

    public DropCalculator(ConfigManager configManager, SuperiorSkyblockHook superiorSkyblockHook, DropMain plugin) {
        this.configManager = configManager;
        this.superiorSkyblockHook = superiorSkyblockHook;
        this.plugin = plugin;
    }

    /**
     * Calculates the drops for a player based on various conditions and settings.
     * @param player The player for whom the drops are calculated.
     */
    public void calculateDrop(Player player) {
        // Check if cobblestone drop is enabled for the player
        if (plugin.getDatabaseManager().isDropEnabled(player.getUniqueId(), "COBBLESTONE")) {
            ItemStack cobbleItem = new ItemStack(Material.COBBLESTONE);
            safelyAddToInventory(player, cobbleItem);
        }

        int fortuneLevel = getFortuneLevel(player.getInventory().getItemInMainHand());

        // Iterate through all configured drops
        for (String item : configManager.getConfigurationSection("drops")) {
            double chance = configManager.getChanceForItem(item) + superiorSkyblockHook.getDropChanceMultiplier(player);

            // Skip if the drop is not enabled for the player
            if (!plugin.getDatabaseManager().isDropEnabled(player.getUniqueId(), item)) {
                continue;
            }

            // Determine if a drop should occur based on the calculated chance
            if (random.nextDouble() * 100 < chance) {
                int amountToDrop = getDropAmountWithFortune(fortuneLevel);
                ItemStack dropItem = new ItemStack(Material.valueOf(item), amountToDrop);
                safelyAddToInventory(player, dropItem);
            }
        }
    }

    /**
     * Safely adds an item to a player's inventory. If the inventory is full, the item is dropped at the player's location.
     * @param player The player to whom the item should be given.
     * @param item The item to be added to the inventory.
     */
    private void safelyAddToInventory(Player player, ItemStack item) {
        HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(item);

        for (ItemStack leftover : leftovers.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), leftover);
        }
    }

    /**
     * Retrieves the fortune enchantment level of an item.
     * @param item The item to check.
     * @return The level of the fortune enchantment, or 0 if not present.
     */
    private int getFortuneLevel(ItemStack item) {
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasEnchant(Enchantment.LOOT_BONUS_BLOCKS)) {
            return 0;
        }
        return item.getItemMeta().getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS);
    }

    /**
     * Calculates the amount of an item to drop based on the fortune level.
     * @param fortuneLevel The level of the fortune enchantment.
     * @return The amount of the item to drop.
     */
    private int getDropAmountWithFortune(int fortuneLevel) {
        int baseAmount = 1;
        if (fortuneLevel == 0) return baseAmount;

        double roll = random.nextDouble();
        switch (fortuneLevel) {
            case 1:
                if (roll < 0.2) return baseAmount + 1; // 20% for 1 additional item
                break;
            case 2:
                if (roll < 0.15) return baseAmount + 2; // 15% for 2 additional items
                else if (roll < 0.3) return baseAmount + 1; // 15% for 1 additional item
                break;
            case 3:
                if (roll < 0.1) return baseAmount + 3; // 10% for 3 additional items
                else if (roll < 0.25) return baseAmount + 2; // 15% for 2 additional items
                else if (roll < 0.4) return baseAmount + 1; // 15% for 1 additional item
                break;
        }
        return baseAmount;
    }
}
