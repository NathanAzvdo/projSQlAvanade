package com.sqlproject.service;

import com.sqlproject.exceptions.*;
import com.sqlproject.persistence.dao.BlockDAO;
import com.sqlproject.persistence.dao.BoardColumnDAO;
import com.sqlproject.persistence.dao.CardDAO;
import com.sqlproject.persistence.dao.CardHistoryDAO;
import com.sqlproject.persistence.entity.Block;
import com.sqlproject.persistence.entity.Board;
import com.sqlproject.persistence.entity.BoardColumn;
import com.sqlproject.persistence.entity.Card;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;

public class CardServiceImpl implements CardService {

    private final CardDAO cardDAO;
    private final BoardColumnDAO boardColumnDAO;
    private final CardHistoryDAO cardHistoryDAO;
    private final BoardService boardService;
    private final BlockDAO blockDAO;

    public CardServiceImpl(CardDAO cardDAO, BoardColumnDAO boardColumnDAO, CardHistoryDAO cardHistoryDAO, BoardService boardService, BlockDAO blockDAO) {
        this.cardDAO = cardDAO;
        this.boardColumnDAO = boardColumnDAO;
        this.cardHistoryDAO = cardHistoryDAO;
        this.boardService = boardService;
        this.blockDAO = blockDAO;
    }

    @Override
    public Card createCard(Card card, Long boardId) throws IllegalArgumentException, BoardNotFoundException, SQLException {
        if (card == null || card.getTitle() == null || card.getTitle().isEmpty() || boardId == null) {
            throw new IllegalArgumentException("Dados do card inválidos.");
        }

        Board board = boardService.getBoardById(boardId).orElseThrow(() -> new BoardNotFoundException("Board com ID " + boardId + " não encontrado."));
        BoardColumn initialColumn = boardColumnDAO.getColumnByBoardId(boardId).stream()
                .filter(column -> "Inicial".equals(column.getName())) // Assumindo que a coluna inicial sempre se chama "Inicial"
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Coluna 'Inicial' não encontrada para o board " + boardId));

        card.setBoardColumn(initialColumn);
        cardDAO.createCard(card);

        return card;
    }

    @Override
    public Card getCardById(Long id) throws CardNotFoundException, SQLException {
        Card card = cardDAO.getCardById(id);
        if (card == null) {
            throw new CardNotFoundException("Card com ID " + id + " não encontrado.");
        }
        return card;
    }

    @Override
    public List<Card> getCardsByBoardId(Long boardId) throws SQLException {
        return cardDAO.getCardsByBoardsId(boardId);
    }

    @Override
    public void moveCard(Long cardId, Long targetColumnId) throws CardNotFoundException, BoardColumnNotFoundException, InvalidCardOperationException, SQLException {

        Card card = cardDAO.getCardById(cardId);
        if (card == null) {
            throw new CardNotFoundException("Card com ID " + cardId + " não encontrado.");
        }

        BoardColumn targetColumn = (BoardColumn) boardColumnDAO.getColumnByBoardId(targetColumnId);
        if (targetColumn == null) {
            throw new BoardColumnNotFoundException("Coluna com ID " + targetColumnId + " não encontrada.");
        }

        if (isCardBlocked(cardId)) {
            throw new InvalidCardOperationException("Card com ID " + cardId + " está bloqueado e não pode ser movido.");
        }

        cardDAO.moveCard(cardId, targetColumnId);
    }

    @Override
    public void cancelCard(Long cardId) throws CardNotFoundException, BoardColumnNotFoundException, SQLException {
        Card card = cardDAO.getCardById(cardId);
        if (card == null) {
            throw new CardNotFoundException("Card com ID " + cardId + " não encontrado.");
        }

        Long boardId = card.getBoardColumn().getId(); // Supondo que o Card tem o boardId
        BoardColumn cancellationColumn = boardColumnDAO.getColumnByBoardId(boardId).stream()
                .filter(column -> "Cancelamento".equals(column.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Coluna 'Cancelamento' não encontrada para o board " + boardId));

        cardDAO.cancelCard(cardId);
    }

    @Override
    public void deleteCard(Long id) throws CardNotFoundException, SQLException {
        Card card = cardDAO.getCardById(id);
        if (card == null) {
            throw new CardNotFoundException("Card com ID " + id + " não encontrado.");
        }
        cardDAO.cancelCard(id);
    }

    private boolean isCardBlocked(Long cardId) throws SQLException {
        List<Block> blockHistory = blockDAO.getBlockHistoryByCardId(cardId);
        return blockHistory.stream().anyMatch(block -> block.getBlockIn() == null || block.getBlockIn().isAfter(OffsetDateTime.from(java.time.LocalDateTime.now())));
    }
}