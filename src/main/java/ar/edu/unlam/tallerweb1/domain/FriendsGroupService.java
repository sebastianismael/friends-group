package ar.edu.unlam.tallerweb1.domain;

import ar.edu.unlam.tallerweb1.domain.exceptions.UserNotExists;
import ar.edu.unlam.tallerweb1.domain.exceptions.UserWithoutFriendsGroup;
import ar.edu.unlam.tallerweb1.domain.model.FriendsGroup;
import ar.edu.unlam.tallerweb1.domain.model.Payment;
import ar.edu.unlam.tallerweb1.domain.model.SharedExpent;
import ar.edu.unlam.tallerweb1.domain.model.User;

import java.time.LocalDateTime;
import java.util.*;

import static java.time.LocalDateTime.now;
import static java.util.Collections.EMPTY_MAP;

public class FriendsGroupService {
    private final UserRepository userRepository;
    private FriendsGroupRepository friendsGroupRepository;
    private SharedExpensesRepository sharedExpensesRepository;
    private PaymentRepository paymentRepository;

    public FriendsGroupService(UserRepository userRepository,
                               FriendsGroupRepository friendsGroupRepository,
                               SharedExpensesRepository sharedExpensesRepository,
                               PaymentRepository paymentRepository) {
        this.friendsGroupRepository = friendsGroupRepository;
        this.userRepository = userRepository;
        this.sharedExpensesRepository = sharedExpensesRepository;
        this.paymentRepository = paymentRepository;
    }
    public void addFriendsToGroup(String username, String newFriendName){
        final User user = findExistingUser(username);
        if(!user.hasFriendGroup()) throw new UserWithoutFriendsGroup(username);

        User friend = new User(newFriendName);
        friend.setFriendsGroup(new FriendsGroup(user.getFriendsGroup().getId(), ""));
        userRepository.saveWithFriendGroup(friend);
    }

    public Map<String, Double> getBalance(String username) {
        final User user = findExistingUser(username);
        if(!user.hasFriendGroup()) return EMPTY_MAP;

        final List<User> members = getFriendsOf(user);
        if(members.isEmpty()) return EMPTY_MAP;

        return buildBalance(username, members);
    }

    public void addExpentToGroup(String username, String detail, Double amount) {
        final User user = userRepository.findByName(username);
        final FriendsGroup friendsGroup = friendsGroupRepository.getGroupOf(user);
        sharedExpensesRepository.save(
                new SharedExpent(user, amount, detail, now(), friendsGroup)
        );
    }

    private Map<String, Double> buildBalance(String username, List<User> members) {
        Map<String, Double> balance = buildEmptyBalanceWith(members);

        final List<SharedExpent> sharedExpenses = sharedExpensesRepository.findBy(username);
        sharedExpenses.forEach(expent-> applyExpentToBalance(members, balance, expent));
        return balance;
    }

    private void applyExpentToBalance(List<User> members, Map<String, Double> balance, SharedExpent expent) {
        updatePayerBalance(balance, expent);
        updateRestOfFriendsBalance(members, balance, expent);
        apllyExpentPayments(balance, expent);
    }

    private void apllyExpentPayments(Map<String, Double> balance, SharedExpent expent) {
        final List<Payment> payments = getPaymentsOf(expent);
        payments.forEach(payment -> applyPayment(balance, payment));
    }

    private void applyPayment(Map<String, Double> balance, Payment payment) {
        balance.put(payment.getPayer().getName(),
                balance.get(payment.getPayer().getName()) + payment.getAmount());
        balance.put(payment.getSharedExpent().getOwner().getName(),
                balance.get(payment.getSharedExpent().getOwner().getName()) - payment.getAmount());
    }

    private User findExistingUser(String username) {
        final User user = userRepository.findByName(username);
        if(user == null) throw new UserNotExists(username);
        return user;
    }

    private List<Payment> getPaymentsOf(SharedExpent expent) {
        return paymentRepository.findPaymentsOf(expent.getId());
    }

    private List<User> getFriendsOf(User theUser) {
        return friendsGroupRepository.getMembers(new FriendsGroup(theUser.getFriendsGroup().getId(), ""));
    }

    private void updateRestOfFriendsBalance(List<User> members, Map<String, Double> balance, SharedExpent expent) {
        members.forEach(member ->
                balance.put(member.getName(), balance.get(member.getName()) - expent.getAmount()/ members.size())
        );
    }

    private void updatePayerBalance(Map<String, Double> balance, SharedExpent expent) {
        String payer = expent.getPayer();
        balance.put(payer, balance.get(payer) + expent.getAmount());
    }

    private Map<String, Double> buildEmptyBalanceWith(List<User> members) {
        Map<String, Double> balance = new HashMap<>();
        members.forEach(it -> balance.put(it.getName(), 0.0));
        return balance;
    }
}
