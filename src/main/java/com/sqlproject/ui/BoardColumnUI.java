package com.sqlproject.ui;

import com.sqlproject.exceptions.BoardColumnNotFoundException;
import com.sqlproject.persistence.entity.Board;
import com.sqlproject.persistence.entity.BoardColumn;
import com.sqlproject.service.BoardColumnService;
import com.sqlproject.service.BoardService;

import java.util.List;
import java.util.Scanner;

public class BoardColumnUI {
    private final BoardColumnService boardColumnService;
    private final BoardService boardService; // Assumindo que você tem um BoardService para buscar informações do board
    private final Scanner scanner;

    public BoardColumnUI(BoardColumnService boardColumnService, BoardService boardService) {
        this.boardColumnService = boardColumnService;
        this.boardService = boardService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            displayMenu();
            int option = readIntegerInput("Escolha uma opção: ");

            try {
                switch (option) {
                    case 1:
                        createColumnUI();
                        break;
                    case 2:
                        getColumnsByBoardIdUI();
                        break;
                    case 3:
                        updateColumnOrderUI();
                        break;
                    case 4:
                        deleteColumnUI();
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
        System.out.println("\n===== GERENCIAMENTO DE COLUNAS =====");
        System.out.println("1. Criar Nova Coluna");
        System.out.println("2. Listar Colunas por Board ID");
        System.out.println("3. Atualizar Ordem de Coluna");
        System.out.println("4. Excluir Coluna");
        System.out.println("0. Voltar");
        System.out.println("===================================");
    }

    private void createColumnUI() {
        System.out.println("\n----- CRIAR NOVA COLUNA -----");

        System.out.print("Nome da Coluna: ");
        String name = scanner.nextLine();

        Long boardId = readLongInput("ID do Board: ");

        int order = readIntegerInput("Ordem da Coluna: ");

        System.out.print("Tipo da Coluna (ex: TODO, IN_PROGRESS, DONE): ");
        String kind = scanner.nextLine();

        try {
            Board board = boardService.getBoardById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("Board com ID " + boardId + " não encontrado."));

            BoardColumn column = new BoardColumn();
            column.setName(name);
            column.setOrder(order);
            column.setKind(kind);
            column.setBoard(board);

            BoardColumn createdColumn = boardColumnService.createColumn(column);
            System.out.println("Coluna criada com sucesso! ID: " + createdColumn.getId());

        } catch (IllegalArgumentException e) {
            System.out.println("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro ao criar coluna: " + e.getMessage());
        }
    }

    private void getColumnsByBoardIdUI() {
        System.out.println("\n----- LISTAR COLUNAS POR BOARD ID -----");
        Long boardId = readLongInput("Informe o ID do Board: ");

        try {
            List<BoardColumn> columns = boardColumnService.getColumnsByBoardId(boardId);

            if (columns.isEmpty()) {
                System.out.println("Nenhuma coluna encontrada para o Board ID " + boardId);
            } else {
                System.out.println("\nColunas encontradas para o Board ID " + boardId + ":");
                System.out.println("------------------------------------------------------------------");
                System.out.printf("%-5s %-25s %-10s %-15s%n",
                        "ID", "Nome", "Ordem", "Tipo");
                System.out.println("------------------------------------------------------------------");

                for (BoardColumn column : columns) {
                    System.out.printf("%-5d %-25s %-10d %-15s%n",
                            column.getId(),
                            column.getName(),
                            column.getOrder(),
                            column.getKind());
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar colunas: " + e.getMessage());
        }
    }

    private void updateColumnOrderUI() {
        System.out.println("\n----- ATUALIZAR ORDEM DE COLUNA -----");
        Long columnId = readLongInput("Informe o ID da Coluna: ");
        int newOrder = readIntegerInput("Informe a nova ordem: ");

        try {
            boardColumnService.updateColumnOrder(columnId, newOrder);
            System.out.println("Ordem da coluna atualizada com sucesso!");
        } catch (BoardColumnNotFoundException e) {
            System.out.println("Coluna não encontrada: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro ao atualizar ordem da coluna: " + e.getMessage());
        }
    }

    private void deleteColumnUI() {
        System.out.println("\n----- EXCLUIR COLUNA -----");
        Long columnId = readLongInput("Informe o ID da Coluna a ser excluída: ");

        System.out.print("Esta operação não pode ser desfeita. Digite 'CONFIRMAR' para continuar: ");
        String confirmation = scanner.nextLine();

        if ("CONFIRMAR".equals(confirmation)) {
            try {
                boardColumnService.deleteColumn(columnId);
                System.out.println("Coluna excluída com sucesso!");
            } catch (BoardColumnNotFoundException e) {
                System.out.println("Coluna não encontrada: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Erro ao excluir coluna: " + e.getMessage());
            }
        } else {
            System.out.println("Operação cancelada pelo usuário.");
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