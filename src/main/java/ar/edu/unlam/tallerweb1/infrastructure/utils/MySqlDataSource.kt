package ar.edu.unlam.tallerweb1.infrastructure.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import java.io.IOException
import java.sql.Connection
import java.sql.DriverManager.getConnection
import java.sql.SQLException
import java.util.*

class MySqlDataSource private constructor() : DataSource {
    @Throws(SQLException::class)
    override fun getConnection(): Connection {
        val prop = Properties()
        try {
            MySqlDataSource::class.java.classLoader.getResourceAsStream("db.properties").use { resourceAsStream ->
                prop.load(resourceAsStream)
            }
        } catch (e: IOException) {
            logger.error("Unable to load properties file db.properties")
        } // TODO mover a una clase que se ocupe de esto


        val url = prop.getProperty("db.url")
        val user = prop.getProperty("db.username")
        val password = prop.getProperty("db.password")
        try {
            Class.forName("com.mysql.jdbc.Driver")
        } catch (e: ClassNotFoundException) {
            logger.error(e.message, e)
            throw RuntimeException(e)
        }
        return getConnection(url, user, password)
    }

    companion object {
        private val logger: Logger = getLogger(MySqlDataSource::class.java)
        private val instance = MySqlDataSource()
        fun instance(): MySqlDataSource {
            return instance
        }
    }
}
