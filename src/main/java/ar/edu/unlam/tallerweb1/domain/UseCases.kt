package ar.edu.unlam.tallerweb1.domain

import ar.edu.unlam.tallerweb1.domain.Repositories.getFriendsGroupRepository
import ar.edu.unlam.tallerweb1.domain.Repositories.getPaymentRepository
import ar.edu.unlam.tallerweb1.domain.Repositories.getSharedExpensesRepository
import ar.edu.unlam.tallerweb1.domain.Repositories.getUserRepository
import ar.edu.unlam.tallerweb1.domain.usecases.AddExpentToGroup
import ar.edu.unlam.tallerweb1.domain.usecases.AddFriendsToGroup
import ar.edu.unlam.tallerweb1.domain.usecases.GetBalance

object UseCases {

    fun addFriendsToGroup() = AddFriendsToGroup( getUserRepository())
    fun getBalance() = GetBalance(getFriendsGroupRepository(), getSharedExpensesRepository(), getUserRepository(), getPaymentRepository())
    fun addExpentToGroup() = AddExpentToGroup(getUserRepository(), getFriendsGroupRepository(), getSharedExpensesRepository())
}