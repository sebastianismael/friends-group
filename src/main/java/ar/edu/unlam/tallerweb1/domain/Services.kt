package ar.edu.unlam.tallerweb1.domain

object Services {
    val friendsGroupService: FriendsGroupService
        get() = FriendsGroupService(
            Repositories.getUserRepository(),
            Repositories.getFriendsGroupRepository(),
            Repositories.getSharedExpensesRepository(),
            Repositories.getPaymentRepository()
        )
}
