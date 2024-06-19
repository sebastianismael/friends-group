package ar.edu.unlam.tallerweb1.domain.usecases

import ar.edu.unlam.tallerweb1.domain.FriendsGroupRepository
import ar.edu.unlam.tallerweb1.domain.UserRepository
import ar.edu.unlam.tallerweb1.domain.exceptions.UserNotExists
import ar.edu.unlam.tallerweb1.domain.exceptions.UserWithoutFriendsGroup
import ar.edu.unlam.tallerweb1.domain.model.FriendsGroup
import ar.edu.unlam.tallerweb1.domain.model.User
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class AddFriendsToGroupTest {
    private val USER = "user_18989"
    private val FRIEND = "friend"
    private lateinit var userRepository: UserRepository
    private lateinit var friendsGroupRepository: FriendsGroupRepository
    private lateinit var addFriendsToGroup: AddFriendsToGroup

    @BeforeEach
    fun init() {
        userRepository = mock()
        friendsGroupRepository = mock()
        addFriendsToGroup = AddFriendsToGroup(userRepository)
    }

    @Test
    fun addFriendShouldThrowAnErrorIfUserNotExists() {
        assertThrows(UserNotExists::class.java) {
            givenNotExistsUser(USER)
            whenAddFriend(USER, FRIEND)
        }
    }

    @Test
    fun addFriendShouldThrowAnErrorIfUserDoesHaveAFriendGroup() {
        assertThrows(UserWithoutFriendsGroup::class.java) {
            givenUserWithoutFriensGroup(USER)
            whenAddFriend(USER, FRIEND)
        }
    }

    @Test
    fun canAddFriendToMyGroup() {
        givenUserWithFriensGroup(USER)
        whenAddFriend(USER, FRIEND)
        thenFriendIsAdded()
    }

    private fun whenAddFriend(user: String, friend: String) = addFriendsToGroup(user, friend)
    private fun thenFriendIsAdded() = verify(userRepository, times(1)).saveWithFriendGroup(any())
    private fun givenUserWithFriensGroup(user: String) {
        val friend = User(1L, "bad guy")
        friend.friendsGroup = FriendsGroup(4L, "")
        whenever(userRepository.findByName(user)) doReturn friend
    }

    private fun givenUserWithoutFriensGroup(user: String) =
        whenever(userRepository.findByName(user)) doReturn User(1L, "bad guy")
    private fun givenNotExistsUser(user: String) = whenever(userRepository.findByName(user)) doReturn null


}