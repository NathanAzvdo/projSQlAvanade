package com.sqlproject.ui;


import com.sqlproject.exceptions.*;
import com.sqlproject.persistence.entity.Card;
import com.sqlproject.persistence.entity.Status;
import com.sqlproject.service.CardService;
import com.sqlproject.service.CardServiceImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CardUI {
    private final CardService cardService;
    private final Scanner scanner;

    public CardUI(Scanner scanner) throws SQLException{
        this.cardService = new CardServiceImpl();
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
                        createCardUI();
                        break;
                    case 2:
                        getCardByIdUI();
                        break;
                    case 3:
                        getCardsByBoardIdUI();
                        break;
                    case 4:
                        moveCardUI();
                        break;
                    case 5:
                        cancelCardUI();
                        break;
                    case 6:
                        deleteCardUI();
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
        System.out.println("\n===== GERENCIAMENTO DE CARDS =====");
        System.out.println("1. Criar Novo Card");
        System.out.println("2. Buscar Card por ID");
        System.out.println("3. Listar Cards por Board ID");
        System.out.println("4. Mover Card para Nova Coluna");
        System.out.println("5. Cancelar Card");
        System.out.println("6. Excluir Card");
        System.out.println("0. Voltar");
        System.out.println("==================================");
    }

    private void createCardUI() {
        System.out.println("\n----- CRIAR NOVO CARD -----");

        System.out.print("Título do Card: ");
        String title = scanner.nextLine();

        System.out.print("Descrição do Card (opcional, pressione ENTER para pular): ");
        String description = scanner.nextLine();

        System.out.println("Status do Card:");
        System.out.println("1 - TODO");
        System.out.println("2 - IN_PROGRESS");
        System.out.println("3 - DONE");
        System.out.println("4 - CANCELED");
        System.out.println("5 - BLOCKED");
        int statusChoice = readIntegerInput("Escolha o status (1-5): ");

        Status status = switch (statusChoice) {
            case 1 -> Status.TODO;
            case 2 -> Status.IN_PROGRESS;
            case 3 -> Status.DONE;
            case 4 -> Status.CANCELED;
            case 5 -> Status.BLOCKED;
            default -> {
                System.out.println("Opção inválida. Usando TODO como padrão.");
                yield Status.TODO;
            }
        };

        Long boardId = readLongInput("ID do Board: ");

        try {
            Card newCard = new Card();
            newCard.setTitle(title);
            newCard.setDescription(description.isEmpty() ? null : description);
            newCard.setStatus(status);

            Card createdCard = cardService.createCard(newCard, boardId);
            System.out.println("Card criado com sucesso! ID: " + createdCard.getId());
            displayCardDetails(createdCard);

        } catch (IllegalArgumentException e) {
            System.out.println("Erro de validação: " + e.getMessage());
        } catch (BoardNotFoundException e) {
            System.out.println("Board não encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Erro de banco de dados: " + e.getMessage());
        }
    }

    private void getCardByIdUI() {
        System.out.println("\n----- BUSCAR CARD POR ID -----");
        Long cardId = readLongInput("Informe o ID do Card: ");

        try {
            Card card = cardService.getCardById(cardId);
            System.out.println("\nCard encontrado:");
            displayCardDetails(card);
        } catch (CardNotFoundException e) {
            System.out.println("Card não encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Erro de banco de dados: " + e.getMessage());
        }
    }

    private void getCardsByBoardIdUI() {
        System.out.println("\n----- LISTAR CARDS POR BOARD ID -----");
        Long boardId = readLongInput("Informe o ID do Board: ");

        try {
            List<Card> cards = cardService.getCardsByBoardId(boardId);

            if (cards.isEmpty()) {
                System.out.println("Nenhum card encontrado para o Board ID " + boardId);
            } else {
                System.out.println("\nCards encontrados para o Board ID " + boardId + ":");
                System.out.println("------------------------------------------------------------------");
                System.out.printf("%-5s %-30s %-15s %-20s%n",
                        "ID", "Título", "Prioridade", "Coluna");
                System.out.println("------------------------------------------------------------------");

                for (Card card : cards) {
                    System.out.printf("%-5d %-30s %-15s %-20s %-20s %-20s%n",
                            card.getId(),
                            card.getTitle(),
                            card.getDescription(),
                            card.getStatus(),
                            card.getCreatedAt(),
                            card.getBoardColumn().getName());
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro de banco de dados: " + e.getMessage());
        }
    }

    private void moveCardUI() {
        System.out.println("\n----- MOVER CARD PARA NOVA COLUNA -----");
        Long cardId = readLongInput("Informe o ID do Card: ");
        Long targetColumnId = readLongInput("Informe o ID da Coluna de destino: ");

        try {
            cardService.moveCard(cardId, targetColumnId);
            System.out.println("Card movido com sucesso para a nova coluna!");
        } catch (CardNotFoundException e) {
            System.out.println("Card não encontrado: " + e.getMessage());
        } catch (BoardColumnNotFoundException e) {
            System.out.println("Coluna não encontrada: " + e.getMessage());
        } catch (InvalidCardOperationException e) {
            System.out.println("Operação inválida: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Erro de banco de dados: " + e.getMessage());
        }
    }

    private void cancelCardUI() {
        System.out.println("\n----- CANCELAR CARD -----");
        Long cardId = readLongInput("Informe o ID do Card a ser cancelado: ");

        try {
            cardService.cancelCard(cardId);
            System.out.println("Card cancelado com sucesso!");
        } catch (CardNotFoundException e) {
            System.out.println("Card não encontrado: " + e.getMessage());
        } catch (BoardColumnNotFoundException e) {
            System.out.println("Coluna de cancelamento não encontrada: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Erro de banco de dados: " + e.getMessage());
        }
    }

    private void deleteCardUI() {
        System.out.println("\n----- EXCLUIR CARD -----");
        Long cardId = readLongInput("Informe o ID do Card a ser excluído: ");

        System.out.print("Esta operação não pode ser desfeita. Digite 'CONFIRMAR' para continuar: ");
        String confirmation = scanner.nextLine();

        if ("CONFIRMAR".equals(confirmation)) {
            try {
                cardService.deleteCard(cardId);
                System.out.println("Card excluído com sucesso!");
            } catch (CardNotFoundException e) {
                System.out.println("Card não encontrado: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Erro de banco de dados: " + e.getMessage());
            }
        } else {
            System.out.println("Operação cancelada pelo usuário.");
        }
    }

    private void displayCardDetails(Card card) {
        System.out.println("------------------------------------------------------------------");
        System.out.println("ID: " + card.getId());
        System.out.println("Título: " + card.getTitle());
        System.out.println("Descrição: " + (card.getDescription() != null ? card.getDescription() : "N/A"));
        System.out.println("Coluna: " + card.getBoardColumn().getName());
        System.out.println("Status: " + card.getStatus());
        System.out.println("Criado em: " + card.getCreatedAt());
        System.out.println("Board ID: " + card.getBoardColumn().getBoard().getId());
        System.out.println("------------------------------------------------------------------");
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