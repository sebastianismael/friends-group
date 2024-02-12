package ar.edu.unlam.tallerweb1.domain;

public class Services {

    public static FriendsGroupService getFriendsGroupService() {
        return new FriendsGroupService(
                Repositories.Companion.getUserRepository(),
                Repositories.Companion.getFriendsGroupRepository(),
                Repositories.Companion.getSharedExpensesRepository(),
                Repositories.Companion.getPaymentRepository());
    }
}