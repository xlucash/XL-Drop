package me.xlucash.xlucashdrop.database;

import me.xlucash.xlucashdrop.DropMain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {
    private final DropMain plugin;
    private Connection connection;
    private String host, database, username, password;
    private int port;

    public DatabaseConnectionManager(DropMain plugin) {
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
        } catch (SQLException e) {
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

}