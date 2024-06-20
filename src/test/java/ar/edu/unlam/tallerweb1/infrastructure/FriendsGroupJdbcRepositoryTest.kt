package ar.edu.unlam.tallerweb1.infrastructure

import ar.edu.unlam.tallerweb1.domain.FriendsGroupRepository
import ar.edu.unlam.tallerweb1.domain.model.FriendsGroup
import ar.edu.unlam.tallerweb1.domain.model.User
import ar.edu.unlam.tallerweb1.infrastructure.utils.TestsPersistenceHelper.createGroup
import ar.edu.unlam.tallerweb1.infrastructure.utils.TestsPersistenceHelper.createUser
import ar.edu.unlam.tallerweb1.infrastructure.utils.TestsPersistenceHelper.getFriendsGroupId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.sql.SQLException

class FriendsGroupJdbcRepositoryTest : JdbcRepositoryTest() {
    private val USER_1 = "user_1"
    private val USER_2 = "user_2"
    private val USER_3 = "user_3"
    private val GROUP_A = "group_A"
    private val GROUP_B = "group_B"
    private val OTHER = "other"

    private val friendsGroupRepository: FriendsGroupRepository = FriendsGroupJdbcRepository(dataSource)
    @Test
    @Throws(SQLException::class)
    fun shouldReturnMemebersWhenGroupHasFriends() {
        val groupId = givenUserHasAGroupWithTwoFriends(USER_1, GROUP_A)
        givenUserHasAGroupWithTwoFriends(OTHER, GROUP_B)
        val members = whenGetGroupMembers(groupId)
        thenGroupHasThreeMemebers(members)
    }

    @Test
    @Throws(SQLException::class)
    fun shouldReturnNoMemebersWhenGroupIsEmpty() {
        createGroup(connection, GROUP_A)
        val groupId = getFriendsGroupId(connection, GROUP_A)
        val members = whenGetGroupMembers(groupId)
        thenGroupHasNotMemebers(members)
    }

    @Test
    @Throws(SQLException::class)
    fun getTheGroup(){
        givenUserHasAGroup(USER_1, GROUP_A)
        val group = whenSearchGroupOf(USER_1)
        thenGetGroup(GROUP_A, group)
    }

    @Throws(SQLException::class)
    private fun givenUserHasAGroup(user: String, group: String) {
        createGroup(connection, group)
        val friendsGroupId = getFriendsGroupId(connection, group)
        createUser(connection, user, friendsGroupId)
    }

    @Throws(SQLException::class)
    private fun givenUserHasAGroupWithTwoFriends(user: String, group: String): Long {
        createGroup(connection, group)
        val friendsGroupId = getFriendsGroupId(connection, group)
        createUser(connection, user, friendsGroupId)
        createUser(connection, USER_2, friendsGroupId)
        createUser(connection, USER_3, friendsGroupId)
        return friendsGroupId
    }

    private fun whenGetGroupMembers(groupId: Long) = friendsGroupRepository.getMembers(FriendsGroup(groupId, ""))

    private fun whenSearchGroupOf(user: String) = friendsGroupRepository.getGroupOf(User(user))!!

    private fun thenGroupHasNotMemebers(members: List<User>) = assertThat(members).isEmpty()

    private fun thenGetGroup(expected: String, group: FriendsGroup) = assertThat(group.name).isEqualTo(expected)

    private fun thenGroupHasThreeMemebers(members: List<User>) = assertThat(members).hasSize(3)
}
