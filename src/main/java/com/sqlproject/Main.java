package com.sqlproject;

import com.sqlproject.persistence.config.ConnectionConfig;
import com.sqlproject.persistence.migration.MigrationStrategy;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try (ConnectionConfig connectionConfig = new ConnectionConfig();
             MigrationStrategy migrator = new MigrationStrategy(connectionConfig)) {

            System.out.println("Iniciando a migração...");
            migrator.executeMigration();
            System.out.println("Migração concluída com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro durante a migração:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}