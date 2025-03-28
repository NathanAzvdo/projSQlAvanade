package com.sqlproject.service;
import com.sqlproject.exception.BoardNotFoundException;
import com.sqlproject.persistence.dao.BoardColumnDAO;
import com.sqlproject.persistence.dao.BoardDAO;
import com.sqlproject.persistence.entity.Board;
import com.sqlproject.persistence.entity.BoardColumn;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BoardServiceImpl implements BoardService {

    private final BoardDAO boardDAO;
    private final BoardColumnDAO boardColumnDAO; // Dependência para criar colunas iniciais

    public BoardServiceImpl(BoardDAO boardDAO, BoardColumnDAO boardColumnDAO) {
        this.boardDAO = boardDAO;
        this.boardColumnDAO = boardColumnDAO;
    }

    @Override
    public Board createBoard(Board board) throws IllegalArgumentException, SQLException {
        // Validação básica
        if (board == null || board.getName() == null || board.getName().isEmpty()) {
            throw new IllegalArgumentException("Nome do board não pode ser nulo ou vazio.");
        }

        // Criar o board
        boardDAO.createBoard(board);

        // Criar as colunas obrigatórias
        BoardColumn initialColumn = new BoardColumn();
        initialColumn.setBoard(board);
        initialColumn.setName("Inicial");
        initialColumn.setOrder(1);
        boardColumnDAO.createColumn(initialColumn);

        BoardColumn finalColumn = new BoardColumn();
        finalColumn.setBoard(board);
        finalColumn.setName("Final");
        finalColumn.setOrder(2);
        boardColumnDAO.createColumn(finalColumn);

        BoardColumn cancellationColumn = new BoardColumn();
        cancellationColumn.setBoard(board);
        cancellationColumn.setName("Cancelamento");
        cancellationColumn.setOrder(3);
        boardColumnDAO.createColumn(cancellationColumn);

        return board;
    }


    @Override
    public Optional<Board> getBoardById(Long id) throws BoardNotFoundException, SQLException {
        Optional<Board> board = boardDAO.getBoardById(id);
        if (board == null) {
            throw new BoardNotFoundException("Board com ID " + id + " não encontrado.");
        }
        return board;
    }

    @Override
    public List<Board> getAllBoards() throws SQLException {
        return boardDAO.getAllBoards();
    }

    @Override
    public void deleteBoard(Long id) throws SQLException, BoardNotFoundException {
        // Verificar se o board existe
        Optional<Board> board = boardDAO.getBoardById(id);
        if (board == null) {
            throw new BoardNotFoundException("Board com ID " + id + " não encontrado.");
        }

        boardDAO.deleteBoard(id);
    }
}

