package ar.edu.unlam.tallerweb1.infrastructure.utils

import java.sql.Connection
import java.sql.Date.valueOf
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.time.LocalDateTime

object JdbcUtil {
    fun prepareStatement(connection: Connection, sql: String): PreparedStatement {
        try {
            return connection.prepareStatement(sql)
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

fun PreparedStatement.withLong(index: Int, value: Long): PreparedStatement {
    try {
        this.setLong(index, value)
    } catch (e: SQLException) {
        throw RuntimeException(e)
    }
    return this
}

fun PreparedStatement.withDouble(index: Int, value: Double): PreparedStatement {
    try {
        this.setDouble(index, value)
    } catch (e: SQLException) {
        throw RuntimeException(e)
    }
    return this
}

fun PreparedStatement.withString(index: Int, value: String): PreparedStatement {
    try {
        this.setString(index, value)
    } catch (e: SQLException) {
        throw RuntimeException(e)
    }
    return this
}

fun PreparedStatement.withDate(index: Int, value: LocalDateTime): PreparedStatement {
    try {
        this.setDate(index, valueOf(value.toLocalDate()))
    } catch (e: SQLException) {
        throw RuntimeException(e)
    }
    return this
}