package com.sqlproject;


import com.sqlproject.persistence.config.ConnectionConfig;

import com.sqlproject.persistence.migration.MigrationStrategy;

import com.sqlproject.ui.BlockUI;
import com.sqlproject.ui.BoardColumnUI;
import com.sqlproject.ui.BoardUI;
import com.sqlproject.ui.CardHistoryUI;
import com.sqlproject.ui.CardUI;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (ConnectionConfig connectionConfig = new ConnectionConfig();
             MigrationStrategy migrator = new MigrationStrategy(connectionConfig)) {

            System.out.println("Iniciando a migração...");
            migrator.executeMigration();
            System.out.println("Migração concluída com sucesso!");

            Scanner scanner = new Scanner(System.in);
            BoardUI boardUI = new BoardUI(scanner);
            BoardColumnUI boardColumnUI = new BoardColumnUI(scanner);
            CardUI cardUI = new CardUI(scanner);
            BlockUI blockUI = new BlockUI(scanner);
            CardHistoryUI cardHistoryUI = new CardHistoryUI(scanner);

            boolean running = true;

            while (running) {
                System.out.println("\n===== MENU PRINCIPAL =====");
                System.out.println("1. Gerenciar Quadros (Boards)");
                System.out.println("2. Gerenciar Colunas (Board Columns)");
                System.out.println("3. Gerenciar Cards");
                System.out.println("4. Gerenciar Bloqueios (Blocks)");
                System.out.println("5. Visualizar Histórico de Cards");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");

                int option = -1;
                try {
                    option = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Opção inválida. Tente novamente.");
                    continue;
                }

                switch (option) {
                    case 1:
                        boardUI.start();
                        break;
                    case 2:
                        boardColumnUI.start();
                        break;
                    case 3:
                        cardUI.start();
                        break;
                    case 4:
                        blockUI.start();
                        break;
                    case 5:
                        cardHistoryUI.start();
                        break;
                    case 0:
                        running = false;
                        System.out.println("Saindo do sistema...");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            }

            scanner.close();

        } catch (SQLException e) {
            System.err.println("Erro durante a aplicação:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}