package me.xlucash.xldrop.utils;

import org.bukkit.entity.Player;

public class PermissionManager {
    public boolean hasPermission(Player player, String permission) {
        return player.hasPermission(permission);
    }
}
