package ar.edu.unlam.tallerweb1.infrastructure.utils;

import java.sql.*;
import java.time.LocalDateTime;

public class JdbcUtil {

    public static PreparedStatement prepareStatement(Connection connection, String sql) {
        try {
            return connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setLong(int index, Long value, PreparedStatement preparedStatement) {
        try {
            preparedStatement.setLong(index, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setDouble(int index, Double value, PreparedStatement preparedStatement) {
        try {
            preparedStatement.setDouble(index, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setString(int index, String value, PreparedStatement preparedStatement) {
        try {
            preparedStatement.setString(index, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setDate(int index, LocalDateTime value, PreparedStatement preparedStatement) {
        try {
            preparedStatement.setDate(index, Date.valueOf(value.toLocalDate()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet executeQuery(PreparedStatement preparedStatement) {
        try {
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int execute(PreparedStatement preparedStatement) {
        try {
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean next(ResultSet resultSet) {
        try {
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static long getLong(String column, ResultSet resultSet) {
        try {
            return resultSet.getLong(column);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static double getDouble(String column, ResultSet resultSet) {
        try {
            return resultSet.getDouble(column);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getString(String column, ResultSet resultSet) {
        try {
            return resultSet.getString(column);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static LocalDateTime getDate(String column, ResultSet resultSet) {
        try {
            return resultSet.getTimestamp("date").toLocalDateTime();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
