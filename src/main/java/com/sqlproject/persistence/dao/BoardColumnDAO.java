package com.sqlproject.persistence.dao;

import com.sqlproject.persistence.config.ConnectionConfig;
import com.sqlproject.persistence.entity.BoardColumn;
import com.sqlproject.persistence.entity.Card;
import com.sqlproject.persistence.entity.CardHistory;
import com.sqlproject.persistence.entity.Block;

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
        String sql = "INSERT INTO board_column (name, board_id, column_order) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, column.getName());
            stmt.setLong(2, column.getBoard().getId());
            stmt.setInt(3, column.getOrder());
            stmt.executeUpdate();
        }
    }

    public List<BoardColumn> getColumnByBoardId(Long boardId) throws SQLException {
        List<BoardColumn> columns = new ArrayList<>();
        String sql = "SELECT * FROM board_column WHERE board_id = ? ORDER BY column_order";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, boardId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    columns.add(new BoardColumn(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getInt("column_order"),
                            rs.getString("kind"),
                            null // board será null, você pode adicionar a lógica de buscar o board se necessário
                    ));
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