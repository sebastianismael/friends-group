package ar.edu.unlam.tallerweb1.infrastructure

import ar.edu.unlam.tallerweb1.domain.SharedExpensesRepository
import ar.edu.unlam.tallerweb1.domain.model.ExpentStatus
import ar.edu.unlam.tallerweb1.domain.model.FriendsGroup
import ar.edu.unlam.tallerweb1.domain.model.SharedExpent
import ar.edu.unlam.tallerweb1.domain.model.User
import ar.edu.unlam.tallerweb1.infrastructure.utils.DataSource
import ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.execute
import ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.executeQuery
import ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.getDate
import ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.getDouble
import ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.getLong
import ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.getString
import ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.next
import ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.prepareStatement
import ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.setDate
import ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.setDouble
import ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.setLong
import ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.setString
import java.sql.Connection
import java.sql.ResultSet

class SharedExpensesJdbcRepository(dataSource: DataSource) : JdbcRepository(dataSource), SharedExpensesRepository {
    override fun findBy(user: String): List<SharedExpent> {
        return searchInTransaction(
            { connection: Connection, _user: String ->
                val found = mutableListOf<SharedExpent>()
                val group = searchGroupOf(connection, _user)
                if (exist(group)) {
                    val friendsGroup = buildFriendsGroup(group)
                    val expenses = searchSharedExpenses(connection, group)
                    while (exist(expenses)) {
                        val owner = searchOwner(connection, getLong("owner", expenses))
                        found.add(buildExpent(expenses, friendsGroup, owner))
                    }
                }
                found
            }, user)
    }

    override fun save(sharedExpent: SharedExpent) {
        executeInTransaction { connection: Connection ->
            val sql = "INSERT INTO shared_expenses (friends_group_id, owner, amount, detail, status, date) VALUES (?,?,?,?,?,?)"
            val ps = prepareStatement(connection, sql)
            setLong(1, sharedExpent.friendsGroup.id, ps)
            setLong(2, sharedExpent.owner.id!!, ps)
            setDouble(3, sharedExpent.amount, ps)
            setString(4, sharedExpent.detail, ps)
            setString(5, ExpentStatus.OPEN.name, ps)
            setDate(6, sharedExpent.date, ps)
            execute(ps)
        }
    }

    private fun buildFriendsGroup(group: ResultSet) = FriendsGroup(getLong("friends_group_id", group), "")
    // TODO obtener el nombre grupo, ver si es necesario

    private fun buildExpent(expenses: ResultSet, friendsGroup: FriendsGroup, owner: User?) =
        SharedExpent(
            getLong("id", expenses),
            owner,
            getDouble("amount", expenses),
            getString("detail", expenses),
            getDate("date", expenses),
            friendsGroup
        )

    private fun exist(resultSet: ResultSet) = next(resultSet)

    private fun searchOwner(connection: Connection, userId: Long): User? {
        val sql = "select * from user where id = ?"
        val expensesStatement = prepareStatement(connection, sql)
        setLong(1, userId, expensesStatement)
        val users = executeQuery(expensesStatement)
        if (next(users))
            return User(getLong("id", users), getString("name", users))
        return null
    }

    private fun searchSharedExpenses(connection: Connection, resultSet: ResultSet): ResultSet {
        val sql = "select * from shared_expenses where friends_group_id = ? and status = 'OPEN' order by date desc"
        val expensesStatement = prepareStatement(connection, sql)
        setLong(1, getLong("friends_group_id", resultSet), expensesStatement)
        return executeQuery(expensesStatement)
    }

    private fun searchGroupOf(connection: Connection, userName: String): ResultSet {
        val sql = "select * from user where name = ?"
        val userStatement = prepareStatement(connection, sql)
        setString(1, userName, userStatement)
        return executeQuery(userStatement)
    }
}
