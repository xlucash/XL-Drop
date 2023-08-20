package me.xlucash.xldrop.database;

import me.xlucash.xldrop.DropMain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Repository class for managing player-specific drop settings in the database.
 */
public class PlayerDropRepository {
    private final Connection connection;
    private final DropMain plugin;

    public PlayerDropRepository(Connection connection, DropMain plugin) {
        this.connection = connection;
        this.plugin = plugin;
    }

    /**
     * Sets the drop enabled status for a specific item for a player.
     * @param playerUUID The UUID of the player.
     * @param itemName   The name of the item.
     * @param isEnabled  The enabled status to set.
     */
    public void setDropEnabled(UUID playerUUID, String itemName, boolean isEnabled) {
        try (PreparedStatement statement = connection.prepareStatement(
                "REPLACE INTO player_drops (player_uuid, item_name, is_enabled) VALUES (?, ?, ?)")) {
            statement.setString(1, playerUUID.toString());
            statement.setString(2, itemName);
            statement.setBoolean(3, isEnabled);
            statement.executeUpdate();
        } catch (SQLException e) {
            DatabaseManager.handleDatabaseError(e, plugin);
        }
    }

    /**
     * Checks if the drop is enabled for a specific item for a player.
     * @param playerUUID The UUID of the player.
     * @param itemName   The name of the item.
     * @return True if the drop is enabled, false otherwise.
     */
    public boolean isDropEnabled(UUID playerUUID, String itemName) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT is_enabled FROM player_drops WHERE player_uuid = ? AND item_name = ?")) {
            statement.setString(1, playerUUID.toString());
            statement.setString(2, itemName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBoolean("is_enabled");
            }
        } catch (SQLException e) {
            DatabaseManager.handleDatabaseError(e, plugin);
        }
        // Default to true if no record is found
        return true;
    }
}
