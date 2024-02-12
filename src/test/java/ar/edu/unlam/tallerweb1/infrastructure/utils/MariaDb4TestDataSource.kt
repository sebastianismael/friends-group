package ar.edu.unlam.tallerweb1.infrastructure.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.LoggerFactory.getLogger
import java.sql.Connection
import java.sql.DriverManager
import java.sql.DriverManager.getConnection
import java.sql.SQLException

class MariaDb4TestDataSource private constructor() : DataSource {
    private val URL = "jdbc:mysql://localhost"
    private val USER = "root"
    private val PASSWORD = ""

    init {
        try {
            createDB()
            createTables()
        } catch (e: Exception) {
            LOGGER.error(e.message, e)
            throw RuntimeException(e)
        }
    }

    @Throws(SQLException::class)
    private fun createTables() {
        try {
            connection.createStatement().execute("drop table payment")
            connection.createStatement().execute("drop table shared_expenses")
            connection.createStatement().execute("drop table user")
            connection.createStatement().execute("drop table friends_group")
        } catch (ignored: Exception) {
        }

        connection
            .createStatement().execute(
                """create table friends_group(
                          id INT NOT NULL AUTO_INCREMENT,
                          name VARCHAR(100) NOT NULL,
                          PRIMARY KEY ( id )
                    )"""
            )

        connection
            .createStatement().execute(
                """create table user(
                         id INT NOT NULL AUTO_INCREMENT,
                         name VARCHAR(100) NOT NULL,
                         friends_group_id INT,
                         FOREIGN KEY (friends_group_id) REFERENCES friends_group(id),
                         PRIMARY KEY ( id )
                    )"""
            )

        connection
            .createStatement().execute(
                """create table shared_expenses(
                        id INT NOT NULL AUTO_INCREMENT,
                        friends_group_id INT NOT NULL,
                        owner INT NOT NULL,
                        amount FLOAT NOT NULL,
                        detail VARCHAR(100) NOT NULL,
                        status VARCHAR(10) NOT NULL,
                        date DATETIME,
                        FOREIGN KEY (friends_group_id) REFERENCES friends_group(id),
                        FOREIGN KEY (owner) REFERENCES user(id),
                        PRIMARY KEY ( id )
                    )"""
            )

        connection
            .createStatement().execute(
                """create table payment(
                        id INT NOT NULL AUTO_INCREMENT,
                        payer INT NOT NULL,
                        expent_id INT NOT NULL,
                        amount FLOAT NOT NULL,
                        FOREIGN KEY (payer) REFERENCES user(id),
                        FOREIGN KEY (expent_id) REFERENCES shared_expenses(id),
                        PRIMARY KEY ( id )
                    )"""
            )
    }

    private fun createDB() {
        try {
            getConnection(URL, USER, PASSWORD)
                .createStatement()
                .execute("CREATE DATABASE IF NOT EXISTS test")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(SQLException::class)
    override fun getConnection(): Connection = getConnection("$URL/test", USER, PASSWORD)

    companion object {
        private val LOGGER: Logger = getLogger(MariaDb4TestDataSource::class.java)
        private val INSTANCE = MariaDb4TestDataSource()

        fun instance(): MariaDb4TestDataSource {
            return INSTANCE
        }
    }
}
