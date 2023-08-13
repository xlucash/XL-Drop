package me.xlucash.xlucashdrop.listeners;

import me.xlucash.xlucashdrop.DropMain;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class BlockBreakListener implements Listener {
    private final DropMain plugin;
    private final Random random = new Random();
    public BlockBreakListener(DropMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String allowedWorld = plugin.getConfig().getString("world");

        if (!player.getWorld().getName().equals(allowedWorld)) {
            return;
        }

        if (event.getBlock().getType() != Material.STONE) {
            return;
        }

        if (player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        Material toolType = player.getInventory().getItemInMainHand().getType();
        if (!toolType.name().endsWith("_PICKAXE")) {
            return;
        }

        giveExp(player, plugin.getConfig().getDouble("stone_exp_drop"));

        for (String item : plugin.getConfig().getConfigurationSection("drops").getKeys(false)) {
            double chance = plugin.getConfig().getDouble("drops." + item + ".chance");

            if (!plugin.getDatabaseManager().isDropEnabled(player.getUniqueId(), item)) {
                continue;
            }

            if (random.nextDouble() * 100 < chance) {
                player.getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.valueOf(item)));
            }
        }
    }

    private void giveExp(Player player, double expAmount) {
        player.giveExp((int) expAmount);
    }
}
