package com.sqlproject.persistence.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionConfig {

    public static Connection getConnection() throws SQLException {
        return configureConnection(DriverManager.getConnection(
                "jdbc:mysql://localhost:" + System.getenv("DB_PORT") + "/sql_project",
                System.getenv("DB_USER"),
                System.getenv("DB_PASSWORD")
        ));
    }

    private static Connection configureConnection(Connection connection) throws SQLException {
        connection.setAutoCommit(false);
        return connection;
    }
}
