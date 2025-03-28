package com.sqlproject.service;

import com.sqlproject.exceptions.CardNotFoundException;
import com.sqlproject.persistence.entity.Block;

import java.sql.SQLException;
import java.util.List;

public interface BlockService {

    void blockCard(Long cardId, String reason) throws CardNotFoundException, SQLException;

    void unblockCard(Long cardId, String reason) throws CardNotFoundException, SQLException;

    List<Block> getBlockHistory(Long cardId) throws SQLException;
}