package me.xlucash.xldrop.database;

import me.xlucash.xldrop.DropMain;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

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

    public void setDropEnabled(UUID playerUUID, String itemName, boolean isEnabled) {
        playerDropRepository.setDropEnabled(playerUUID, itemName, isEnabled);
    }

    public boolean isDropEnabled(UUID playerUUID, String itemName) {
        return playerDropRepository.isDropEnabled(playerUUID, itemName);
    }

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

    public void connect() {
        connectionManager.connect();
    }

    public void disconnect() {
        connectionManager.disconnect();
    }

    protected static void handleDatabaseError(Exception e, DropMain plugin) {
        plugin.getLogger().severe("Wystąpił błąd podczas łączenia z bazą danych! Sprawdź konfigurację pluginu!");
        plugin.getServer().getPluginManager().disablePlugin(plugin);
    }
}
