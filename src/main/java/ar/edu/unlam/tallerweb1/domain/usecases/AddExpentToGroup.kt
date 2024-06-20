package ar.edu.unlam.tallerweb1.domain.usecases

import ar.edu.unlam.tallerweb1.domain.FriendsGroupRepository
import ar.edu.unlam.tallerweb1.domain.SharedExpensesRepository
import ar.edu.unlam.tallerweb1.domain.UserRepository
import ar.edu.unlam.tallerweb1.domain.model.SharedExpent
import java.time.LocalDateTime.now

class AddExpentToGroup (
    private val userRepository: UserRepository,
    private val friendsGroupRepository: FriendsGroupRepository,
    private val sharedExpensesRepository: SharedExpensesRepository
){

    operator fun invoke(username: String, detail: String, amount: Double) {
        userRepository.findByName(username)?.let { user ->
            val friendsGroup = friendsGroupRepository.getGroupOf(user)
            sharedExpensesRepository.save(SharedExpent(user, amount, detail, now(), friendsGroup))
        }
    }
}