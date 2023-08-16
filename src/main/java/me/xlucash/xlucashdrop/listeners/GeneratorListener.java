package me.xlucash.xlucashdrop.listeners;

import me.xlucash.xlucashdrop.DropMain;
import me.xlucash.xlucashdrop.enums.Message;
import me.xlucash.xlucashdrop.utils.RecipeManager;
import org.bukkit.Bukkit;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneratorListener implements Listener {
    private final DropMain plugin;
    private Map<Location, Integer> generatorTasks = new HashMap<>();

    public GeneratorListener(DropMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerStart(ServerLoadEvent event) {
        List<Location> generatorLocations = plugin.getDatabaseManager().getAllGeneratorLocations();

        for (Location location : generatorLocations) {
            if (location.getBlock().getType() != Material.STONE) {
                location.getBlock().setType(Material.STONE);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType() != Material.END_STONE) {
            return;
        }

        ItemStack item = event.getItemInHand();
        if (isGenerator(item)) {
            plugin.getDatabaseManager().addGenerator(event.getBlock().getLocation(), event.getPlayer().getUniqueId());

            event.getBlock().setType(Material.STONE);
            event.getPlayer().sendMessage(Message.GENERATOR_PLACED.getText());

            generateStoneTask(event.getBlock().getLocation());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (plugin.getDatabaseManager().isGenerator(event.getBlock().getLocation())) {
            Player player = event.getPlayer();
            if (event.getBlock().getType() == Material.STONE
                    && player.getInventory().getItemInMainHand().getType() == Material.GOLDEN_PICKAXE) {
                plugin.getDatabaseManager().removeGenerator(event.getBlock().getLocation());
                player.getInventory().addItem(RecipeManager.getGeneratorItem());
                player.sendMessage(Message.GENERATOR_DESTROYED.getText());
                if (generatorTasks.containsKey(event.getBlock().getLocation())) {
                    Bukkit.getScheduler().cancelTask(generatorTasks.get(event.getBlock().getLocation()));
                    generatorTasks.remove(event.getBlock().getLocation());
                }
            } else if (event.getBlock().getType() == Material.STONE) {
                generateStoneTask(event.getBlock().getLocation());
            }
        }
    }

    private void generateStoneTask(Location location) {
        int taskId = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            location.getBlock().setType(Material.STONE);
            generatorTasks.remove(location);
        }, 40L).getTaskId();

        generatorTasks.put(location, taskId);
    }

    @EventHandler
    public void onBlockCanBuild(BlockCanBuildEvent event) {
        if (plugin.getDatabaseManager().isGenerator(event.getBlock().getLocation())) {
            event.getPlayer().sendMessage(Message.GENERATOR_CANNOT_BUILD.getText());
            event.setBuildable(false);
        }
    }

    private boolean isGenerator(ItemStack item) {
        return item != null && item.getType() == Material.END_STONE && item.hasItemMeta()
                && Message.GENERATOR_NAME.getText().equals(item.getItemMeta().getDisplayName());
    }
}
