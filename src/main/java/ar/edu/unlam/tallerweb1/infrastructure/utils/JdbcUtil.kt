package ar.edu.unlam.tallerweb1.infrastructure.utils

import java.sql.*
import java.sql.Date.valueOf
import java.time.LocalDateTime

object JdbcUtil {
    fun prepareStatement(connection: Connection, sql: String): PreparedStatement {
        try {
            return connection.prepareStatement(sql)
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }

    fun setLong(index: Int, value: Long, preparedStatement: PreparedStatement) {
        try {
            preparedStatement.setLong(index, value)
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }

    fun setDouble(index: Int, value: Double, preparedStatement: PreparedStatement) {
        try {
            preparedStatement.setDouble(index, value)
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }

    fun setString(index: Int, value: String, preparedStatement: PreparedStatement) {
        try {
            preparedStatement.setString(index, value)
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }

    fun setDate(index: Int, value: LocalDateTime, preparedStatement: PreparedStatement) {
        try {
            preparedStatement.setDate(index, valueOf(value.toLocalDate()))
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }

    fun executeQuery(preparedStatement: PreparedStatement): ResultSet {
        try {
            return preparedStatement.executeQuery()
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }

    fun execute(preparedStatement: PreparedStatement): Int {
        try {
            return preparedStatement.executeUpdate()
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }

    fun next(resultSet: ResultSet): Boolean {
        try {
            return resultSet.next()
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }

    fun getLong(column: String, resultSet: ResultSet): Long {
        try {
            return resultSet.getLong(column)
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }

    fun getDouble(column: String, resultSet: ResultSet): Double {
        try {
            return resultSet.getDouble(column)
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }

    fun getString(column: String, resultSet: ResultSet): String {
        try {
            return resultSet.getString(column)
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }

    fun getDate(column: String, resultSet: ResultSet): LocalDateTime {
        try {
            return resultSet.getTimestamp("date").toLocalDateTime()
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }
}
