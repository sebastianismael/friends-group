package ar.edu.unlam.tallerweb1.delivery.util

import ar.edu.unlam.tallerweb1.delivery.Controller
import ar.edu.unlam.tallerweb1.domain.model.ExpentStatus
import ar.edu.unlam.tallerweb1.domain.model.User
import ar.edu.unlam.tallerweb1.infrastructure.utils.MySqlDataSource
import java.lang.System.currentTimeMillis
import java.sql.Connection
import java.sql.Date
import java.sql.SQLException
import javax.servlet.http.HttpServletRequest

class LoadData : Controller {
    private lateinit var connection: Connection
    private val USER_1 = "Ana"
    private val USER_2 = "Pedro"
    private val USER_3 = "Silvia"
    private val USER_4 = "Hector"
    private val GROUP = "group_1"
    override fun invoke(request: HttpServletRequest): String {
        try {
            connection = MySqlDataSource.instance().connection

            val friendsGroupId = createAgroup(GROUP)
            createUser(USER_1, friendsGroupId)
            createUser(USER_2, friendsGroupId)
            val user3Id = createUser(USER_3, friendsGroupId).id
            createUser(USER_4, friendsGroupId)
            val expent1Id = createAnExpent(friendsGroupId, USER_1, 120.0, "Cena")
            val expent2Id = createAnExpent(friendsGroupId, USER_2, 120.0, "Almuerzo")

            createAPayment(user3Id, 30.0, expent1Id)
            createAPayment(user3Id, 30.0, expent2Id)

            connection.close()
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
        return "Data loaded!"
    }

    @Throws(SQLException::class)
    private fun createAPayment(user: Long, amount: Double, expentId: Long) {
        val sql = "INSERT INTO payment (payer, expent_id, amount) VALUES (?,?,?)"
        val psUser = connection.prepareStatement(sql)
        psUser.setLong(1, user)
        psUser.setLong(2, expentId)
        psUser.setDouble(3, amount)
        psUser.execute()
    }

    @Throws(SQLException::class)
    private fun createUser(user: String, friendsGroupId: Long): User {
        val psUser = connection.prepareStatement("INSERT INTO user (name, friends_group_id) VALUES (?,?)")
        psUser.setString(1, user)
        psUser.setLong(2, friendsGroupId)
        psUser.execute()
        return User(getUserId(connection, user), user)
    }

    @Throws(SQLException::class)
    private fun createAnExpent(friendsGroupId: Long, user: String, amount: Double, detail: String): Long {
        val ps = connection.prepareStatement(
            "INSERT INTO shared_expenses " +
                    "(friends_group_id, owner, amount, detail, status, date) VALUES (?,?,?,?,?,?)"
        )
        ps.setLong(1, friendsGroupId!!)
        ps.setLong(2, getUserId(connection, user))
        ps.setDouble(3, amount)
        ps.setString(4, detail)
        ps.setString(5, ExpentStatus.OPEN.name)
        ps.setDate(6, Date(currentTimeMillis()))
        ps.execute()

        val results = connection.createStatement().executeQuery("select id from shared_expenses")
        results.next()
        return results.getLong("id")
    }

    @Throws(SQLException::class)
    private fun createAgroup(groupName: String): Long {
        connection.createStatement().execute("INSERT INTO friends_group (name) VALUES ('$groupName')")
        val results =
            connection.createStatement().executeQuery("select id from friends_group where name = '$groupName'")
        results.next()
        return results.getString("id").toLong()
    }

    companion object {
        @Throws(SQLException::class)
        fun getUserId(connection: Connection, user: String): Long {
            val results = connection.createStatement().executeQuery("select id from user where name = '$user'")
            results.next()
            return results.getString("id").toLong()
        }
    }
}
