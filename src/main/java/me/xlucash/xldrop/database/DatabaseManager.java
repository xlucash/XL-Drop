package me.xlucash.xldrop.database;

import me.xlucash.xldrop.DropMain;
import me.xlucash.xldrop.enums.Message;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

/**
 * Manages database operations related to player drops and stone generators.
 */
public class DatabaseManager {
    private final DropMain plugin;
    private final DatabaseConnectionManager connectionManager;
    private final PlayerDropRepository playerDropRepository;
    private final StoneGeneratorRepository stoneGeneratorRepository;

    public DatabaseManager(DropMain plugin) {
        this.plugin = plugin;
        this.connectionManager = new DatabaseConnectionManager(plugin);
        this.playerDropRepository = new PlayerDropRepository(connectionManager.getConnection(), plugin);
        this.stoneGeneratorRepository = new StoneGeneratorRepository(connectionManager.getConnection(), plugin);
    }

    // Player Drop related methods
    public void setDropEnabled(UUID playerUUID, String itemName, boolean isEnabled) {
        playerDropRepository.setDropEnabled(playerUUID, itemName, isEnabled);
    }

    public boolean isDropEnabled(UUID playerUUID, String itemName) {
        return playerDropRepository.isDropEnabled(playerUUID, itemName);
    }

    // Stone Generator related methods
    public void addGenerator(Location location, UUID ownerUUID) {
        stoneGeneratorRepository.addGenerator(location, ownerUUID);
    }

    public void removeGenerator(Location location) {
        stoneGeneratorRepository.removeGenerator(location);
    }

    public boolean isGenerator(Location location) {
        return stoneGeneratorRepository.isGenerator(location);
    }

    public List<Location> getAllGeneratorLocations() {
        return stoneGeneratorRepository.getAllGeneratorLocations();
    }

    // Database connection related methods
    public void connect() {
        connectionManager.connect();
    }

    public void disconnect() {
        connectionManager.disconnect();
    }

    /**
     * Handles database errors by logging the error and disabling the plugin.
     * @param e      The exception that occurred.
     * @param plugin The main plugin instance.
     */
    protected static void handleDatabaseError(Exception e, DropMain plugin) {
        plugin.getLogger().severe(Message.DATABASE_CONNECTION_ERROR.getText());
        plugin.getLogger().severe(e.getMessage());
        plugin.getServer().getPluginManager().disablePlugin(plugin);
    }
}
