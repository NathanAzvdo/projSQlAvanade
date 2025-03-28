package com.sqlproject.persistence.dao;

import com.sqlproject.persistence.config.ConnectionConfig;
import com.sqlproject.persistence.entity.Block;
import com.sqlproject.persistence.entity.BoardColumn;
import com.sqlproject.persistence.entity.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class BlockDAO {
    private final Connection connection;

    public BlockDAO() throws SQLException, java.sql.SQLException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        this.connection = connectionConfig.getConnection();
    }

    public void blockCard(int cardId, String cause) throws SQLException {
        String sql = "INSERT INTO block (card_id, cause, unblock_in) VALUES (?, ?, NULL)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cardId);
            stmt.setString(2, cause);
            stmt.executeUpdate();
        } catch (java.sql.SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void unblockCard(int cardId, String cause) throws SQLException {
        String sql = "UPDATE block SET unblock_in = NOW() WHERE card_id = ? AND unblock_in IS NULL";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cardId);
            stmt.executeUpdate();
        } catch (java.sql.SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Block> getBlockHistoryByCardId(Long cardId) throws SQLException {
        List<Block> blocks = new ArrayList<>();
        String sql = "SELECT b.id, b.card_id, b.block_cause, b.block_in, b.unblock_cause, b.unblock_in, " +
                "c.title as card_title, c.description as card_description, " +
                "bc.id as board_column_id, bc.name as board_column_name, bc.column_order, bc.kind " +
                "FROM block b " +
                "JOIN card c ON b.card_id = c.id " +
                "JOIN board_column bc ON b.board_column_id = bc.id " +
                "WHERE b.card_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cardId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Cria o Card
                    Card card = new Card(
                            rs.getLong("card_id"),
                            rs.getString("card_title"),
                            rs.getString("card_description"),
                            null, // createdAt
                            null, // status
                            null  // boardColumn
                    );

                    // Cria a BoardColumn
                    BoardColumn boardColumn = new BoardColumn(
                            rs.getLong("board_column_id"),
                            rs.getString("board_column_name"),
                            rs.getInt("column_order"),
                            rs.getString("kind"),
                            null // board
                    );

                    // Cria o Block
                    blocks.add(new Block(
                            rs.getLong("id"),
                            card,
                            boardColumn,
                            rs.getString("block_cause"),
                            rs.getObject("block_in", OffsetDateTime.class),
                            rs.getString("unblock_cause"),
                            rs.getObject("unblock_in", OffsetDateTime.class)
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return blocks;
    }
}
