package me.xlucash.xlucashdrop.hooks;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import me.xlucash.xlucashdrop.DropMain;
import org.bukkit.entity.Player;

public class SuperiorSkyblockHook {
    private static DropMain plugin = DropMain.plugin;
    public static Number getIslandLevel(Player player) {
        Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();
        return island == null ? 0 : island.getIslandLevel();
    }

    public static boolean hasIsland(Player player) {
        Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();
        return island != null;
    }

    public static double getDropChanceMultiplier(Player player) {
        if (SuperiorSkyblockHook.hasIsland(player)) {
            double levelsInterval = plugin.getConfig().getInt("drop_multiplier_per_island_level.levels_interval");
            double multiplierValue = plugin.getConfig().getDouble("drop_multiplier_per_island_level.multiplier_value");
            return ((Double) SuperiorSkyblockHook.getIslandLevel(player) / levelsInterval) * multiplierValue;
        }
        return 0;
    }
}
