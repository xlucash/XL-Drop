package me.xlucash.xldrop.listeners;

import me.xlucash.xldrop.DropMain;
import me.xlucash.xldrop.config.ConfigManager;
import me.xlucash.xldrop.enums.Message;
import me.xlucash.xldrop.hooks.SuperiorSkyblockHook;
import me.xlucash.xldrop.utils.GeneratorManager;
import me.xlucash.xldrop.utils.RecipeManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Listener responsible for handling events related to the stone generator.
 */
public class GeneratorListener implements Listener {
    private final DropMain plugin;
    private final GeneratorManager generatorManager;
    private final SuperiorSkyblockHook superiorSkyblockHook;
    private final ConfigManager configManager;

    public GeneratorListener(DropMain plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.generatorManager = new GeneratorManager(plugin);
        this.configManager = configManager;
        superiorSkyblockHook = new SuperiorSkyblockHook(plugin, configManager);
    }

    /**
     * Ensures that all registered generator locations have a stone block on server start.
     * @param event The server load event.
     */
    @EventHandler
    public void onServerStart(ServerLoadEvent event) {
        List<Location> generatorLocations = plugin.getDatabaseManager().getAllGeneratorLocations();
        for (Location location : generatorLocations) {
            if (location.getBlock().getType() != Material.STONE) {
                location.getBlock().setType(Material.STONE);
            }
        }
    }

    /**
     * Handles the event when a block is placed.
     * Checks if the block is a generator and handles its placement accordingly.
     * @param event The block place event.
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Location location = event.getBlock().getLocation();
        String allowedWorld = plugin.getConfig().getString("world");

        if (isGenerator(event.getItemInHand())) {
            if (!location.getWorld().getName().equalsIgnoreCase(allowedWorld)) {
                player.sendMessage(Message.GENERATOR_PREVENT_PLACE.getText());
                event.setCancelled(true);
                return;
            }

            if (!superiorSkyblockHook.checkPlayerPrivilege(player, location, "BUILD")) {
                player.sendMessage(Message.GENERATOR_PREVENT_PLACE.getText());
                event.setCancelled(true);
                return;
            }

            generatorManager.addGenerator(event.getBlock().getLocation(), event.getPlayer().getUniqueId());
            event.getBlock().setType(Material.STONE);
            event.getPlayer().sendMessage(Message.GENERATOR_PLACED.getText());
        }
    }

    /**
     * Handles the event when a block is broken.
     * Checks if the block is a generator and handles its removal accordingly.
     * @param event The block break event.
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (plugin.getDatabaseManager().isGenerator(event.getBlock().getLocation())) {
            Player player = event.getPlayer();
            if (event.getBlock().getType() == Material.STONE
                    && player.getInventory().getItemInMainHand().getType() == Material.GOLDEN_PICKAXE) {
                if(!superiorSkyblockHook.checkPlayerPrivilege(player, event.getBlock().getLocation(), "BREAK")) {
                    return;
                }
                generatorManager.removeGenerator(event.getBlock().getLocation());
                player.getInventory().addItem(RecipeManager.getGeneratorItem());
                player.sendMessage(Message.GENERATOR_DESTROYED.getText());
            } else if (event.getBlock().getType() == Material.STONE) {
                generatorManager.generateStoneTask(event.getBlock().getLocation());
            }
        }
    }

    /**
     * Ensures that players cannot build in the location of generator.
     * @param event The block can build event.
     */
    @EventHandler
    public void onBlockCanBuild(BlockCanBuildEvent event) {
        if (plugin.getDatabaseManager().isGenerator(event.getBlock().getLocation())) {
            event.getPlayer().sendMessage(Message.GENERATOR_CANNOT_BUILD.getText());
            event.setBuildable(false);
        }
    }

    /**
     * Checks if the provided item is a generator.
     * @param item The item to check.
     * @return True if the item is a generator, false otherwise.
     */
    private boolean isGenerator(ItemStack item) {
        return item != null && item.getType() == Material.END_STONE && item.hasItemMeta()
                && Message.GENERATOR_NAME.getText().equals(item.getItemMeta().getDisplayName());
    }
}
