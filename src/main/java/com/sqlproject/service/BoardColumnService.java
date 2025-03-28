package com.sqlproject.service;

import com.sqlproject.exception.BoardColumnNotFoundException;
import com.sqlproject.persistence.entity.BoardColumn;

import java.util.List;

public interface BoardColumnService {

    BoardColumn createColumn(BoardColumn column) throws IllegalArgumentException;

    List<BoardColumn> getColumnsByBoardId(Long boardId);

    void updateColumnOrder(Long columnId, int newOrder) throws BoardColumnNotFoundException, IllegalArgumentException;

    void deleteColumn(Long columnId) throws BoardColumnNotFoundException;
}