package com.sqlproject.service;

import com.sqlproject.persistence.dao.BoardDAO;
import com.sqlproject.persistence.entity.Board;
import lombok.AllArgsConstructor;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class BoardService {
    private final BoardDAO boardDAO;

    public Board createBoard(String name) throws SQLException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Board name cannot be null or empty");
        }

        Board board = new Board();
        board.setName(name);
        boardDAO.createBoard(board);
        return board;
    }

    public Optional<Board> getBoardById(Long id) throws SQLException {
        return boardDAO.getBoardById(id);
    }

    public List<Board> getAllBoards() throws SQLException {
        return boardDAO.getAllBoards();
    }

    public void deleteBoard(Long id) throws SQLException {
        boardDAO.deleteBoard(id);
    }

    public Board updateBoard(Long id, String newName) throws SQLException {
        Board board = boardDAO.getBoardById(id)
                .orElseThrow(() -> new SQLException("Board not found with id: " + id));

        board.setName(newName);
        return boardDAO.updateBoard(board);
    }

    public void close() throws SQLException {
        boardDAO.close();
    }
}