package com.sqlproject.service;

import com.sqlproject.exceptions.BoardColumnNotFoundException;
import com.sqlproject.persistence.dao.BoardColumnDAO;
import com.sqlproject.persistence.entity.BoardColumn;

import java.sql.SQLException;
import java.util.List;

public class BoardColumnServiceImpl implements BoardColumnService {

    private final BoardColumnDAO boardColumnDAO;

    public BoardColumnServiceImpl() throws SQLException {
        this.boardColumnDAO = new BoardColumnDAO();
    }

    @Override
    public BoardColumn createColumn(BoardColumn column) throws IllegalArgumentException {
        if (column == null || column.getName() == null || column.getName().isEmpty() ||
                column.getBoard() == null || column.getBoard().getId() == null) {
            throw new IllegalArgumentException("Dados da coluna inválidos. Nome e board são obrigatórios.");
        }

        try {
            boardColumnDAO.createColumn(column);
            return column;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar coluna: " + e.getMessage(), e);
        }
    }

    @Override
    public List<BoardColumn> getColumnsByBoardId(Long boardId) {
        if (boardId == null) {
            throw new IllegalArgumentException("ID do board não pode ser nulo.");
        }

        try {
            return boardColumnDAO.getColumnByBoardId(boardId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar colunas do board: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateColumnOrder(Long columnId, int newOrder) throws BoardColumnNotFoundException, IllegalArgumentException {
        if (columnId == null) {
            throw new IllegalArgumentException("ID da coluna não pode ser nulo.");
        }

        if (newOrder < 0) {
            throw new IllegalArgumentException("A ordem da coluna não pode ser negativa.");
        }

        try {
            // Verificar se a coluna existe primeiro
            List<BoardColumn> allColumns = boardColumnDAO.getColumnByBoardId(null); // Precisamos adaptar o DAO para buscar por ID da coluna
            boolean columnExists = allColumns.stream().anyMatch(c -> c.getId().equals(columnId));

            if (!columnExists) {
                throw new BoardColumnNotFoundException("Coluna com ID " + columnId + " não encontrada.");
            }

            boardColumnDAO.updateColumnOrder(columnId, newOrder);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar a ordem da coluna: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteColumn(Long columnId) throws BoardColumnNotFoundException {
        if (columnId == null) {
            throw new IllegalArgumentException("ID da coluna não pode ser nulo.");
        }

        try {
            // Verificar se a coluna existe primeiro
            List<BoardColumn> allColumns = boardColumnDAO.getColumnByBoardId(null); // Precisamos adaptar o DAO para buscar por ID da coluna
            boolean columnExists = allColumns.stream().anyMatch(c -> c.getId().equals(columnId));

            if (!columnExists) {
                throw new BoardColumnNotFoundException("Coluna com ID " + columnId + " não encontrada.");
            }

            boardColumnDAO.deleteColumn(columnId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir a coluna: " + e.getMessage(), e);
        }
    }
}