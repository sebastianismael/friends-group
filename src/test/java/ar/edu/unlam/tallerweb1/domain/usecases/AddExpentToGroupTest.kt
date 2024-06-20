package ar.edu.unlam.tallerweb1.domain.usecases

import ar.edu.unlam.tallerweb1.domain.*
import ar.edu.unlam.tallerweb1.domain.model.FriendsGroup
import ar.edu.unlam.tallerweb1.domain.model.User
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class AddExpentToGroupTest {
    private lateinit var addExpentToGroup: AddExpentToGroup
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
        addExpentToGroup = AddExpentToGroup(
            userRepository,
            friendsGroupRepository,
            sharedExpensesRepository
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
        addExpentToGroup(user, detail, amount)

    private fun givenUserWithFriensGroup(user: String) {
        val friend = User(1L, "bad guy")
        friend.friendsGroup = FriendsGroup(4L, "")
        whenever(userRepository.findByName(user)) doReturn friend
    }

}
