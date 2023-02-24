package ar.edu.unlam.tallerweb1.infrastructure.utils;

import java.sql.Connection;
import java.sql.SQLException;

public interface DataSource {
    Connection getConnection() throws SQLException;
}
