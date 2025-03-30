package com.sqlproject.service;

import com.sqlproject.exceptions.CardNotFoundException;
import com.sqlproject.persistence.dao.BlockDAO;
import com.sqlproject.persistence.dao.CardDAO;
import com.sqlproject.persistence.entity.Block;
import com.sqlproject.persistence.entity.Card;

import java.sql.SQLException;
import java.util.List;

public class BlockServiceImpl implements BlockService {

    private final BlockDAO blockDAO;
    private final CardDAO cardDAO;

    public BlockServiceImpl() throws SQLException {
        this.blockDAO = new BlockDAO();
        this.cardDAO = new CardDAO();
    }

    @Override
    public void blockCard(Long cardId, String reason) throws CardNotFoundException, SQLException {
        Card card = cardDAO.getCardById(cardId);
        if (card == null) {
            throw new CardNotFoundException("Card com ID " + cardId + " não encontrado.");
        }

        blockDAO.blockCard(cardId, reason);
    }

    @Override
    public void unblockCard(Long cardId, String reason) throws CardNotFoundException, SQLException {
        Card card = cardDAO.getCardById(cardId);
        if (card == null) {
            throw new CardNotFoundException("Card com ID " + cardId + " não encontrado.");
        }

        blockDAO.unblockCard(cardId, reason);
    }

    @Override
    public List<Block> getBlockHistory(Long cardId) throws SQLException {
        return blockDAO.getBlockHistoryByCardId(cardId);
    }
}