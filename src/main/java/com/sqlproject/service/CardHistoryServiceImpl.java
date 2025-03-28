package com.sqlproject.service;

import com.sqlproject.persistence.dao.CardHistoryDAO;
import com.sqlproject.persistence.entity.CardHistory;

import java.sql.SQLException;
import java.util.List;

public class CardHistoryServiceImpl implements CardHistoryService {

    private final CardHistoryDAO cardHistoryDAO;

    public CardHistoryServiceImpl(CardHistoryDAO cardHistoryDAO) {
        this.cardHistoryDAO = cardHistoryDAO;
    }

    @Override
    public List<CardHistory> getHistoryByCardId(Long cardId) throws SQLException {
        return cardHistoryDAO.getHistoryByCardId(cardId);
    }
}