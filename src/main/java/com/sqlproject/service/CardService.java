package com.sqlproject.service;

import com.sqlproject.persistence.entity.Card;
import com.sqlproject.exceptions.*;

import java.sql.SQLException;
import java.util.List;

public interface CardService {

    Card createCard(Card card, Long boardId) throws IllegalArgumentException, BoardNotFoundException, SQLException;

    Card getCardById(Long id) throws CardNotFoundException, SQLException;

    List<Card> getCardsByBoardId(Long boardId) throws SQLException;

    void moveCard(Long cardId, Long targetColumnId) throws CardNotFoundException, BoardColumnNotFoundException, InvalidCardOperationException, SQLException;

    void cancelCard(Long cardId) throws CardNotFoundException, BoardColumnNotFoundException, SQLException;

    void deleteCard(Long id) throws CardNotFoundException, SQLException;
}