package com.sqlproject.service;

import com.sqlproject.exceptions.BoardNotFoundException;
import com.sqlproject.persistence.entity.Board;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BoardService {

    Board createBoard(Board board) throws IllegalArgumentException, SQLException;

    Optional<Board> getBoardById(Long id) throws BoardNotFoundException, SQLException;

    List<Board> getAllBoards() throws SQLException;

    void deleteBoard(Long id) throws SQLException, BoardNotFoundException;
}
