package ar.edu.unlam.tallerweb1.infrastructure

import ar.edu.unlam.tallerweb1.infrastructure.utils.DataSource
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import java.sql.Connection
import java.sql.SQLException

abstract class JdbcRepository(private val dataSource: DataSource) {
    private val logger: Logger = getLogger(JdbcRepository::class.java)

    @Throws(RuntimeException::class)
    protected fun <T> searchInTransaction(function: (Connection, String) -> List<T>, inputValue: String):  List<T> {
        try{
            return function(dataSource.getConnection(), inputValue)
        } catch(e: SQLException) {
            logger.error(e.message, e)
            throw RuntimeException(e);
        }
    }

    protected fun <T> findInTransaction(function: (Connection, String) -> T?, inputValue: String): T?{
        try{
            return function(dataSource.getConnection(), inputValue)
        } catch(e: SQLException) {
            logger.error(e.message, e)
            throw RuntimeException(e);
        }
    }

    protected fun executeInTransaction(function: (Connection) -> Unit) {
        try{
            function(dataSource.getConnection())
        } catch(e: SQLException) {
            logger.error(e.message, e)
            throw RuntimeException(e);
        }
    }

}