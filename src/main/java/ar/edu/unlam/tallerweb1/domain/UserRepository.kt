package ar.edu.unlam.tallerweb1.domain

import ar.edu.unlam.tallerweb1.domain.model.User

interface UserRepository {
    fun save(user: User)
    fun saveWithFriendGroup(user: User)
    fun findByName(name: String): User?
}
