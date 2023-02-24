package ar.edu.unlam.tallerweb1.domain;

import static ar.edu.unlam.tallerweb1.domain.Repositories.*;

public class Services {

    public static FriendsGroupService getFriendsGroupService() {
        return new FriendsGroupService(
                getUserRepository(),
                getFriendsGroupRepository(),
                getSharedExpensesRepository(),
                getPaymentRepository());
    }
}
