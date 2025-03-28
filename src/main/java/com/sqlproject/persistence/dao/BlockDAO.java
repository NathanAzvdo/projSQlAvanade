package com.sqlproject.persistence.dao;

import com.sqlproject.persistence.config.ConnectionConfig;
import com.sqlproject.persistence.entity.Block;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BlockDAO {

    private final Connection connection;

    public BlockDAO() throws SQLException {
        this.connection = ConnectionConfig.getConnection();
    }

    public void blockCard(int cardId, String cause) {
    }

    public void unblockCard(int cardId, String cause) {
    }

    public List<Block> getBlockHistoryByCardId(Long cardId) {
        return null;
    }
}
