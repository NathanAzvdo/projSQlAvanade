package com.sqlproject.persistence.dao;

import com.sqlproject.persistence.config.ConnectionConfig;
import com.sqlproject.persistence.entity.Board;
import com.sqlproject.persistence.entity.BoardColumn;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BoardColumnDAO {
    private final Connection connection;

    public BoardColumnDAO() throws SQLException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        this.connection = connectionConfig.getConnection();
    }


    public void createColumn(BoardColumn column) throws SQLException {
        String sql = "INSERT INTO board_column (name, board_id, column_order, kind) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, column.getName());
            stmt.setLong(2, column.getBoard().getId());
            stmt.setInt(3, column.getOrder());
            stmt.setString(4, column.getKind());
            stmt.executeUpdate();
        }
    }

    public void createColumns(List<BoardColumn> columns) throws SQLException {
        String sql = "INSERT INTO board_column (name, board_id, column_order, kind) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (BoardColumn column : columns) {
                stmt.setString(1, column.getName());
                stmt.setLong(2, column.getBoard().getId());
                stmt.setInt(3, column.getOrder());
                stmt.setString(4, column.getKind());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    public List<BoardColumn> getColumnByBoardId(Long boardId) throws SQLException {
        List<BoardColumn> columns = new ArrayList<>();
        String sql = "SELECT id, name, column_order, kind, board_id FROM board_column WHERE board_id = ? ORDER BY column_order";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, boardId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Board board = new Board();
                    board.setId(rs.getLong("board_id")); // Cria um objeto Board com o ID correto

                    BoardColumn column = new BoardColumn(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getInt("column_order"),
                            rs.getString("kind"),
                            board // Agora o board não será mais null
                    );

                    columns.add(column);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return columns;
    }

    public void updateColumnOrder(Long columnId, int newOrder) throws SQLException {
        String sql = "UPDATE board_column SET column_order = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, newOrder);
            stmt.setLong(2, columnId);
            stmt.executeUpdate();
        }
    }

    public void deleteColumn(Long columnId) throws SQLException {
        String sql = "DELETE FROM board_column WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, columnId);
            stmt.executeUpdate();
        }
    }


}