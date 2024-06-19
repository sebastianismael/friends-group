package ar.edu.unlam.tallerweb1.domain

import ar.edu.unlam.tallerweb1.domain.exceptions.UserNotExists
import ar.edu.unlam.tallerweb1.domain.exceptions.UserWithoutFriendsGroup
import ar.edu.unlam.tallerweb1.domain.model.FriendsGroup
import ar.edu.unlam.tallerweb1.domain.model.Payment
import ar.edu.unlam.tallerweb1.domain.model.SharedExpent
import ar.edu.unlam.tallerweb1.domain.model.User
import java.time.LocalDateTime.now

interface FriendsGroupService {
    fun getBalance(username: String): Map<String, Double>
    fun addExpentToGroup(username: String, detail: String, amount: Double)
}
class FriendsGroupServiceImpl (
    private val userRepository: UserRepository,
    private val friendsGroupRepository: FriendsGroupRepository,
    private val sharedExpensesRepository: SharedExpensesRepository,
    private val paymentRepository: PaymentRepository
) : FriendsGroupService  {

    override fun getBalance(username: String): Map<String, Double> {
        val user = findExistingUser(username)
        if (!user.hasFriendGroup()) return mapOf()

        val members = getFriendsOf(user)
        if (members.isEmpty()) return mapOf()

        return buildBalance(username, members)
    }

    override fun addExpentToGroup(username: String, detail: String, amount: Double) {
        userRepository.findByName(username)?.let { user ->
            val friendsGroup = friendsGroupRepository.getGroupOf(user)
            sharedExpensesRepository.save(SharedExpent(user, amount, detail, now(), friendsGroup))
        }
    }

    private fun buildBalance(username: String, members: List<User>): Map<String, Double> {
        val balance = buildEmptyBalanceWith(members)

        val sharedExpenses = sharedExpensesRepository.findBy(username)
        sharedExpenses.forEach { expent -> applyExpentToBalance(members, balance, expent) }
        return balance
    }

    private fun applyExpentToBalance(members: List<User>, balance: MutableMap<String, Double>, expent: SharedExpent) {
        updatePayerBalance(balance, expent)
        updateRestOfFriendsBalance(members, balance, expent)
        apllyExpentPayments(balance, expent)
    }

    private fun apllyExpentPayments(balance: MutableMap<String, Double>, expent: SharedExpent) {
        val payments = getPaymentsOf(expent)
        payments.forEach{ payment -> applyPayment(balance, payment) }
    }

    private fun applyPayment(balance: MutableMap<String, Double>, payment: Payment) {
        balance[payment.payer.name] = balance[payment.payer.name]!! + payment.amount
        balance[payment.sharedExpent.owner.name] = balance[payment.sharedExpent.owner.name]!! - payment.amount
    }

    private fun findExistingUser(username: String) = userRepository.findByName(username) ?: throw UserNotExists(username)

    private fun getPaymentsOf(expent: SharedExpent) = paymentRepository.findPaymentsOf(expent.id!!)

    private fun getFriendsOf(theUser: User) =
        friendsGroupRepository.getMembers(FriendsGroup(theUser.friendsGroup?.id!!, ""))

    private fun updateRestOfFriendsBalance(
        members: List<User>,
        balance: MutableMap<String, Double>,
        expent: SharedExpent
    ) = members.forEach{ member -> balance[member.name] = balance[member.name]!! - expent.amount / members.size }

    private fun updatePayerBalance(balance: MutableMap<String, Double>, expent: SharedExpent) {
        val payer = expent.payer
        balance[payer] = balance[payer]!! + expent.amount
    }

    private fun buildEmptyBalanceWith(members: List<User>): MutableMap<String, Double> {
        val balance: MutableMap<String, Double> = mutableMapOf()
        members.forEach{ member -> balance[member.name] = 0.0 }
        return balance
    }
}
