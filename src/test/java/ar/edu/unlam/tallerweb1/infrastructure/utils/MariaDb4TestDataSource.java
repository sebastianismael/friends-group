package ar.edu.unlam.tallerweb1.infrastructure.utils;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MariaDb4TestDataSource implements DataSource {

    private static Logger logger = LoggerFactory.getLogger(MariaDb4TestDataSource.class);
    private static MariaDb4TestDataSource instance = new MariaDb4TestDataSource();
    private MariaDb4TestDataSource(){
        try {
            DB database = DB.newEmbeddedDB(3306);
            database.start();
            database.source("create-tables.sql");
        } catch (ManagedProcessException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static MariaDb4TestDataSource instance(){
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost/test", "root", "");
    }

}
