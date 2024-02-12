package ar.edu.unlam.tallerweb1.delivery.controllers

import ar.edu.unlam.tallerweb1.delivery.HtmlStrings
import ar.edu.unlam.tallerweb1.domain.FriendsGroupService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import javax.servlet.http.HttpServletRequest

class AddExpentTest {
    private lateinit var request: HttpServletRequest
    private lateinit var controller: AddExpent
    private lateinit var friendsGroupService: FriendsGroupService

    @BeforeEach
    fun init() {
        request = mock()
        whenever(request.getParameter("user")) doReturn  USER
        whenever(request.getParameter("amount")) doReturn  AMOUNT
        whenever(request.getParameter("detail")) doReturn  DETAIL
        friendsGroupService = mock()

        controller = AddExpent(friendsGroupService)
    }

    @Test
    fun addExpentToUserFriendGroup() {
        whenAddAnExpentToFriendsGroupOf(USER)
        thenAddExpentToGroup(DETAIL, AMOUNT)
    }

    @Test
    fun shouldReturnErrorIfAmountIsNotPresent() {
        givenAmountIsNotPresent()
        val response = whenAddAnExpentToFriendsGroupOf(USER)
        thenGetErrorMessage(response, HtmlStrings.ADD_EXPENT_NO_AMOUNT)
    }

    @Test
    fun shouldReturnErrorIfDetailIsNotPresent() {
        givenDetailIsNotPresent()
        val response = whenAddAnExpentToFriendsGroupOf(USER)
        thenGetErrorMessage(response, HtmlStrings.ADD_EXPENT_NO_DETAIL)
    }

    private fun givenDetailIsNotPresent() {
        whenever(request.getParameter("detail")) doReturn ""
    }

    private fun givenAmountIsNotPresent() {
        whenever(request.getParameter("amount")) doReturn ""
    }

    private fun whenAddAnExpentToFriendsGroupOf(user: String) = controller(request)

    private fun thenAddExpentToGroup(detail: String, amount: String) {
        verify(friendsGroupService, times(1)).addExpentToGroup(USER, detail, amount.toDouble())
    }

    private fun thenGetErrorMessage(actual: String, expected: String) {
        assertThat(actual).isEqualTo(expected)
    }

    companion object {
        private const val USER = "user_1"
        private const val DETAIL = "Cena"

        private const val AMOUNT = "100.50"
    }
}