package me.xlucash.xlucashdrop.utils;

import me.xlucash.xlucashdrop.DropMain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private Connection connection;
    private final String host, database, username, password;
    private final int port;

    public DatabaseManager(DropMain plugin) {
        this.host = plugin.getConfig().getString("database.host");
        this.port = plugin.getConfig().getInt("database.port");
        this.database = plugin.getConfig().getString("database.name");
        this.username = plugin.getConfig().getString("database.username");
        this.password = plugin.getConfig().getString("database.password");
    }

    public void connect() throws SQLException {
        if(connection != null && !connection.isClosed()) {
            return;
        }

        connection = DriverManager.getConnection("jdbc:mysql://" +
                this.host + ":" + this.port + "/" + this.database, this.username, this.password);
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
