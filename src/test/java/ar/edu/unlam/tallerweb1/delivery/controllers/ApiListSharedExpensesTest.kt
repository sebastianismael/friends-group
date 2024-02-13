package ar.edu.unlam.tallerweb1.delivery.controllers

import ar.edu.unlam.tallerweb1.delivery.api.ApiListSharedExpenses
import ar.edu.unlam.tallerweb1.domain.SharedExpensesRepository
import ar.edu.unlam.tallerweb1.domain.model.FriendsGroup
import ar.edu.unlam.tallerweb1.domain.model.SharedExpent
import ar.edu.unlam.tallerweb1.domain.model.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDateTime.now
import java.time.temporal.ChronoUnit.DAYS
import javax.servlet.http.HttpServletRequest


class ApiListSharedExpensesTest {
    private lateinit var controller: ApiListSharedExpenses
    private lateinit var repository: SharedExpensesRepository
    private lateinit var request: HttpServletRequest
    private val group = FriendsGroup(1L, "a")
    private val USER = "user_1"

    @BeforeEach
    fun init() {
        request = mock()
        whenever(request.getParameter("user")) doReturn USER
        repository = mock()
        controller = ApiListSharedExpenses(repository)
    }

    @Test
    fun returnAnEmtyListWhenThereIsNotExpenses() {
        givenThereIsNotExpensesForGroupOf(USER)
        val expenses = whenGetSharedExpensesOfGroupWithUser(USER)
        thenThereIsNotExpenses(expenses)
    }

    @Test
    fun returnAnExpensesListWhenUserGroupHasSeveral() {
        givenExpensesForGroupOf(USER)
        val expenses = whenGetSharedExpensesOfGroupWithUser(USER)
        thenThereAreExpenses(expenses)
    }

    private fun givenExpensesForGroupOf(user: String) {
        val list = mutableListOf(
            SharedExpent(
                1L,
                User(1L, "Francisco Buyo"),
                100.0,
                "Cena",
                now().plus(-5, DAYS),
                group
            ),
            SharedExpent(
                2L,
                User(2L, "Alfonso Perez"),
                53.40,
                "Taxi",
                now().plus(-12, DAYS),
                group
            )
        )

        whenever(repository.findBy(user)) doReturn list
    }

    private fun givenThereIsNotExpensesForGroupOf(user: String) {
        whenever(repository.findBy(user)) doReturn listOf()
    }

    private fun whenGetSharedExpensesOfGroupWithUser(user: String) = controller.invoke(request) as List<SharedExpent>

    private fun thenThereAreExpenses(expenses: List<SharedExpent>) {
        assertThat(expenses).hasSize(2)
    }

    private fun thenThereIsNotExpenses(expenses: List<SharedExpent>) {
        assertThat(expenses).isEmpty()
    }
}
