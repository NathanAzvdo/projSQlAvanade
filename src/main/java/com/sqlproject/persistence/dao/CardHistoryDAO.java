package com.sqlproject.persistence.dao;

import com.sqlproject.persistence.config.ConnectionConfig;
import com.sqlproject.persistence.entity.BoardColumn;
import com.sqlproject.persistence.entity.Card;
import com.sqlproject.persistence.entity.CardHistory;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class CardHistoryDAO {
    private final Connection connection;

    public CardHistoryDAO() throws SQLException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        this.connection = connectionConfig.getConnection();
    }

    public List<CardHistory> getHistoryByCardId(Long cardId) throws SQLException {
        List<CardHistory> historyList = new ArrayList<>();
        String sql = "SELECT ch.id, ch.entered_at, ch.exited_at, " +
                "ch.card_id, c.title, c.description, " +
                "ch.board_column_id, bc.name as column_name, bc.column_order, bc.kind " +
                "FROM card_history ch " +
                "JOIN card c ON ch.card_id = c.id " +
                "JOIN board_column bc ON ch.board_column_id = bc.id " +
                "WHERE ch.card_id = ? ORDER BY ch.entered_at";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cardId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {

                    Card card = new Card(
                            rs.getLong("card_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            null,
                            null,
                            null
                    );

                    BoardColumn boardColumn = new BoardColumn(
                            rs.getLong("board_column_id"),
                            rs.getString("column_name"),
                            rs.getInt("column_order"),
                            rs.getString("kind"),
                            null
                    );
                    historyList.add(new CardHistory(
                            rs.getLong("id"),
                            card,
                            boardColumn,
                            rs.getObject("entered_at", OffsetDateTime.class),
                            rs.getObject("exited_at", OffsetDateTime.class) // pode ser null
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return historyList;
    }
}