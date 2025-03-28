package com.sqlproject.service;

import com.sqlproject.persistence.entity.CardHistory;

import java.sql.SQLException;
import java.util.List;

public interface CardHistoryService {
    List<CardHistory> getHistoryByCardId(Long cardId) throws SQLException;
}