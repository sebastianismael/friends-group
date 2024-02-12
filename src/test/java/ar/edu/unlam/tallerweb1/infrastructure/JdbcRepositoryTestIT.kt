package ar.edu.unlam.tallerweb1.infrastructure

import ar.edu.unlam.tallerweb1.domain.model.User
import ar.edu.unlam.tallerweb1.infrastructure.utils.DataSource
import ar.edu.unlam.tallerweb1.infrastructure.utils.HSQLDB4TestDataSource.Companion.instance
import ar.edu.unlam.tallerweb1.infrastructure.utils.TestsPersistenceHelper.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.sql.Connection
import java.sql.SQLException

abstract class JdbcRepositoryTestIT {
    protected lateinit var connection: Connection
    var dataSource: DataSource = instance()

    @BeforeEach
    @Throws(SQLException::class)
    fun init() {
        connection = dataSource.connection
    }

    @AfterEach
    @Throws(SQLException::class)
    fun tearDown() {
        connection.createStatement().execute("delete from payment")
        connection.createStatement().execute("delete from shared_expenses")
        connection.createStatement().execute("delete from user")
        connection.createStatement().execute("delete from friends_group")
        connection.close()
    }

    @Throws(SQLException::class)
    protected fun givenAGroupUser(user: String, friendsGroupId: Long): User {
        createUser(connection, user, friendsGroupId)
        return User(getUserId(connection, user), user)
    }

    @Throws(SQLException::class)
    protected fun givenAgroup(groupName: String): Long {
        createGroup(connection, groupName)
        return getFriendsGroupId(connection, groupName)
    }

    @Throws(SQLException::class)
    protected fun givenAnExpent(friendsGroupId: Long, user: String, amount: Double, detail: String): Long {
        createExpent(
            connection,
            getUserId(connection, user),
            friendsGroupId,
            120.0,
            "Cena"
        )
        return expentId
    }

    @get:Throws(SQLException::class)
    private val expentId: Long
        get() {
            val results = connection.createStatement().executeQuery("select id from shared_expenses")
            results.next()
            return results.getLong("id")
        }

    @Throws(SQLException::class)
    protected fun givenAPayment(user: Long, amount: Double, expentId: Long) {
        val sql = "INSERT INTO payment (payer, expent_id, amount) VALUES (?,?,?)"
        val psUser = connection.prepareStatement(sql)
        psUser.setLong(1, user)
        psUser.setLong(2, expentId)
        psUser.setDouble(3, amount)
        psUser.execute()
    }
}
