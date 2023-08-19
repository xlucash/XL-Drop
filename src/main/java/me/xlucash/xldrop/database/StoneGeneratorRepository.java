package me.xlucash.xldrop.database;

import me.xlucash.xldrop.DropMain;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StoneGeneratorRepository {
    private final Connection connection;
    private final DropMain plugin;

    public StoneGeneratorRepository(Connection connection, DropMain plugin) {
        this.connection = connection;
        this.plugin = plugin;
    }

    public void addGenerator(Location location, UUID ownerUUID) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO stone_generators (x, y, z, world, owner_uuid) VALUES (?, ?, ?, ?, ?)")) {
            prepareSQLStatement(location, statement);
            statement.setString(5, ownerUUID.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            DatabaseManager.handleDatabaseError(e, plugin);
        }
    }

    public void removeGenerator(Location location) {
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM stone_generators WHERE x = ? AND y = ? AND z = ? AND world = ?")) {
            prepareSQLStatement(location, statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            DatabaseManager.handleDatabaseError(e, plugin);
        }
    }

    public boolean isGenerator(Location location) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM stone_generators WHERE x = ? AND y = ? AND z = ? AND world = ?")) {
            prepareSQLStatement(location, statement);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            DatabaseManager.handleDatabaseError(e, plugin);
        }
        return false;
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
            DatabaseManager.handleDatabaseError(e, plugin);
        }

        return locations;
    }

    private static void prepareSQLStatement(Location location, PreparedStatement statement) throws SQLException {
        statement.setInt(1, location.getBlockX());
        statement.setInt(2, location.getBlockY());
        statement.setInt(3, location.getBlockZ());
        statement.setString(4, location.getWorld().getName());
    }
}