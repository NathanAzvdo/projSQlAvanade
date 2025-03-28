package com.sqlproject.persistence.dao;

import com.sqlproject.persistence.config.ConnectionConfig;
import com.sqlproject.persistence.entity.Board;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class BoardDAO {

    private final Connection connection;

    public BoardDAO() throws SQLException {
        this.connection = ConnectionConfig.getConnection();
    }

    public void createBoard(Board board){

    }

    public Optional<Board> getBoardById(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID do board inválido: " + id);
        }

        String sql = "SELECT * FROM board WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Board board = new Board();
                    board.setId(resultSet.getLong("id"));
                    String name = resultSet.getString("name");
                    if (name == null || name.trim().isEmpty()) {
                        throw new SQLException("Nome do board não pode ser nulo ou vazio");
                    }
                    board.setName(name);

                    return Optional.of(board);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new SQLException("Erro ao buscar board com ID " + id + ": " + e.getMessage(), e);
        }
    }

    public List<Board> getAllBoards(){
        return null;
    }

    public void deleteBoard(Long id){

    }

    public Board updateBoard(Board board){
        return null;
    }
}
