package ar.edu.unlam.tallerweb1.domain

import ar.edu.unlam.tallerweb1.domain.model.SharedExpent
import java.time.LocalDateTime.now

interface FriendsGroupService {
    fun addExpentToGroup(username: String, detail: String, amount: Double)
}
class FriendsGroupServiceImpl (
    private val userRepository: UserRepository,
    private val friendsGroupRepository: FriendsGroupRepository,
    private val sharedExpensesRepository: SharedExpensesRepository,
    private val paymentRepository: PaymentRepository
) : FriendsGroupService  {

    override fun addExpentToGroup(username: String, detail: String, amount: Double) {
        userRepository.findByName(username)?.let { user ->
            val friendsGroup = friendsGroupRepository.getGroupOf(user)
            sharedExpensesRepository.save(SharedExpent(user, amount, detail, now(), friendsGroup))
        }
    }

}
