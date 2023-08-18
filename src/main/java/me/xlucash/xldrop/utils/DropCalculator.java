package me.xlucash.xldrop.utils;

import me.xlucash.xldrop.DropMain;
import me.xlucash.xldrop.config.ConfigManager;
import me.xlucash.xldrop.hooks.SuperiorSkyblockHook;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Random;

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

    public void calculateDrop(Player player) {
        if (plugin.getDatabaseManager().isDropEnabled(player.getUniqueId(), "COBBLESTONE")) {
            ItemStack cobbleItem = new ItemStack(Material.COBBLESTONE);
            safelyAddToInventory(player, cobbleItem);
        }

        int fortuneLevel = getFortuneLevel(player.getInventory().getItemInMainHand());

        for (String item : configManager.getConfigurationSection("drops")) {
            double chance = configManager.getChanceForItem(item);
            chance += superiorSkyblockHook.getDropChanceMultiplier(player);

            if (!plugin.getDatabaseManager().isDropEnabled(player.getUniqueId(), item)) {
                continue;
            }

            if (random.nextDouble() * 100 < chance) {
                int amountToDrop = getDropAmountWithFortune(fortuneLevel);
                ItemStack dropItem = new ItemStack(Material.valueOf(item), amountToDrop);
                safelyAddToInventory(player, dropItem);
            }
        }
    }

    private void safelyAddToInventory(Player player, ItemStack item) {
        HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(item);

        for (ItemStack leftover : leftovers.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), leftover);
        }
    }

    private int getFortuneLevel(ItemStack item) {
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasEnchant(org.bukkit.enchantments.Enchantment.LOOT_BONUS_BLOCKS)) {
            return 0;
        }
        return item.getItemMeta().getEnchantLevel(org.bukkit.enchantments.Enchantment.LOOT_BONUS_BLOCKS);
    }

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
