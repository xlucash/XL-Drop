package me.xlucash.xldrop.utils;

import org.bukkit.entity.Player;

/**
 * Manages permissions for the Drop plugin.
 */
public class PermissionManager {

    /**
     * Checks if the given player has the specified permission.
     * @param player     The player to check.
     * @param permission The permission string to check against.
     * @return True if the player has the permission, false otherwise.
     */
    public boolean hasPermission(Player player, String permission) {
        return player.hasPermission(permission);
    }
}
