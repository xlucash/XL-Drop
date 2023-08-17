package me.xlucash.xlucashdrop.hooks;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import me.xlucash.xlucashdrop.DropMain;
import me.xlucash.xlucashdrop.config.DropConfig;
import org.bukkit.entity.Player;

public class SuperiorSkyblockHook {

    private final DropMain plugin;
    private final DropConfig dropConfig;

    public SuperiorSkyblockHook(DropMain plugin, DropConfig dropConfig) {
        this.plugin = plugin;
        this.dropConfig = dropConfig;
    }

    private Island getIsland(Player player) {
        return SuperiorSkyblockAPI.getPlayer(player).getIsland();
    }

    public Number getIslandLevel(Player player) {
        Island island = getIsland(player);
        return island == null ? 0 : island.getIslandLevel();
    }

    public boolean playerHasIsland(Player player) {
        return getIsland(player) != null;
    }

    public double getDropChanceMultiplier(Player player) {
        if (playerHasIsland(player)) {
            double levelsInterval = dropConfig.getIntForPath("drop_multiplier_per_island_level.levels_interval");
            double multiplierValue = dropConfig.getDoubleForPath("drop_multiplier_per_island_level.multiplier_value");
            return (getIslandLevel(player).doubleValue() / levelsInterval) * multiplierValue;
        }
        return 0;
    }
}
