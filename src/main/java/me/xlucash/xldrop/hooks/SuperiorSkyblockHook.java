package me.xlucash.xldrop.hooks;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import me.xlucash.xldrop.DropMain;
import me.xlucash.xldrop.config.ConfigManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SuperiorSkyblockHook {

    private final DropMain plugin;
    private final ConfigManager configManager;

    public SuperiorSkyblockHook(DropMain plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    private Island getIsland(Player player) {
        return SuperiorSkyblockAPI.getPlayer(player).getIsland();
    }

    public Island getIslandAtLocation(Location location) {
        return SuperiorSkyblockAPI.getIslandAt(location);
    }

    public Number getIslandLevel(Player player) {
        Island island = getIsland(player);
        return island == null ? 0 : island.getIslandLevel();
    }

    public boolean checkPlayerPrivilege(Player player, Location location, String privilege) {
        Island island = getIslandAtLocation(location);
        if (island == null) {
            return true;
        }
        return island.hasPermission(player, IslandPrivilege.getByName(privilege));
    }

    public boolean playerHasIsland(Player player) {
        return getIsland(player) != null;
    }

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
