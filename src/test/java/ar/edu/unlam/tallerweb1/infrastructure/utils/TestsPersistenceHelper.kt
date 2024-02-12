package ar.edu.unlam.tallerweb1.infrastructure.utils

import ar.edu.unlam.tallerweb1.domain.model.ExpentStatus.OPEN
import java.lang.System.currentTimeMillis
import java.sql.Connection
import java.sql.Date
import java.sql.SQLException

class TestsPersistenceHelper {
    companion object {

        @Throws(SQLException::class)
        fun createUser(connection: Connection, user: String, friendsGroupId: Long) {
            val psUser = connection.prepareStatement("INSERT INTO user (name, friends_group_id) VALUES (?,?)")
            psUser.setString(1, user)
            psUser.setLong(2, friendsGroupId)
            psUser.execute()
        }

        @Throws(SQLException::class)
        fun createGroup(connection: Connection, group: String) {
            connection.createStatement().execute("INSERT INTO friends_group (name) VALUES ('$group')")
        }

        @Throws(SQLException::class)
        fun createExpent(
            connection: Connection,
            owner: Long,
            friendsGroupId: Long,
            amount: Double,
            detail: String
        ) {
            val ps = connection.prepareStatement(
                "INSERT INTO shared_expenses " +
                        "(friends_group_id, owner, amount, detail, status, date) VALUES (?,?,?,?,?,?)"
            )
            ps.setLong(1, friendsGroupId)
            ps.setLong(2, owner)
            ps.setDouble(3, amount)
            ps.setString(4, detail)
            ps.setString(5, OPEN.name)
            ps.setDate(6, Date(currentTimeMillis()))
            ps.execute()
        }

        @Throws(SQLException::class)
        fun getFriendsGroupId(connection: Connection, group: String): Long {
            val results = connection.createStatement().executeQuery("select id from friends_group where name = '$group'")
            results.next()
            return results.getString("id").toLong()
        }

        @Throws(SQLException::class)
        fun getUserId(connection: Connection, user: String): Long {
            val results = connection.createStatement().executeQuery("select id from user where name = '$user'")
            results.next()
            return results.getString("id").toLong()
        }

        @Throws(SQLException::class)
        fun cleanExpenses(connection: Connection) {
            connection.createStatement().execute("delete from shared_expenses")
        }
    }
}
