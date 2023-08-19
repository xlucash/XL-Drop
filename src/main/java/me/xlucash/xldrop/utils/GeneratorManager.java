package me.xlucash.xldrop.utils;

import me.xlucash.xldrop.DropMain;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GeneratorManager {
    private final DropMain plugin;
    private Map<Location, Integer> generatorTasks = new HashMap<>();

    public GeneratorManager(DropMain plugin) {
        this.plugin = plugin;
    }

    public void addGenerator(Location location, UUID playerUUID) {
        plugin.getDatabaseManager().addGenerator(location, playerUUID);
        generateStoneTask(location);
    }

    public void removeGenerator(Location location) {
        plugin.getDatabaseManager().removeGenerator(location);
        if (generatorTasks.containsKey(location)) {
            Bukkit.getScheduler().cancelTask(generatorTasks.get(location));
            generatorTasks.remove(location);
        }
    }

    public void generateStoneTask(Location location) {
        int taskId = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            location.getBlock().setType(Material.STONE);
            generatorTasks.remove(location);
        }, 40L).getTaskId();

        generatorTasks.put(location, taskId);
    }
}
