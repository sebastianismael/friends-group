package ar.edu.unlam.tallerweb1.infrastructure.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import java.sql.DriverManager.getConnection
import java.sql.SQLException
import kotlin.concurrent.Volatile

class HSQLDB4TestDataSource private constructor() : DataSource {
    private val URL = "jdbc:hsqldb:mem:db_test"
    private val USER = "sa"
    private val PASSWORD = ""

    companion object {
        private val LOGGER: Logger = getLogger(HSQLDB4TestDataSource::class.java)

        @Volatile
        private var INSTANCE: HSQLDB4TestDataSource? = null

        fun instance() =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: HSQLDB4TestDataSource().also { INSTANCE = it }
            }
    }

    @Throws(SQLException::class)
    override fun getConnection() = getConnection(URL, USER, PASSWORD)

    init {
        try {
            createTables()
        } catch (e: Exception) {
            LOGGER.error(e.message, e)
            throw RuntimeException(e)
        }
    }

    @Throws(SQLException::class)
    private fun createTables() {
        try {
            getConnection().createStatement().execute("drop table payment")
            getConnection().createStatement().execute("drop table shared_expenses")
            getConnection().createStatement().execute("drop table user")
            getConnection().createStatement().execute("drop table friends_group")
        } catch (ignored: Exception) {
        }

        getConnection().createStatement().execute(
                """create table friends_group(
                          id INT NOT NULL IDENTITY,
                          name VARCHAR(100) NOT NULL,
                    )"""
            )

        getConnection().createStatement().execute(
                """create table user(
                         id INT NOT NULL IDENTITY,
                         name VARCHAR(100) NOT NULL,
                         friends_group_id INT,
                         FOREIGN KEY (friends_group_id) REFERENCES friends_group(id),
                    )"""
            )

        getConnection().createStatement().execute(
                """create table shared_expenses(
                        id INT NOT NULL IDENTITY,
                        friends_group_id INT NOT NULL,
                        owner INT NOT NULL,
                        amount FLOAT NOT NULL,
                        detail VARCHAR(100) NOT NULL,
                        status VARCHAR(10) NOT NULL,
                        date DATETIME,
                        FOREIGN KEY (friends_group_id) REFERENCES friends_group(id),
                        FOREIGN KEY (owner) REFERENCES user(id),
                    )"""
            )

        getConnection().createStatement().execute(
                """create table payment(
                        id INT NOT NULL IDENTITY,
                        payer INT NOT NULL,
                        expent_id INT NOT NULL,
                        amount FLOAT NOT NULL,
                        FOREIGN KEY (payer) REFERENCES user(id),
                        FOREIGN KEY (expent_id) REFERENCES shared_expenses(id),
                    )"""
            )
    }
}
