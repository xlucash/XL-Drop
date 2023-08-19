package me.xlucash.xldrop.database;

import me.xlucash.xldrop.DropMain;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnectionManager {
    private final DropMain plugin;
    private Connection connection;
    private String host, database, username, password, type;
    private int port;

    public DatabaseConnectionManager(DropMain plugin) {
        this.plugin = plugin;
        loadConfig();
        connect();
    }

    private void loadConfig() {
        this.type = plugin.getConfig().getString("database.type", "SQLite");
        if (this.type.equals("MySQL")) {
            this.host = plugin.getConfig().getString("database.host");
            this.port = plugin.getConfig().getInt("database.port");
            this.database = plugin.getConfig().getString("database.name");
            this.username = plugin.getConfig().getString("database.username");
            this.password = plugin.getConfig().getString("database.password");
        }
    }

    public void connect() {
        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }

            switch (type) {
                case "MySQL":
                    connection = DriverManager.getConnection("jdbc:mysql://" +
                            this.host + ":" + this.port + "/" + this.database, this.username, this.password);
                    break;
                case "SQLite":
                    File dbFile = new File(plugin.getDataFolder(), "database.db");
                    if (!dbFile.exists()) {
                        dbFile.createNewFile();
                    }
                    connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
                    break;
                default:
                    throw new SQLException("Unsupported database type: " + type);
            }

            createTableIfNotExists();
        } catch (SQLException | IOException e) {
            DatabaseManager.handleDatabaseError(e, plugin);
        }
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            DatabaseManager.handleDatabaseError(e, plugin);
        }
    }

    public Connection getConnection() {
        return connection;
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
            DatabaseManager.handleDatabaseError(e, plugin);
        }
    }

}