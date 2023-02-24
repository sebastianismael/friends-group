package ar.edu.unlam.tallerweb1.infrastructure;

import ar.edu.unlam.tallerweb1.infrastructure.utils.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public abstract class JdbcRepository {

    private DataSource dataSource;

    public JdbcRepository(DataSource dataSource){

        this.dataSource = dataSource;
    }
    private static Logger logger = LoggerFactory.getLogger(JdbcRepository.class);
    protected <T> List<T> searchInTransaction(BiFunction<Connection, String, List<T>> function, String inputValue){
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            return function.apply(conn, inputValue);

        } catch (SQLException e) {
            error(e);
            throw new RuntimeException(e);
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                error(e);
                throw new RuntimeException(e);
            }
        }
    }

    protected <T> T findInTransaction(BiFunction<Connection, String, T> function, String inputValue){
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            return function.apply(conn, inputValue);

        } catch (SQLException e) {
            error(e);
            throw new RuntimeException(e);
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                error(e);
                throw new RuntimeException(e);
            }
        }
    }

    protected <T> void executeInTransaction(Consumer<Connection> function){
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            function.accept(conn);

        } catch (SQLException e) {
            error(e);
            throw new RuntimeException(e);
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                error(e);
                throw new RuntimeException(e);
            }
        }
    }

    private static void error(Exception e) {
        logger.error(e.getMessage(), e);
    }
}
