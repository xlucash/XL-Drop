package me.xlucash.xlucashdrop.listeners;

import me.xlucash.xlucashdrop.DropMain;
import me.xlucash.xlucashdrop.config.DropConfig;
import me.xlucash.xlucashdrop.hooks.SuperiorSkyblockHook;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Random;

public class BlockBreakListener implements Listener {
    private final DropMain plugin;
    private final DropConfig dropConfig;
    private final SuperiorSkyblockHook superiorSkyblockHook;
    private final Random random = new Random();
    public BlockBreakListener(DropMain plugin, DropConfig dropConfig, SuperiorSkyblockHook superiorSkyblockHook) {
        this.plugin = plugin;
        this.dropConfig = dropConfig;
        this.superiorSkyblockHook = superiorSkyblockHook;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String allowedWorld = dropConfig.getStringForPath("world");

        if (!player.getWorld().getName().equals(allowedWorld)) {
            return;
        }

        if (event.getBlock().getType() != Material.STONE) {
            return;
        }

        if (player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        event.setDropItems(false);

        Material toolType = player.getInventory().getItemInMainHand().getType();
        if (!toolType.name().endsWith("_PICKAXE")) {
            return;
        }

        giveExp(player, dropConfig.getDoubleForPath("stone_exp_drop"));
        if (plugin.getDatabaseManager().isDropEnabled(player.getUniqueId(), "COBBLESTONE")) {
            ItemStack cobbleItem = new ItemStack(Material.COBBLESTONE);
            safelyAddToInventory(player, cobbleItem);
        }

        int fortuneLevel = getFortuneLevel(player.getInventory().getItemInMainHand());

        for (String item : dropConfig.getConfigurationSection("drops")) {
            double chance = dropConfig.getChanceForItem(item);
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
    private void giveExp(Player player, double baseExpAmount) {
        int playerLevel = player.getLevel();
        double adjustedExpAmount = baseExpAmount * (1 + (playerLevel * 0.1));
        player.giveExp((int) adjustedExpAmount);
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
