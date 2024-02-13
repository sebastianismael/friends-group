package ar.edu.unlam.tallerweb1.domain

import ar.edu.unlam.tallerweb1.domain.Repositories.getFriendsGroupRepository
import ar.edu.unlam.tallerweb1.domain.Repositories.getPaymentRepository
import ar.edu.unlam.tallerweb1.domain.Repositories.getSharedExpensesRepository
import ar.edu.unlam.tallerweb1.domain.Repositories.getUserRepository

object Services {
    fun getFriendsGroupService() = FriendsGroupServiceImpl(
        getUserRepository(),
        getFriendsGroupRepository(),
        getSharedExpensesRepository(),
        getPaymentRepository()
    )
}