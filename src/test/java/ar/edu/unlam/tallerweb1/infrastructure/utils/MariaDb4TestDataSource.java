package ar.edu.unlam.tallerweb1.infrastructure.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.slf4j.LoggerFactory.getLogger;

public class MariaDb4TestDataSource implements DataSource {

    private final String URL = "jdbc:mysql://localhost";
    private final String USER = "root";
    private final String PASSWORD = "";
    private static final Logger LOGGER = getLogger(MariaDb4TestDataSource.class);
    private static final MariaDb4TestDataSource INSTANCE = new MariaDb4TestDataSource();

    private MariaDb4TestDataSource() {
        try {
            createDB();
            createTables();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void createTables() throws SQLException {
        try {
            getConnection().createStatement().execute("drop table payment");
            getConnection().createStatement().execute("drop table shared_expenses");
            getConnection().createStatement().execute("drop table user");
            getConnection().createStatement().execute("drop table friends_group");;
        } catch (Exception ignored) {
        }

        getConnection()
                .createStatement().execute("create table friends_group(\n" +
                        "      id INT NOT NULL AUTO_INCREMENT,\n" +
                        "      name VARCHAR(100) NOT NULL,\n" +
                        "      PRIMARY KEY ( id )\n" +
                        ")");

        getConnection()
                .createStatement().execute("create table user(\n" +
                        "     id INT NOT NULL AUTO_INCREMENT,\n" +
                        "     name VARCHAR(100) NOT NULL,\n" +
                        "     friends_group_id INT,\n" +
                        "     FOREIGN KEY (friends_group_id) REFERENCES friends_group(id),\n" +
                        "     PRIMARY KEY ( id )\n" +
                        ")");

        getConnection()
                .createStatement().execute("create table shared_expenses(\n" +
                        "    id INT NOT NULL AUTO_INCREMENT,\n" +
                        "    friends_group_id INT NOT NULL,\n" +
                        "    owner INT NOT NULL,\n" +
                        "    amount FLOAT NOT NULL,\n" +
                        "    detail VARCHAR(100) NOT NULL,\n" +
                        "    status VARCHAR(10) NOT NULL,\n" +
                        "    date DATETIME,\n" +
                        "    FOREIGN KEY (friends_group_id) REFERENCES friends_group(id),\n" +
                        "    FOREIGN KEY (owner) REFERENCES user(id),\n" +
                        "    PRIMARY KEY ( id )\n" +
                        ")");

        getConnection()
                .createStatement().execute("create table payment(\n" +
                        "    id INT NOT NULL AUTO_INCREMENT,\n" +
                        "    payer INT NOT NULL,\n" +
                        "    expent_id INT NOT NULL,\n" +
                        "    amount FLOAT NOT NULL,\n" +
                        "    FOREIGN KEY (payer) REFERENCES user(id),\n" +
                        "    FOREIGN KEY (expent_id) REFERENCES shared_expenses(id),\n" +
                        "    PRIMARY KEY ( id )\n" +
                        ")");
    }

    private void createDB() {

        try {
            DriverManager.getConnection(URL, USER, PASSWORD)
                    .createStatement()
                    .execute("CREATE DATABASE IF NOT EXISTS test");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MariaDb4TestDataSource instance() {
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL + "/test", USER, PASSWORD);
    }

}
