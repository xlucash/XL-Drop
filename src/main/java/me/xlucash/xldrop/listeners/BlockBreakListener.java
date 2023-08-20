package me.xlucash.xldrop.listeners;

import me.xlucash.xldrop.DropMain;
import me.xlucash.xldrop.config.ConfigManager;
import me.xlucash.xldrop.hooks.SuperiorSkyblockHook;
import me.xlucash.xldrop.utils.DropCalculator;
import me.xlucash.xldrop.utils.ExperienceManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Listener responsible for handling block break events.
 * It checks if the broken block is a stone in the allowed world and awards experience and drops accordingly.
 */
public class BlockBreakListener implements Listener {
    private final DropMain plugin;
    private final ConfigManager configManager;
    private final SuperiorSkyblockHook superiorSkyblockHook;
    private final DropCalculator dropCalculator;
    private final ExperienceManager experienceManager;
    public BlockBreakListener(DropMain plugin, ConfigManager configManager, SuperiorSkyblockHook superiorSkyblockHook) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.superiorSkyblockHook = superiorSkyblockHook;
        this.dropCalculator = new DropCalculator(configManager, superiorSkyblockHook, plugin);
        this.experienceManager = new ExperienceManager(configManager);
    }

    /**
     * Handles the event when a block is broken.
     * Awards experience and calculates drops if the broken block is a stone in the allowed world.
     * @param event The block break event.
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String allowedWorld = configManager.getStringForPath("world");

        if (!player.getWorld().getName().equals(allowedWorld)) {
            return;
        }

        if(!superiorSkyblockHook.checkPlayerPrivilege(player, event.getBlock().getLocation(), "BREAK")) {
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

        experienceManager.giveExperience(player);
        dropCalculator.calculateDrop(player);
    }
}
