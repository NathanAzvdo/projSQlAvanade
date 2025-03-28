package com.sqlproject.persistence.migration;

import com.sqlproject.persistence.config.ConnectionConfig;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.AllArgsConstructor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
public class MigrationStrategy implements AutoCloseable {
    private final ConnectionConfig connectionConfig;

    public MigrationStrategy() throws SQLException {
        this.connectionConfig = new ConnectionConfig();
    }

    public void executeMigration() {
        var originalOut = System.out;
        var originalErr = System.err;

        try (var fos = new FileOutputStream("liquibase.log");
             var printStream = new PrintStream(fos)) {

            System.setOut(printStream);
            System.setErr(printStream);

            try (var connection = connectionConfig.getConnection()) {
                var jdbcConnection = new JdbcConnection(connection);
                var liquibase = new Liquibase(
                        "db/changelog/db.changelog-master.yml",
                        new ClassLoaderResourceAccessor(),
                        jdbcConnection
                );
                liquibase.update();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Migration failed", e);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Failed to create log file", ex);
        } finally {
            System.setOut(originalOut);
            System.setErr(originalErr);
        }
    }

    @Override
    public void close() throws SQLException {
        if (connectionConfig != null) {
            connectionConfig.close();
        }
    }
}