package com.sqlproject.service;
import com.sqlproject.exceptions.BoardNotFoundException;
import com.sqlproject.persistence.dao.BoardColumnDAO;
import com.sqlproject.persistence.dao.BoardDAO;
import com.sqlproject.persistence.entity.Board;
import com.sqlproject.persistence.entity.BoardColumn;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BoardServiceImpl implements BoardService {

    private final BoardDAO boardDAO;
    private final BoardColumnDAO boardColumnDAO;

    public BoardServiceImpl() throws SQLException {
        this.boardDAO = new BoardDAO();
        this.boardColumnDAO = new BoardColumnDAO();
    }

    @Override
    public Board createBoard(Board board) throws IllegalArgumentException, SQLException {
        if (board == null || board.getName() == null || board.getName().isEmpty()) {
            throw new IllegalArgumentException("Nome do board não pode ser nulo ou vazio.");
        }

        boardDAO.createBoard(board);

        List<BoardColumn> columns = new ArrayList<>();


        BoardColumn backlogColumn = new BoardColumn();
        backlogColumn.setBoard(board);
        backlogColumn.setName("Backlog");
        backlogColumn.setOrder(0);
        backlogColumn.setKind("Idea");
        columns.add(backlogColumn);

        BoardColumn toDoColumn = new BoardColumn();
        toDoColumn.setBoard(board);
        toDoColumn.setName("To Do");
        toDoColumn.setOrder(1);
        toDoColumn.setKind("Task");
        columns.add(toDoColumn);


        BoardColumn inProgressColumn = new BoardColumn();
        inProgressColumn.setBoard(board);
        inProgressColumn.setName("In Progress");
        inProgressColumn.setOrder(2);
        inProgressColumn.setKind("Task");
        columns.add(inProgressColumn);


        BoardColumn reviewColumn = new BoardColumn();
        reviewColumn.setBoard(board);
        reviewColumn.setName("Review/QA");
        reviewColumn.setOrder(3);
        reviewColumn.setKind("Task");
        columns.add(reviewColumn);


        BoardColumn doneColumn = new BoardColumn();
        doneColumn.setBoard(board);
        doneColumn.setName("Done");
        doneColumn.setOrder(4);
        doneColumn.setKind("Task");
        columns.add(doneColumn);

        boardColumnDAO.createColumns(columns);

        return board;
    }


    @Override
    public Optional<Board> getBoardById(Long id) throws BoardNotFoundException, SQLException {
        Optional<Board> board = boardDAO.getBoardById(id);
        if (board.isEmpty()) {
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
        Optional<Board> board = boardDAO.getBoardById(id);
        if (board == null) {
            throw new BoardNotFoundException("Board com ID " + id + " não encontrado.");
        }

        boardDAO.deleteBoard(id);
    }
}

