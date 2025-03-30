package com.sqlproject.ui;

import com.sqlproject.persistence.entity.CardHistory;
import com.sqlproject.service.CardHistoryServiceImpl;


import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CardHistoryUI {
    private final CardHistoryServiceImpl cardHistoryService;
    private final Scanner scanner;

    public CardHistoryUI(Scanner scanner) throws SQLException {
        this.cardHistoryService = new CardHistoryServiceImpl();
        this.scanner = scanner;
    }

    public void start() {
        boolean running = true;
        while (running) {
            displayMenu();
            int option = readIntegerInput("Escolha uma opção: ");

            try {
                switch (option) {
                    case 1:
                        getCardHistoryUI();
                        break;
                    case 0:
                        running = false;
                        System.out.println("Voltando ao menu principal...");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }

            if (running) {
                System.out.println("\nPressione ENTER para continuar...");
                scanner.nextLine();
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n===== HISTÓRICO DE CARTÕES =====");
        System.out.println("1. Visualizar Histórico de um Cartão");
        System.out.println("0. Voltar");
        System.out.println("===============================");
    }

    private void getCardHistoryUI() {
        System.out.println("\n----- HISTÓRICO DE CARTÃO -----");
        Long cardId = readLongInput("Informe o ID do cartão: ");

        try {
            List<CardHistory> history = cardHistoryService.getHistoryByCardId(cardId);

            if (history.isEmpty()) {
                System.out.println("Nenhum registro de histórico encontrado para este cartão.");
            } else {
                System.out.println("\nHistórico do cartão ID " + cardId + ":");
                System.out.println("------------------------------------------------------------------");
                System.out.printf("%-5s %-20s %-25s %-25s %-20s%n",
                        "ID", "ID do Cartão", "Data da Operação", "Tipo de Operação", "Detalhes");
                System.out.println("------------------------------------------------------------------");

                for (CardHistory entry : history) {
                    System.out.printf("%-5d %-20s %-30s %-30s %-10s%n",
                            entry.getId(),
                            entry.getCard().toString(),
                            entry.getEnteredAt(),
                            entry.getBoardColumn(),
                            entry.getExitedAt());
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao acessar o banco de dados: " + e.getMessage());
        }
    }

    private Long readLongInput(String prompt) {
        Long value = null;
        while (value == null) {
            System.out.print(prompt);
            try {
                value = Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, insira um número válido.");
            }
        }
        return value;
    }

    private int readIntegerInput(String prompt) {
        int value = -1;
        while (value == -1) {
            System.out.print(prompt);
            try {
                value = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, insira um número válido.");
            }
        }
        return value;
    }
}