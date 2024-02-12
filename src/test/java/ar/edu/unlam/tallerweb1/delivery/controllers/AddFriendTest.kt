package ar.edu.unlam.tallerweb1.delivery.controllers

import ar.edu.unlam.tallerweb1.domain.FriendsGroupService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.servlet.http.HttpServletRequest
import org.mockito.kotlin.*

class AddFriendTest {
    private lateinit  var request: HttpServletRequest
    private lateinit var controller: AddFriend
    private lateinit var friendsGroupService: FriendsGroupService

    @BeforeEach
    fun init() {
        request = mock()
        whenever(request.getParameter("user")) doReturn USER
        whenever(request.getParameter("friend")) doReturn FRIEND
        friendsGroupService = mock()
        controller = AddFriend(friendsGroupService)
    }

    @Test
    fun addFriendToUserFriendGroup() {
        whenAddFriendToFriendsGroupOf(USER, FRIEND)
        thenAddFriendToGroup(FRIEND)
    }

    private fun whenAddFriendToFriendsGroupOf(user: String, friend: String) {
        controller(request)
    }

    private fun thenAddFriendToGroup(friend: String) =
        verify(friendsGroupService, times(1)).addFriendsToGroup(USER, friend)

    companion object {
        private const val USER = "user_1"
        private const val FRIEND = "friend"
    }
}
