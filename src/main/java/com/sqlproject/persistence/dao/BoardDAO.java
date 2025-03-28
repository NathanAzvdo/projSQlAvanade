package com.sqlproject.persistence.dao;

import com.sqlproject.persistence.config.ConnectionConfig;
import com.sqlproject.persistence.entity.Board;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BoardDAO {

    private final Connection connection;

    public BoardDAO() throws SQLException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        this.connection = connectionConfig.getConnection();
    }

    public void createBoard(Board board) throws SQLException {
        if (board == null) {
            throw new IllegalArgumentException("Board não pode ser nulo");
        }
        if (board.getName() == null || board.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do board não pode ser nulo ou vazio");
        }

        String sql = "INSERT INTO board (nome) VALUES (?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, board.getName());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Falha ao criar board, nenhuma linha afetada");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    board.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Falha ao criar board, nenhum ID obtido");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Erro ao criar board: " + e.getMessage(), e);
        }
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

                    String nome = resultSet.getString("nome");
                    if (nome == null || nome.trim().isEmpty()) {
                        throw new SQLException("Nome do board não pode ser nulo ou vazio");
                    }
                    board.setName(nome);

                    return Optional.of(board);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new SQLException("Erro ao buscar board com ID " + id + ": " + e.getMessage(), e);
        }
    }

    public List<Board> getAllBoards() throws SQLException {
        String sql = "SELECT * FROM board ORDER BY nome";
        List<Board> boards = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Board board = new Board();
                board.setId(resultSet.getLong("id"));
                board.setName(resultSet.getString("nome"));

                boards.add(board);
            }
        } catch (SQLException e) {
            throw new SQLException("Erro ao buscar todos os boards: " + e.getMessage(), e);
        }

        return boards;
    }

    public void deleteBoard(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID do board inválido: " + id);
        }

        String sql = "DELETE FROM board WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Nenhum board encontrado com ID: " + id);
            }
        } catch (SQLException e) {
            throw new SQLException("Erro ao deletar board com ID " + id + ": " + e.getMessage(), e);
        }
    }

    public Board updateBoard(Board board) throws SQLException {
        if (board == null) {
            throw new IllegalArgumentException("Board não pode ser nulo");
        }
        if (board.getId() == null || board.getId() <= 0) {
            throw new IllegalArgumentException("ID do board inválido");
        }
        if (board.getName() == null || board.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do board não pode ser nulo ou vazio");
        }

        String sql = "UPDATE board SET nome = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, board.getName());
            statement.setLong(2, board.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Nenhum board encontrado com ID: " + board.getId());
            }

            return board;
        } catch (SQLException e) {
            throw new SQLException("Erro ao atualizar board: " + e.getMessage(), e);
        }
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}