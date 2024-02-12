package ar.edu.unlam.tallerweb1.domain

import ar.edu.unlam.tallerweb1.domain.Repositories.Companion.getFriendsGroupRepository
import ar.edu.unlam.tallerweb1.domain.Repositories.Companion.getPaymentRepository
import ar.edu.unlam.tallerweb1.domain.Repositories.Companion.getSharedExpensesRepository
import ar.edu.unlam.tallerweb1.domain.Repositories.Companion.getUserRepository

class Services {
    companion object {
        fun getFriendsGroupService() = FriendsGroupServiceImpl(
            getUserRepository(),
            getFriendsGroupRepository(),
            getSharedExpensesRepository(),
            getPaymentRepository()
        )
    }
}