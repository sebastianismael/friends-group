package ar.edu.unlam.tallerweb1.infrastructure.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySqlDataSource implements DataSource {

    private static Logger logger = LoggerFactory.getLogger(MySqlDataSource.class);
    private static MySqlDataSource instance = new MySqlDataSource();
    private MySqlDataSource(){}

    public static MySqlDataSource instance(){
        return instance;
    }

    public Connection getConnection() throws SQLException {
        Properties prop = new Properties();
        try (InputStream resourceAsStream = MySqlDataSource.class.getClassLoader().getResourceAsStream("db.properties")) {
            prop.load(resourceAsStream);
        } catch (IOException e) {
            logger.error("Unable to load properties file db.properties");
        } // TODO mover a una clase que se ocupe de esto

        String url = prop.getProperty("db.url");
        String user = prop.getProperty("db.username");
        String password = prop.getProperty("db.password");
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return DriverManager.getConnection(url, user, password);
    }

}
