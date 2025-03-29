package com.sqlproject.ui;

import com.sqlproject.exceptions.BoardNotFoundException;
import com.sqlproject.persistence.entity.Board;
import com.sqlproject.service.BoardService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class BoardUI {
    private final Scanner scanner;
    private final BoardService boardService;

    public BoardUI(BoardService boardService) {
        this.scanner = new Scanner(System.in);
        this.boardService = boardService;
    }

    public void start() {
        boolean running = true;

        while (running) {
            displayMainMenu();
            int option = readOption();

            try {
                switch (option) {
                    case 1:
                        listAllBoards();
                        break;
                    case 2:
                        createNewBoard();
                        break;
                    case 3:
                        viewBoardDetails();
                        break;
                    case 4:
                        deleteBoard();
                        break;
                    case 0:
                        running = false;
                        System.out.println("Encerrando aplicação...");
                        break;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                }
            } catch (SQLException e) {
                System.out.println("Erro de banco de dados: " + e.getMessage());
            } catch (BoardNotFoundException e) {
                System.out.println("Erro: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Erro inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }

        scanner.close();
    }

    private void displayMainMenu() {
        System.out.println("\n===== GERENCIADOR DE QUADROS =====");
        System.out.println("1. Listar todos os quadros");
        System.out.println("2. Criar novo quadro");
        System.out.println("3. Visualizar detalhes de um quadro");
        System.out.println("4. Excluir quadro");
        System.out.println("0. Sair");
        System.out.print("Digite sua opção: ");
    }

    private int readOption() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void listAllBoards() throws SQLException {
        System.out.println("\n===== LISTA DE QUADROS =====");
        List<Board> boards = boardService.getAllBoards();

        if (boards.isEmpty()) {
            System.out.println("Nenhum quadro encontrado.");
            return;
        }

        for (Board board : boards) {
            System.out.println("ID: " + board.getId() + " | Nome: " + board.getName());
        }
    }

    private void createNewBoard() throws SQLException {
        System.out.println("\n===== CRIAR NOVO QUADRO =====");
        System.out.print("Digite o nome do quadro: ");
        String name = scanner.nextLine();

        if (name.trim().isEmpty()) {
            System.out.println("Nome não pode ser vazio.");
            return;
        }

        Board newBoard = new Board();
        newBoard.setName(name);

        try {
            Board createdBoard = boardService.createBoard(newBoard);
            System.out.println("Quadro criado com sucesso! ID: " + createdBoard.getId());
            System.out.println("Colunas iniciais criadas automaticamente: Inicial, Final e Cancelamento.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao criar quadro: " + e.getMessage());
        }
    }

    private void viewBoardDetails() throws SQLException, BoardNotFoundException {
        System.out.println("\n===== DETALHES DO QUADRO =====");
        Long boardId = promptForBoardId();

        if (boardId == null) return;

        Optional<Board> boardOpt = boardService.getBoardById(boardId);

        if (boardOpt.isEmpty()) {
            System.out.println("Quadro não encontrado.");
            return;
        }

        Board board = boardOpt.get();
        System.out.println("ID: " + board.getId());
        System.out.println("Nome: " + board.getName());
        System.out.println("\nEste quadro possui as colunas padrão: Inicial, Final e Cancelamento.");
    }

    private void deleteBoard() throws SQLException, BoardNotFoundException {
        System.out.println("\n===== EXCLUIR QUADRO =====");
        Long boardId = promptForBoardId();

        if (boardId == null) return;

        System.out.print("Tem certeza que deseja excluir este quadro? (S/N): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("S")) {
            boardService.deleteBoard(boardId);
            System.out.println("Quadro excluído com sucesso!");
        } else {
            System.out.println("Operação cancelada.");
        }
    }

    private Long promptForBoardId() {
        System.out.print("Digite o ID do quadro: ");
        try {
            return Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return null;
        }
    }
}