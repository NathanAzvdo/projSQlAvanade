package com.sqlproject.persistence.dao;

import com.sqlproject.persistence.config.ConnectionConfig;
import com.sqlproject.persistence.entity.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class CardDAO {
    private final Connection connection;

    public CardDAO() throws SQLException, java.sql.SQLException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        this.connection = connectionConfig.getConnection();
    }

    public void createCard(Card card) throws SQLException {
        String sql = "INSERT INTO card (title, description, board_column_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, card.getTitle());
            stmt.setString(2, card.getDescription());
            stmt.setLong(3, card.getBoardColumn().getId());
            stmt.executeUpdate();
        } catch (java.sql.SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Card getCardById(Long id) throws SQLException {
        String sql = "SELECT c.id, c.title, c.description, c.created_at, c.status, c.board_column_id, " +
                "bc.id as column_id, bc.name as column_name, bc.column_order, bc.kind " +
                "FROM card c " +
                "JOIN board_column bc ON c.board_column_id = bc.id " +
                "WHERE c.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BoardColumn boardColumn = new BoardColumn(
                            rs.getLong("column_id"),
                            rs.getString("column_name"),
                            rs.getInt("column_order"),
                            rs.getString("kind"),
                            null // board será null, você pode adicionar a lógica de buscar o board se necessário
                    );

                    return new Card(
                            rs.getLong("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getObject("created_at", OffsetDateTime.class),
                            Status.valueOf(rs.getString("status")),
                            boardColumn
                    );
                }
            }
        } catch (java.sql.SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public List<Card> getCardsByBoardsId(Long boardId) throws SQLException {
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT c.id, c.title, c.description, c.created_at, c.status, c.board_column_id, " +
                "bc.id as column_id, bc.name as column_name, bc.column_order, bc.kind " +
                "FROM card c " +
                "JOIN board_column bc ON c.board_column_id = bc.id " +
                "WHERE bc.board_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, boardId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BoardColumn boardColumn = new BoardColumn(
                            rs.getLong("column_id"),
                            rs.getString("column_name"),
                            rs.getInt("column_order"),
                            rs.getString("kind"),
                            null // board será null, você pode adicionar a lógica de buscar o board se necessário
                    );

                    cards.add(new Card(
                            rs.getLong("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getObject("created_at", OffsetDateTime.class),
                            Status.valueOf(rs.getString("status")),
                            boardColumn
                    ));
                }
            }
        } catch (java.sql.SQLException e) {
            throw new RuntimeException(e);
        }

        return cards;
    }

    public void moveCard(Long cardId, Long targetColumnId) throws SQLException {
        String sql = "UPDATE card SET board_column_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, targetColumnId);
            stmt.setLong(2, cardId);
            stmt.executeUpdate();
        } catch (java.sql.SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void cancelCard(Long cardId) throws SQLException {
        String sql = "DELETE FROM card WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cardId);
            stmt.executeUpdate();
        } catch (java.sql.SQLException e) {
            throw new RuntimeException(e);
        }
    }
}