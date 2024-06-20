package ar.edu.unlam.tallerweb1.infrastructure

import ar.edu.unlam.tallerweb1.domain.UserRepository
import ar.edu.unlam.tallerweb1.domain.model.FriendsGroup
import ar.edu.unlam.tallerweb1.domain.model.User
import ar.edu.unlam.tallerweb1.infrastructure.utils.TestsPersistenceHelper.createGroup
import ar.edu.unlam.tallerweb1.infrastructure.utils.TestsPersistenceHelper.getFriendsGroupId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.sql.SQLException

class UserJdbcRepositoryTest : JdbcRepositoryTest() {
    private val userRepository: UserRepository = UserJdbcRepostory(dataSource)
    private val USER = "user_18989"
    private val GROUP = "group_sfaf1"
    @Test
    fun createANewUser() {
        whenCreateAnUser(USER)
        thenCanFindIt(USER)
    }

    @Test
    @Throws(SQLException::class)
    fun createANewUserWithFriendGroup() {
        createGroup(connection, GROUP)
        whenCreateAnUser(USER, GROUP)
        thenCanFindItAndHasAFriendsGroup(USER)
    }

    @Throws(SQLException::class)
    private fun whenCreateAnUser(user: String, group: String) {
        val friendsGroupId = getFriendsGroupId(connection, group)
        val userToBeCreated = User(user)
        userToBeCreated.friendsGroup = FriendsGroup(friendsGroupId, group)
        userRepository.saveWithFriendGroup(userToBeCreated)
    }

    @Test
    fun searchAnUnexistingUser() {
        whenNotExistsTheUser(USER)
        thenCantFindIt(USER)
    }

    private fun whenNotExistsTheUser(user: String) {}

    private fun whenCreateAnUser(user: String) = userRepository.save(User(user))

    private fun thenCantFindIt(user: String) = assertThat(userRepository.findByName(user)).isNull()

    private fun thenCanFindItAndHasAFriendsGroup(user: String) {
        val found = userRepository.findByName(user)
        assertThat(found).isNotNull()
        assertThat(found!!.friendsGroup).isNotNull()
    }

    private fun thenCanFindIt(user: String) = assertThat(userRepository.findByName(user)).isNotNull()
}
