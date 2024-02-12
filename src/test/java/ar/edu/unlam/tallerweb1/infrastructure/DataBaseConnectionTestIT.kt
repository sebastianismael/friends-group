package ar.edu.unlam.tallerweb1.infrastructure

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import java.sql.SQLException

class DataBaseConnectionTestIT : JdbcRepositoryTestIT() {
    @Test
    fun checkDbConnection() {
        try {
            assertFalse(connection.isClosed)
        } catch (e: SQLException) {
            fail<Any>(e)
        }
    }
}
