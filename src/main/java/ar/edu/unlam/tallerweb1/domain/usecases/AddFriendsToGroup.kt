package ar.edu.unlam.tallerweb1.domain.usecases

import ar.edu.unlam.tallerweb1.domain.UserRepository
import ar.edu.unlam.tallerweb1.domain.exceptions.UserNotExists
import ar.edu.unlam.tallerweb1.domain.exceptions.UserWithoutFriendsGroup
import ar.edu.unlam.tallerweb1.domain.model.FriendsGroup
import ar.edu.unlam.tallerweb1.domain.model.User

class AddFriendsToGroup(
    private val userRepository: UserRepository
) {

    operator fun invoke(username: String, newFriendName: String){
        val user = findExistingUser(username)
        if (!user.hasFriendGroup()) throw UserWithoutFriendsGroup(username)

        val friend = User(newFriendName)
        friend.friendsGroup = FriendsGroup(user.friendsGroup?.id!!, "")
        userRepository.saveWithFriendGroup(friend)
    }

    private fun findExistingUser(username: String) = userRepository.findByName(username) ?: throw UserNotExists(username)
}