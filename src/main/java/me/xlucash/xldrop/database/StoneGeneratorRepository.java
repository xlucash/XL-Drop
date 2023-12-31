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

/**
 * Repository class for managing stone generators in the database.
 */
public class StoneGeneratorRepository {
    private final DatabaseConnectionManager connectionManager;
    private final DropMain plugin;

    public StoneGeneratorRepository(DatabaseConnectionManager connectionManager, DropMain plugin) {
        this.connectionManager = connectionManager;
        this.plugin = plugin;
    }

    /**
     * Adds a new stone generator to the database.
     * @param location  The location of the generator.
     * @param ownerUUID The UUID of the generator's owner.
     */
    public void addGenerator(Location location, UUID ownerUUID) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO stone_generators (x, y, z, world, owner_uuid) VALUES (?, ?, ?, ?, ?)")) {
            prepareSQLStatement(location, statement);
            statement.setString(5, ownerUUID.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            DatabaseManager.handleDatabaseError(e, plugin);
        }
    }

    /**
     * Removes a stone generator from the database.
     * @param location The location of the generator.
     */
    public void removeGenerator(Location location) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM stone_generators WHERE x = ? AND y = ? AND z = ? AND world = ?")) {
            prepareSQLStatement(location, statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            DatabaseManager.handleDatabaseError(e, plugin);
        }
    }

    /**
     * Checks if a location is a stone generator.
     * @param location The location to check.
     * @return True if the location is a stone generator, false otherwise.
     */
    public boolean isGenerator(Location location) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM stone_generators WHERE x = ? AND y = ? AND z = ? AND world = ?")) {
            prepareSQLStatement(location, statement);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            DatabaseManager.handleDatabaseError(e, plugin);
        }
        return false;
    }

    /**
     * Retrieves all the locations of stone generators from the database.
     * @return A list of stone generator locations.
     */
    public List<Location> getAllGeneratorLocations() {
        List<Location> locations = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
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

    /**
     * Prepares the SQL statement with the given location.
     * @param location  The location to set in the statement.
     * @param statement The SQL statement to prepare.
     * @throws SQLException If there's an error setting the parameters.
     */
    private static void prepareSQLStatement(Location location, PreparedStatement statement) throws SQLException {
        statement.setInt(1, location.getBlockX());
        statement.setInt(2, location.getBlockY());
        statement.setInt(3, location.getBlockZ());
        statement.setString(4, location.getWorld().getName());
    }
}