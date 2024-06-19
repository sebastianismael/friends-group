package ar.edu.unlam.tallerweb1.delivery.controllers

import ar.edu.unlam.tallerweb1.domain.usecases.AddFriendsToGroup
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import javax.servlet.http.HttpServletRequest

class AddFriendTest {
    private val USER = "user_1"
    private val FRIEND = "friend"
    private lateinit var controller: AddFriend
    private lateinit  var request: HttpServletRequest
    private lateinit var addFriendsToGroup: AddFriendsToGroup

    @BeforeEach
    fun init() {
        request = mock()
        whenever(request.getParameter("user"))   doReturn USER
        whenever(request.getParameter("friend")) doReturn FRIEND
        addFriendsToGroup = mock()
        controller = AddFriend(addFriendsToGroup)
    }

    @Test
    fun addFriendToUserFriendGroup() {
        whenAddFriendToFriendsGroupOf(USER, FRIEND)
        thenAddFriendToGroup(FRIEND)
    }

    private fun whenAddFriendToFriendsGroupOf(user: String, friend: String) = controller(request)

    private fun thenAddFriendToGroup(friend: String) =
        verify(addFriendsToGroup, times(1)).invoke(USER, friend)
}
