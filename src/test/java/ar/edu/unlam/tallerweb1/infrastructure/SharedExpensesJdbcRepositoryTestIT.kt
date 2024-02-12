package ar.edu.unlam.tallerweb1.infrastructure

import ar.edu.unlam.tallerweb1.domain.model.FriendsGroup
import ar.edu.unlam.tallerweb1.domain.model.SharedExpent
import ar.edu.unlam.tallerweb1.domain.model.User
import ar.edu.unlam.tallerweb1.infrastructure.utils.TestsPersistenceHelper.*
import ar.edu.unlam.tallerweb1.infrastructure.utils.TestsPersistenceHelper.Companion.createExpent
import ar.edu.unlam.tallerweb1.infrastructure.utils.TestsPersistenceHelper.Companion.createGroup
import ar.edu.unlam.tallerweb1.infrastructure.utils.TestsPersistenceHelper.Companion.createUser
import ar.edu.unlam.tallerweb1.infrastructure.utils.TestsPersistenceHelper.Companion.getFriendsGroupId
import ar.edu.unlam.tallerweb1.infrastructure.utils.TestsPersistenceHelper.Companion.getUserId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.sql.SQLException
import java.time.LocalDateTime

class SharedExpensesJdbcRepositoryTestIT : JdbcRepositoryTestIT() {
    private val USER_1 = "user_1"
    private val USER_2 = "user_2"
    private val USER_3 = "user_3"
    private val GROUP = "group_1"
    private val repository = SharedExpensesJdbcRepository(dataSource)

    @Throws(SQLException::class)
    @Test
    fun getSharedExpensesIfExist() {
        givenUserHasAGroupWithThreeExpenses(USER_1)
        val sharedExpenses = whenSearchExpensesOfUserGroup(USER_1)
        thenGetThreeExpenses(sharedExpenses)
    }

    @Throws(SQLException::class)
    @Test
    fun getEmptySharedExpensesIfNotExist() {
        givenUserHasAGroupWithOutExpenses(USER_1)
        val sharedExpenses = whenSearchExpensesOfUserGroup(USER_1)
        thenNotGetExpenses(sharedExpenses)
    }

    @Test
    @Throws(SQLException::class)
    fun shouldAddAnExpent() {
        val friendsGroupId = givenAgroup(GROUP)
        val owner = givenAGroupUser(USER_1, friendsGroupId)
        whenAddAnExpent(owner, friendsGroupId)
        thenThereIsOneExpentInGroupOf(USER_1)
    }

    @Throws(SQLException::class)
    private fun givenUserHasAGroupWithOutExpenses(user: String): Long {
        createGroup(connection, GROUP)
        val friendsGroupId = getFriendsGroupId(connection, GROUP)
        createUser(connection, user, friendsGroupId)
        return friendsGroupId
    }

    @Throws(SQLException::class)
    private fun givenUserHasAGroupWithThreeExpenses(user: String) {
        createGroup(connection, GROUP)
        val friendsGroupId = getFriendsGroupId(connection, GROUP)
        createUser(connection, user, friendsGroupId)
        createUser(connection, USER_2, friendsGroupId)
        createUser(connection, USER_3, friendsGroupId)
        createExpent(
            connection,
            getUserId(connection, USER_2),
            friendsGroupId,
            123.0,
            "Cena"
        )
        createExpent(
            connection,
            getUserId(connection, USER_2),
            friendsGroupId,
            30.0,
            "Desayuno"
        )
        createExpent(
            connection,
            getUserId(connection, USER_3),
            friendsGroupId,
            100.5,
            "Almuerzo"
        )
    }

    private fun whenAddAnExpent(owner: User, groupId: Long) {
        val expent = SharedExpent(owner, 333.3, "detail", LocalDateTime.now(), FriendsGroup(groupId, ""))
        repository.save(expent)
    }

    private fun whenSearchExpensesOfUserGroup(user: String) = getExpentBy(user)

    private fun getExpentBy(user: String) = repository.findBy(user)

    private fun thenGetThreeExpenses(sharedExpenses: List<SharedExpent>) {
        assertThat(sharedExpenses).hasSize(3)
    }

    private fun thenNotGetExpenses(sharedExpenses: List<SharedExpent>) {
        assertThat(sharedExpenses).isEmpty()
    }

    private fun thenThereIsOneExpentInGroupOf(user: String) {
        assertThat(getExpentBy(user)).hasSize(1)
    }
}
