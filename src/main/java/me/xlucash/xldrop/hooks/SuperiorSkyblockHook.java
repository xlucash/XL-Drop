package me.xlucash.xldrop.hooks;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import me.xlucash.xldrop.DropMain;
import me.xlucash.xldrop.config.ConfigManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Provides integration with the SuperiorSkyblock plugin.
 * Allows for retrieval of island information and checks related to player privileges on islands.
 */
public class SuperiorSkyblockHook {

    private final DropMain plugin;
    private final ConfigManager configManager;

    public SuperiorSkyblockHook(DropMain plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    /**
     * Retrieves the island associated with the given player.
     * @param player The player whose island is to be fetched.
     * @return The island of the player or null if the player doesn't have an island.
     */
    private Island getIsland(Player player) {
        return SuperiorSkyblockAPI.getPlayer(player).getIsland();
    }

    /**
     * Retrieves the island located at the given location.
     * @param location The location to check for an island.
     * @return The island at the location or null if no island is found.
     */
    public Island getIslandAtLocation(Location location) {
        return SuperiorSkyblockAPI.getIslandAt(location);
    }

    /**
     * Retrieves the level of the island associated with the given player.
     * @param player The player whose island level is to be fetched.
     * @return The level of the island or 0 if the player doesn't have an island.
     */
    public Number getIslandLevel(Player player) {
        Island island = getIsland(player);
        return island == null ? 0 : island.getIslandLevel();
    }

    /**
     * Checks if the player has the specified privilege at the given location.
     * @param player The player to check for the privilege.
     * @param location The location to check for the privilege.
     * @param privilege The privilege to check.
     * @return True if the player has the privilege, false otherwise.
     */
    public boolean checkPlayerPrivilege(Player player, Location location, String privilege) {
        Island island = getIslandAtLocation(location);
        if (island == null) {
            return true;
        }
        return island.hasPermission(player, IslandPrivilege.getByName(privilege));
    }

    /**
     * Checks if the player has an associated island.
     * @param player The player to check.
     * @return True if the player has an island, false otherwise.
     */
    public boolean playerHasIsland(Player player) {
        return getIsland(player) != null;
    }

    /**
     * Calculates the drop chance multiplier based on the player's island level.
     * @param player The player for whom the multiplier is to be calculated.
     * @return The calculated drop chance multiplier.
     */
    public double getDropChanceMultiplier(Player player) {
        if (playerHasIsland(player)) {
            double levelsInterval = configManager.getIntForPath("drop_multiplier_per_island_level.levels_interval");
            double multiplierValue = configManager.getDoubleForPath("drop_multiplier_per_island_level.multiplier_value");
            double result = (getIslandLevel(player).doubleValue() / levelsInterval) * multiplierValue;
            result = Math.round(result * 100.0) / 100.0;
            return result;
        }
        return 0;
    }
}
