package ar.edu.unlam.tallerweb1.delivery.util

import ar.edu.unlam.tallerweb1.delivery.Controller
import ar.edu.unlam.tallerweb1.infrastructure.utils.MySqlDataSource
import java.sql.SQLException
import javax.servlet.http.HttpServletRequest

class CleanData : Controller {
    override fun invoke(request: HttpServletRequest): String {
        try {
            val connection = MySqlDataSource.instance().connection
            connection.createStatement().execute("delete from payment")
            connection.createStatement().execute("delete from shared_expenses")
            connection.createStatement().execute("delete from user")
            connection.createStatement().execute("delete from friends_group")
            connection.close()
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
        return "Data deleted!"
    }
}
