package com.sqlproject.ui;

import com.sqlproject.exceptions.CardNotFoundException;
import com.sqlproject.persistence.entity.Block;
import com.sqlproject.service.BlockService;
import com.sqlproject.service.BlockServiceImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class BlockUI {
    private final BlockService blockService;
    private final Scanner scanner;

    public BlockUI(Scanner scanner) throws SQLException {
        this.blockService = new BlockServiceImpl();
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
                        blockCardUI();
                        break;
                    case 2:
                        unblockCardUI();
                        break;
                    case 3:
                        getBlockHistoryUI();
                        break;
                    case 0:
                        running = false;
                        System.out.println("Saindo do sistema...");
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
        System.out.println("\n===== SISTEMA DE GERENCIAMENTO DE BLOQUEIOS =====");
        System.out.println("1. Bloquear Cartão");
        System.out.println("2. Desbloquear Cartão");
        System.out.println("3. Visualizar Histórico de Bloqueios");
        System.out.println("0. Sair");
        System.out.println("===============================================");
    }

    private void blockCardUI() {
        System.out.println("\n----- BLOQUEAR CARTÃO -----");
        Long cardId = readLongInput("Informe o ID do cartão: ");
        System.out.print("Informe o motivo do bloqueio: ");
        String reason = scanner.nextLine();

        try {
            blockService.blockCard(cardId, reason);
            System.out.println("Cartão bloqueado com sucesso!");
        } catch (CardNotFoundException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Erro ao acessar o banco de dados: " + e.getMessage());
        }
    }

    private void unblockCardUI() {
        System.out.println("\n----- DESBLOQUEAR CARTÃO -----");
        Long cardId = readLongInput("Informe o ID do cartão: ");
        System.out.print("Informe o motivo do desbloqueio: ");
        String reason = scanner.nextLine();

        try {
            blockService.unblockCard(cardId, reason);
            System.out.println("Cartão desbloqueado com sucesso!");
        } catch (CardNotFoundException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Erro ao acessar o banco de dados: " + e.getMessage());
        }
    }

    private void getBlockHistoryUI() {
        System.out.println("\n----- HISTÓRICO DE BLOQUEIOS -----");
        Long cardId = readLongInput("Informe o ID do cartão: ");

        try {
            List<Block> blockHistory = blockService.getBlockHistory(cardId);

            if (blockHistory.isEmpty()) {
                System.out.println("Nenhum registro de bloqueio encontrado para este cartão.");
            } else {
                System.out.println("\nHistórico de bloqueios para o cartão ID " + cardId + ":");
                System.out.println("-------------------------------------------------------");
                System.out.printf("%-5s %-20s %-30s %-30s %-10s%n",
                        "ID", "ID do Cartão", "Data de Bloqueio", "Data de Desbloqueio", "Motivo");
                System.out.println("-------------------------------------------------------");

                for (Block block : blockHistory) {
                    System.out.printf("%-5d %-20s %-30s %-30s %-10s%n",
                            block.getId(),
                            block.getCard().toString(),
                            block.getBlockIn(),
                            block.getUnblockIn() != null ? block.getUnblockIn() : "Ainda bloqueado",
                            block.getBlockCause());
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