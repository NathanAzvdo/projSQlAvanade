package com.sqlproject;

import com.sqlproject.persistence.config.ConnectionConfig;
import com.sqlproject.persistence.dao.BoardColumnDAO;
import com.sqlproject.persistence.dao.BoardDAO;
import com.sqlproject.persistence.migration.MigrationStrategy;
import com.sqlproject.service.BoardService;
import com.sqlproject.service.BoardServiceImpl;
import com.sqlproject.ui.BoardUI;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try (ConnectionConfig connectionConfig = new ConnectionConfig();
             MigrationStrategy migrator = new MigrationStrategy(connectionConfig)) {

            System.out.println("Iniciando a migração...");
            migrator.executeMigration();
            System.out.println("Migração concluída com sucesso!");

            BoardDAO boardDAO = new BoardDAO();
            BoardColumnDAO boardColumnDAO = new BoardColumnDAO();

            BoardService boardService = new BoardServiceImpl(boardDAO, boardColumnDAO);

            BoardUI ui = new BoardUI(boardService);


            System.out.println("=== Sistema de Gerenciamento de Quadros ===");
            ui.start();

        } catch (SQLException e) {
            System.err.println("Erro durante a aplicação:");
            e.printStackTrace();
            System.exit(1);
        }

    }
}