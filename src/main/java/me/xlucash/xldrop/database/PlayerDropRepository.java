package me.xlucash.xldrop.database;

import me.xlucash.xldrop.DropMain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerDropRepository {
    private final Connection connection;
    private final DropMain plugin;

    public PlayerDropRepository(Connection connection, DropMain plugin) {
        this.connection = connection;
        this.plugin = plugin;
    }

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
        return true;
    }
}
