package ar.edu.unlam.tallerweb1.domain

import ar.edu.unlam.tallerweb1.domain.exceptions.UserNotExists
import ar.edu.unlam.tallerweb1.domain.exceptions.UserWithoutFriendsGroup
import ar.edu.unlam.tallerweb1.domain.model.FriendsGroup
import ar.edu.unlam.tallerweb1.domain.model.Payment
import ar.edu.unlam.tallerweb1.domain.model.SharedExpent
import ar.edu.unlam.tallerweb1.domain.model.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.time.LocalDateTime.now

class FriendsGroupServiceTest {
    private lateinit var friendsGroupService: FriendsGroupService
    private val USER = "user_18989"
    private lateinit var userRepository: UserRepository
    private lateinit var friendsGroupRepository: FriendsGroupRepository
    private lateinit var sharedExpensesRepository: SharedExpensesRepository
    private lateinit var paymentRepository: PaymentRepository
    private val DETAIL = "Una cena"
    private val AMOUNT = 1050.0

    @BeforeEach
    fun init() {
        userRepository = mock()
        friendsGroupRepository = mock()
        sharedExpensesRepository = mock()
        paymentRepository = mock()
        friendsGroupService = FriendsGroupServiceImpl(
            userRepository,
            friendsGroupRepository,
            sharedExpensesRepository,
            paymentRepository
        )
    }

    @Test
    fun addExpentShouldPersistANewOne() {
        givenUserWithFriensGroup(USER)
        whenAddExpentToAGroup(USER, DETAIL, AMOUNT)
        thenExpentIsSaved()
    }

    private fun thenExpentIsSaved() = verify(sharedExpensesRepository, times(1)).save(any())

    private fun whenAddExpentToAGroup(user: String, detail: String, amount: Double) =
        friendsGroupService.addExpentToGroup(user, detail, amount)

    private fun givenAPayment(user: String, amount: Double, sharedExpent: SharedExpent) =
        whenever(paymentRepository.findPaymentsOf(sharedExpent.id!!)) doReturn listOf(aPaymentWith(user, amount, sharedExpent))

    private fun aPaymentWith(user: String, amount: Double, sharedExpent: SharedExpent) =
        Payment(1L, User(user), amount, sharedExpent)

    private fun givenUserWithFriensGroup(user: String) {
        val friend = User(1L, "bad guy")
        friend.friendsGroup = FriendsGroup(4L, "")
        whenever(userRepository.findByName(user)) doReturn friend
    }

}
