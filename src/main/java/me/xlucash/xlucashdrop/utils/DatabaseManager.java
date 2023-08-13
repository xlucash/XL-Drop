package me.xlucash.xlucashdrop.utils;

import me.xlucash.xlucashdrop.DropMain;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS stone_generators (" +
                            "x INT NOT NULL," +
                            "y INT NOT NULL," +
                            "z INT NOT NULL," +
                            "world VARCHAR(255) NOT NULL," +
                            "owner_uuid VARCHAR(36) NOT NULL," +
                            "PRIMARY KEY (x, y, z, world))"
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

    public void addGenerator(Location location, UUID ownerUUID) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO stone_generators (x, y, z, world, owner_uuid) VALUES (?, ?, ?, ?, ?)")) {
            prepareSQLStatement(location, statement);
            statement.setString(5, ownerUUID.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeGenerator(Location location) {
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM stone_generators WHERE x = ? AND y = ? AND z = ? AND world = ?")) {
            prepareSQLStatement(location, statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isGenerator(Location location) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM stone_generators WHERE x = ? AND y = ? AND z = ? AND world = ?")) {
            prepareSQLStatement(location, statement);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void prepareSQLStatement(Location location, PreparedStatement statement) throws SQLException {
        statement.setInt(1, location.getBlockX());
        statement.setInt(2, location.getBlockY());
        statement.setInt(3, location.getBlockZ());
        statement.setString(4, location.getWorld().getName());
    }

    public List<Location> getAllGeneratorLocations() {
        List<Location> locations = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT world, x, y, z FROM stone_generators")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String worldName = resultSet.getString("world");
                World world = Bukkit.getWorld(worldName);
                double x = resultSet.getDouble("x");
                double y = resultSet.getDouble("y");
                double z = resultSet.getDouble("z");
                locations.add(new Location(world, x, y, z));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return locations;
    }
}
