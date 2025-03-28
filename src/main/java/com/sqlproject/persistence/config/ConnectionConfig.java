package com.sqlproject.persistence.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionConfig implements AutoCloseable {
    private final Connection connection;

    public ConnectionConfig() throws SQLException {
        this.connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:" + System.getenv("DB_PORT") + "/sql_project",
                System.getenv("DB_USER"),
                System.getenv("DB_PASSWORD")
        );
        this.connection.setAutoCommit(false);
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
