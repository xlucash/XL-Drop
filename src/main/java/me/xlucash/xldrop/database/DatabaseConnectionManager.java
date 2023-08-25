package me.xlucash.xldrop.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.xlucash.xldrop.DropMain;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages the database connection for the plugin.
 */
public class DatabaseConnectionManager {
    private final DropMain plugin;
    private Connection connection;
    private HikariDataSource dataSource;
    private String host, database, username, password, type;
    private int port;

    public DatabaseConnectionManager(DropMain plugin) {
        this.plugin = plugin;
        loadConfig();
        setupPool();
    }

    /**
     * Loads the database configuration from the plugin's config.
     */
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

    /**
     * Sets up the HikariCP connection pool based on the loaded configuration.
     * For MySQL, it sets up the JDBC URL, username, and password.
     * For SQLite, it ensures the database file exists and sets up the JDBC URL.
     * The maximum pool size is also set based on the database type.
     */
    private void setupPool() {
        HikariConfig config = new HikariConfig();

        switch (type) {
            case "MySQL":
                config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
                config.setUsername(username);
                config.setPassword(password);
                config.setMaximumPoolSize(25);
                break;
            case "SQLite":
                File dbFile = new File(plugin.getDataFolder(), "database.db");
                if (!dbFile.exists()) {
                    try {
                        dbFile.createNewFile();
                    } catch (IOException e) {
                        DatabaseManager.handleDatabaseError(e, plugin);
                    }
                }
                config.setJdbcUrl("jdbc:sqlite:" + dbFile.getAbsolutePath());
                config.setMaximumPoolSize(10);
                break;
        }

        dataSource = new HikariDataSource(config);
    }


    /**
     * Disconnects and closes the HikariCP connection pool.
     * This should be called when the plugin is disabled to free up resources.
     */
    public void disconnect() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    /**
     * Retrieves a connection from the HikariCP connection pool.
     * If there's an issue obtaining the connection, a runtime exception is thrown.
     *
     * @return A database connection.
     */
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Creates the necessary tables if they do not exist.
     */
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