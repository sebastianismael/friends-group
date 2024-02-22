package ar.edu.unlam.tallerweb1.delivery.util

import ar.edu.unlam.tallerweb1.delivery.Controller
import ar.edu.unlam.tallerweb1.infrastructure.utils.MySqlDataSource
import java.sql.Connection
import java.sql.SQLException
import javax.servlet.http.HttpServletRequest

class CreateSchema : Controller {
    private lateinit var connection: Connection
    override fun invoke(request: HttpServletRequest): String {
        try {
            connection = MySqlDataSource.instance().getConnection()
            createTables()
            connection.close()
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
        return "Schema created!"
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

        connection.createStatement().execute(
            """create table friends_group(
                          id INT NOT NULL AUTO_INCREMENT,
                          name VARCHAR(100) NOT NULL,
                    PRIMARY KEY (id)
                    )"""
        )

        connection.createStatement().execute(
            """create table user(
                         id INT NOT NULL AUTO_INCREMENT,
                         name VARCHAR(100) NOT NULL,
                         friends_group_id INT,
                        PRIMARY KEY (id),
                         FOREIGN KEY (friends_group_id) REFERENCES friends_group(id)
                    )"""
        )

        connection.createStatement().execute(
            """create table shared_expenses(
                        id INT NOT NULL AUTO_INCREMENT,
                        friends_group_id INT NOT NULL,
                        owner INT NOT NULL,
                        amount FLOAT NOT NULL,
                        detail VARCHAR(100) NOT NULL,
                        status VARCHAR(10) NOT NULL,
                        date DATETIME,
                        PRIMARY KEY (id),
                        FOREIGN KEY (friends_group_id) REFERENCES friends_group(id),
                        FOREIGN KEY (owner) REFERENCES user(id)
                    )"""
        )

        connection.createStatement().execute(
            """create table payment(
                        id INT NOT NULL AUTO_INCREMENT,
                        payer INT NOT NULL,
                        expent_id INT NOT NULL,
                        amount FLOAT NOT NULL,
                        PRIMARY KEY (id),
                        FOREIGN KEY (payer) REFERENCES user(id),
                        FOREIGN KEY (expent_id) REFERENCES shared_expenses(id)
                    )"""
        )
    }
}
