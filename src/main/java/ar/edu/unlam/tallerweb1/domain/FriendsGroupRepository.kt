package ar.edu.unlam.tallerweb1.domain

import ar.edu.unlam.tallerweb1.domain.model.FriendsGroup
import ar.edu.unlam.tallerweb1.domain.model.User

interface FriendsGroupRepository {
    fun getMembers(group: FriendsGroup): List<User?>

    fun getGroupOf(user: User): FriendsGroup?
}
