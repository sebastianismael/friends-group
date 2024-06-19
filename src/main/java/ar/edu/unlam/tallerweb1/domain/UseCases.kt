package ar.edu.unlam.tallerweb1.domain

import ar.edu.unlam.tallerweb1.domain.Repositories.getUserRepository
import ar.edu.unlam.tallerweb1.domain.usecases.AddFriendsToGroup

object UseCases {

    fun addFriendsToGroup() = AddFriendsToGroup( getUserRepository())
}