package com.sqlproject;

import com.sqlproject.persistence.config.ConnectionConfig;
import com.sqlproject.persistence.migration.MigrationStrategy;

import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {
        try(var connection = ConnectionConfig.getConnection()) {
            new MigrationStrategy(connection).executeMigration();
        }
    }
}