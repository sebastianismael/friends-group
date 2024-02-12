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
    private val FRIEND = "friend"
    private lateinit var userRepository: UserRepository
    private lateinit var friendsGroupRepository: FriendsGroupRepository
    private lateinit var sharedExpensesRepository: SharedExpensesRepository
    private lateinit var paymentRepository: PaymentRepository
    private val DETAIL = "Una cena"
    private val AMOUNT = 1050.0
    private val FRIEND_1: String = "friend_1"
    private val FRIEND_2: String = "friend_2"
    private val FRIEND_3: String = "friend_3"

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

    @Test
    fun balanceShouldBeEmptyIfThereIsNotFriendsInGroup() {
        givenAnEmptyFriendsGroup(USER)
        val balance = whenGetBalance(USER)
        thenTheBalanceIsEmpty(balance)
    }

    @Test
    fun balanceShouldBeEmptyIfUserHasNotAFriendsGroup() {
        givenUserWithoutFriensGroup(USER)
        val balance = whenGetBalance(USER)
        thenTheBalanceIsEmpty(balance)
    }

    @Test
    fun balanceShouldThrowAnErrorIfUserNotExists() {
        assertThrows(UserNotExists::class.java) {
            givenNotExistsUser(USER)
            whenGetBalance(USER)
        }
    }

    @Test
    fun balanceShouldBeZeroForEverybodyIfGroupHasNotExpenses() {
        givenUserWithFriensGroup(USER)
        givenAGroupWith(4)
        givenGroupHasNotExpenses()
        val balance = whenGetBalance(USER)
        thenBalanceShouldHasALineFor(USER, 0.0, balance)
        thenBalanceShouldHasALineFor(FRIEND_1, 0.0, balance)
        thenBalanceShouldHasALineFor(FRIEND_2, 0.0, balance)
        thenBalanceShouldHasALineFor(FRIEND_3, 0.0, balance)
    }

    @Test
    fun balanceWithOneExpentAndWithoutPayments() {
        givenUserWithFriensGroup(USER)
        givenAGroupWith(4)
        givenGroupHasAnExpents(1L, USER, 120.0)

        val balance = whenGetBalance(USER)

        thenBalanceShouldHasALineFor(USER, 90.0, balance)
        thenBalanceShouldHasALineFor(FRIEND_1, -30.0, balance)
        thenBalanceShouldHasALineFor(FRIEND_2, -30.0, balance)
        thenBalanceShouldHasALineFor(FRIEND_3, -30.0, balance)
    }

    @Test
    fun balanceWithMoreThanOneExpentAndWithoutPayments() {
        givenUserWithFriensGroup(USER)
        givenAGroupWith(4)

        givenGroupHasAnExpents( mutableListOf(
            SharedExpent(1L, User(USER), 120.0, "cena", now(), null),
            SharedExpent(12L, User(FRIEND_1), 120.0, "almuerzo", now(), null)
        ))

        val balance = whenGetBalance(USER)

        thenBalanceShouldHasALineFor(USER, 60.0, balance)
        thenBalanceShouldHasALineFor(FRIEND_1, 60.0, balance)
        thenBalanceShouldHasALineFor(FRIEND_2, -60.0, balance)
        thenBalanceShouldHasALineFor(FRIEND_3, -60.0, balance)
    }

    @Test
    fun balanceWithMoreThanOneExpentAndWithPayments() {
        givenUserWithFriensGroup(USER)
        givenAGroupWith(4)

        val e1 = SharedExpent(1L, User(USER), 120.0, "cena", now(), null)
        val e2 = SharedExpent(12L, User(FRIEND_1), 120.0, "almuerzo", now(), null)
        givenGroupHasAnExpents( mutableListOf(e1,e2))

        givenAPayment(FRIEND_3, 30.0, e1)
        givenAPayment(FRIEND_3, 30.0, e2)

        val balance = whenGetBalance(USER)

        thenBalanceShouldHasALineFor(USER, 30.0, balance)
        thenBalanceShouldHasALineFor(FRIEND_1, 30.0, balance)
        thenBalanceShouldHasALineFor(FRIEND_2, -60.0, balance)
        thenBalanceShouldHasALineFor(FRIEND_3, 0.0, balance)
    }

    @Test
    fun addExpentShouldPersistANewOne() {
        givenUserWithFriensGroup(USER)
        whenAddExpentToAGroup(USER, DETAIL, AMOUNT)
        thenExpentIsSaved()
    }

    private fun thenExpentIsSaved() {
        verify(sharedExpensesRepository, times(1)).save(any())
    }

    private fun whenAddExpentToAGroup(user: String, detail: String, amount: Double) {
        friendsGroupService.addExpentToGroup(user, detail, amount)
    }

    private fun givenAPayment(user: String, amount: Double, sharedExpent: SharedExpent) {
        whenever(paymentRepository.findPaymentsOf(sharedExpent.id)) doReturn listOf(aPaymentWith(user, amount, sharedExpent))
    }

    private fun aPaymentWith(user: String, amount: Double, sharedExpent: SharedExpent) =
        Payment(1L, User(user), amount, sharedExpent)

    private fun givenGroupHasNotExpenses() {
        whenever(sharedExpensesRepository.findBy(any())) doReturn listOf()
    }

    private fun givenGroupHasAnExpents(id: Long, payer: String, amount: Double) {
        whenever(sharedExpensesRepository.findBy(payer)) doReturn listOf(aSharedExpentWith(id, payer, amount))
    }

    private fun givenGroupHasAnExpents(expenses: List<SharedExpent>) {
        whenever(sharedExpensesRepository.findBy(any<String>())) doReturn expenses
    }

    private fun givenAGroupWith(friendsNumber: Int) {
        val friends = mutableListOf<User>()
        for (i in 1 until friendsNumber)
            friends.add(User("friend_$i"))

        friends.add(User(USER))
        whenever(friendsGroupRepository.getMembers(any<FriendsGroup>())) doReturn friends
    }

    private fun givenAnEmptyFriendsGroup(user: String) {
        val mockUser: User = mock()
        whenever(mockUser.friendsGroup) doReturn mock()
        whenever(userRepository.findByName(user)) doReturn mockUser
        whenever(friendsGroupRepository.getMembers(any())) doReturn listOf()
    }

    private fun givenUserWithFriensGroup(user: String) {
        val friend = User(1L, "bad guy")
        friend.friendsGroup = FriendsGroup(4L, "")
        whenever(userRepository.findByName(user)) doReturn friend
    }

    private fun givenUserWithoutFriensGroup(user: String) {
        whenever(userRepository.findByName(user)) doReturn User(1L, "bad guy")
    }

    private fun givenNotExistsUser(user: String) {
        whenever(userRepository.findByName(user)) doReturn null
    }

    private fun whenAddFriend(user: String, friend: String) {
        friendsGroupService.addFriendsToGroup(user, friend)
    }

    private fun whenGetBalance(user: String): Map<String, Double> = friendsGroupService.getBalance(user)

    private fun thenFriendIsAdded() {
        verify(userRepository, times(1)).saveWithFriendGroup(any())
    }

    private fun thenTheBalanceIsEmpty(balance: Map<String, Double>) {
        assertThat(balance).isEmpty()
    }

    private fun thenBalanceShouldHasALineFor(user: String, amount: Double, balance: Map<String, Double>) {
        assertThat(balance[user]).isEqualTo(amount)
    }

    private fun aSharedExpentWith(id: Long, payer: String, amount: Double): SharedExpent =
        SharedExpent(id, User(payer), amount, "detail", now(), FriendsGroup(1L, ""))


}
