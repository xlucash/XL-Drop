package me.xlucash.xldrop.utils;

import me.xlucash.xldrop.DropMain;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages the stone generators for the Drop plugin.
 */
public class GeneratorManager {
    private final DropMain plugin;
    // Maps each generator's location to its associated task ID.
    private Map<Location, Integer> generatorTasks = new HashMap<>();

    public GeneratorManager(DropMain plugin) {
        this.plugin = plugin;
    }

    /**
     * Adds a new stone generator at the specified location and associates it with a player.
     * @param location   The location where the generator is placed.
     * @param playerUUID The UUID of the player who placed the generator.
     */
    public void addGenerator(Location location, UUID playerUUID) {
        plugin.getDatabaseManager().addGenerator(location, playerUUID);
        generateStoneTask(location);
    }

    /**
     * Removes the stone generator at the specified location.
     * @param location The location of the generator to be removed.
     */
    public void removeGenerator(Location location) {
        plugin.getDatabaseManager().removeGenerator(location);
        if (generatorTasks.containsKey(location)) {
            Bukkit.getScheduler().cancelTask(generatorTasks.get(location));
            generatorTasks.remove(location);
        }
    }

    /**
     * Schedules a task to generate a stone block at the specified location after a delay.
     * @param location The location where the stone block should be generated.
     */
    public void generateStoneTask(Location location) {
        int taskId = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            location.getBlock().setType(Material.STONE);
            generatorTasks.remove(location);
        }, 40L).getTaskId();

        generatorTasks.put(location, taskId);
    }
}
