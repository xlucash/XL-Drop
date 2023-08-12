package me.xlucash.xlucashdrop.utils;

import me.xlucash.xlucashdrop.DropMain;

import java.sql.*;
import java.util.UUID;

public class DatabaseManager {
    private final DropMain plugin;
    private Connection connection;
    private String host, database, username, password;
    private int port;

    public DatabaseManager(DropMain plugin) {
        this.plugin = plugin;
        loadConfig();
        connect();
    }

    private void loadConfig() {
        this.host = plugin.getConfig().getString("database.host");
        this.port = plugin.getConfig().getInt("database.port");
        this.database = plugin.getConfig().getString("database.name");
        this.username = plugin.getConfig().getString("database.username");
        this.password = plugin.getConfig().getString("database.password");
    }

    public void connect() {
        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }

            connection = DriverManager.getConnection("jdbc:mysql://" +
                    this.host + ":" + this.port + "/" + this.database, this.username, this.password);

            createTableIfNotExists();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTableIfNotExists() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS player_drops (" +
                            "player_uuid VARCHAR(36) NOT NULL," +
                            "item_name VARCHAR(255) NOT NULL," +
                            "is_enabled BOOLEAN DEFAULT TRUE," +
                            "PRIMARY KEY (player_uuid, item_name))"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setDropEnabled(UUID playerUUID, String itemName, boolean isEnabled) {
        try (PreparedStatement statement = connection.prepareStatement(
                "REPLACE INTO player_drops (player_uuid, item_name, is_enabled) VALUES (?, ?, ?)")) {
            statement.setString(1, playerUUID.toString());
            statement.setString(2, itemName);
            statement.setBoolean(3, isEnabled);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return true;
    }
}
