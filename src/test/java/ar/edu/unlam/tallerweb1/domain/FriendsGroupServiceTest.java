package ar.edu.unlam.tallerweb1.domain;

import ar.edu.unlam.tallerweb1.domain.exceptions.UserNotExists;
import ar.edu.unlam.tallerweb1.domain.exceptions.UserWithoutFriendsGroup;
import ar.edu.unlam.tallerweb1.domain.model.FriendsGroup;
import ar.edu.unlam.tallerweb1.domain.model.Payment;
import ar.edu.unlam.tallerweb1.domain.model.SharedExpent;
import ar.edu.unlam.tallerweb1.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class FriendsGroupServiceTest {

    private static final String DETAIL = "Una cena";
    private static final Double AMOUNT = 1050.0;
    public static final String FRIEND_1 = "friend_1";
    public static final String FRIEND_2 = "friend_2";
    public static final String FRIEND_3 = "friend_3";
    private FriendsGroupService friendsGroupService;
    private final String USER = "user_18989";
    private final String FRIEND = "friend";
    private UserRepository userRepository;
    private FriendsGroupRepository friendsGroupRepository;
    private SharedExpensesRepository sharedExpensesRepository;
    private PaymentRepository paymentRepository;

    @BeforeEach
    public void init(){
        userRepository = mock(UserRepository.class);
        friendsGroupRepository = mock(FriendsGroupRepository.class);
        sharedExpensesRepository = mock(SharedExpensesRepository.class);
        paymentRepository = mock(PaymentRepository.class);
        friendsGroupService = new FriendsGroupServiceImpl(
                userRepository,
                friendsGroupRepository,
                sharedExpensesRepository,
                paymentRepository
        );
    }

    @Test
    public void addFriendShouldThrowAnErrorIfUserNotExists(){
        assertThrows(UserNotExists.class, () -> {
            givenNotExistsUser(USER);
            whenAddFriend(USER, FRIEND);
        });
    }

    @Test
    public void addFriendShouldThrowAnErrorIfUserDoesHaveAFriendGroup(){
        assertThrows(UserWithoutFriendsGroup.class, () -> {
            givenUserWithoutFriensGroup(USER);
            whenAddFriend(USER, FRIEND);
        });
    }

    @Test
    public void canAddFriendToMyGroup(){
        givenUserWithFriensGroup(USER);
        whenAddFriend(USER, FRIEND);
        thenFriendIsAdded();
    }

    @Test
    public void balanceShouldBeEmptyIfThereIsNotFriendsInGroup(){
        givenAnEmptyFriendsGroup(USER);
        final Map<String, Double> balance = whenGetBalance(USER);
        thenTheBalanceIsEmpty(balance);
    }

    @Test
    public void balanceShouldBeEmptyIfUserHasNotAFriendsGroup(){
        givenUserWithoutFriensGroup(USER);
        final Map<String, Double> balance = whenGetBalance(USER);
        thenTheBalanceIsEmpty(balance);
    }

    @Test
    public void balanceShouldThrowAnErrorIfUserNotExists(){
        assertThrows(UserNotExists.class, () -> {
            givenNotExistsUser(USER);
            whenGetBalance(USER);
        });
    }

    @Test
    public void balanceShouldBeZeroForEverybodyIfGroupHasNotExpenses(){
        givenUserWithFriensGroup(USER);
        givenAGroupWith(4);
        givenGroupHasNotExpenses();
        final Map<String, Double> balance = whenGetBalance(USER);
        thenBalanceShouldHasALineFor(USER, 0.0, balance);
        thenBalanceShouldHasALineFor(FRIEND_1, 0.0, balance);
        thenBalanceShouldHasALineFor(FRIEND_2, 0.0, balance);
        thenBalanceShouldHasALineFor(FRIEND_3, 0.0, balance);
    }

    @Test
    public void balanceWithOneExpentAndWithoutPayments(){
        givenUserWithFriensGroup(USER);
        givenAGroupWith(4);
        givenGroupHasAnExpents(1L, USER, 120.0);

        final Map<String, Double> balance = whenGetBalance(USER);

        thenBalanceShouldHasALineFor(USER, 90.0, balance);
        thenBalanceShouldHasALineFor(FRIEND_1, -30.0, balance);
        thenBalanceShouldHasALineFor(FRIEND_2, -30.0, balance);
        thenBalanceShouldHasALineFor(FRIEND_3, -30.0, balance);
    }

    @Test
    public void balanceWithMoreThanOneExpentAndWithoutPayments(){
        givenUserWithFriensGroup(USER);
        givenAGroupWith(4);

        List<SharedExpent> expenses = new LinkedList<>();
        SharedExpent e1 = new SharedExpent(1l, new User(USER), 120.0, "cena", LocalDateTime.now(), null);
        expenses.add(e1);
        SharedExpent e2 = new SharedExpent(12l, new User(FRIEND_1), 120.0, "almuerzo", LocalDateTime.now(), null);
        expenses.add(e2);
        givenGroupHasAnExpents(expenses);

        final Map<String, Double> balance = whenGetBalance(USER);

        thenBalanceShouldHasALineFor(USER, 60.0, balance);
        thenBalanceShouldHasALineFor(FRIEND_1, 60.0, balance);
        thenBalanceShouldHasALineFor(FRIEND_2, -60.0, balance);
        thenBalanceShouldHasALineFor(FRIEND_3, -60.0, balance);
    }

    @Test
    public void balanceWithMoreThanOneExpentAndWithPayments(){
        givenUserWithFriensGroup(USER);
        givenAGroupWith(4);

        List<SharedExpent> expenses = new LinkedList<>();
        SharedExpent e1 = new SharedExpent(1l, new User(USER), 120.0, "cena", LocalDateTime.now(), null);
        expenses.add(e1);
        SharedExpent e2 = new SharedExpent(12l, new User(FRIEND_1), 120.0, "almuerzo", LocalDateTime.now(), null);
        expenses.add(e2);
        givenGroupHasAnExpents(expenses);

        givenAPayment(FRIEND_3, 30.0, e1);
        givenAPayment(FRIEND_3, 30.0, e2);

        final Map<String, Double> balance = whenGetBalance(USER);

        thenBalanceShouldHasALineFor(USER, 30.0, balance);
        thenBalanceShouldHasALineFor(FRIEND_1, 30.0, balance);
        thenBalanceShouldHasALineFor(FRIEND_2, -60.0, balance);
        thenBalanceShouldHasALineFor(FRIEND_3, 0.0, balance);
    }

    @Test
    public void addExpentShouldPersistANewOne(){
        givenUserWithFriensGroup(USER);
        whenAddExpentToAGroup(USER, DETAIL, AMOUNT);
        thenExpentIsSaved();
    }

    private void thenExpentIsSaved() {
        doNothing().when(sharedExpensesRepository).save(mock(SharedExpent.class));// TODO investigar esto.!!!;
        verify(sharedExpensesRepository, times(1)).save(any());
    }

    private void whenAddExpentToAGroup(String user, String detail, Double amount) {
        friendsGroupService.addExpentToGroup(user, detail, amount);
    }

    private void givenAPayment(String user, double amount, SharedExpent sharedExpent) {
        List<Payment> payments = new LinkedList<>();
        payments.add(aPaymentWith(user, amount, sharedExpent));
        when(paymentRepository.findPaymentsOf(sharedExpent.getId())).thenReturn(payments);
    }

    private Payment aPaymentWith(String user, double amount, SharedExpent sharedExpent) {
        return new Payment(1L, new User(user), amount, sharedExpent);
    }

    private void givenGroupHasNotExpenses() {
        when(sharedExpensesRepository.findBy(any())).thenReturn(new LinkedList<>());
    }

    private void givenGroupHasAnExpents(Long id, String payer, double amount) {
        List<SharedExpent> expenses = new LinkedList<>();
        expenses.add(aSharedExpentWith(id, payer, amount));
        when(sharedExpensesRepository.findBy(payer)).thenReturn(expenses);
    }

    private void givenGroupHasAnExpents(List<SharedExpent> expenses) {
        when(sharedExpensesRepository.findBy(any())).thenReturn(expenses);
    }

    private void givenAGroupWith(int friendsNumber) {
        List<User> friends = new LinkedList<>();
        for(int i = 1; i < friendsNumber; i++){
            friends.add(new User("friend_"+i));
        }
        friends.add(new User(USER));
        when(friendsGroupRepository.getMembers(any())).thenReturn(friends);
    }

    private void givenAnEmptyFriendsGroup(String user) {
        User mockUser = mock(User.class);
        FriendsGroup mockGroup = mock(FriendsGroup.class);
        when(mockUser.getFriendsGroup()).thenReturn(mockGroup);
        when(userRepository.findByName(user)).thenReturn(mockUser);
        when(friendsGroupRepository.getMembers(any())).thenReturn(new LinkedList<>());
    }

    private void givenUserWithFriensGroup(String user) {
        User friend = new User(1L, "bad guy");
        friend.setFriendsGroup(new FriendsGroup(4L, ""));
        when(userRepository.findByName(user)).thenReturn(friend);
    }

    private void givenUserWithoutFriensGroup(String user) {
        User userWithoutFriends = new User(1L, "bad guy");
        when(userRepository.findByName(user)).thenReturn(userWithoutFriends);
    }

    private void givenNotExistsUser(String user) {
        when(userRepository.findByName(user)).thenReturn(null);
    }

    private void whenAddFriend(String user, String friend) {
        friendsGroupService.addFriendsToGroup(user, friend);
    }

    private Map<String, Double> whenGetBalance(String user) {
        return friendsGroupService.getBalance(user);
    }

    private void thenFriendIsAdded() {
        verify(userRepository, times(1)).saveWithFriendGroup(any());
    }

    private void thenTheBalanceIsEmpty(Map<String, Double> balance) {
        assertThat(balance).isEmpty();
    }

    private void thenBalanceShouldHasALineFor(String user, double amount, Map<String, Double> balance) {
        assertThat(balance.get(user)).isEqualTo(amount);
    }

    private SharedExpent aSharedExpentWith(Long id, String payer, double amount) {
        return new SharedExpent(id, new User(payer), amount, "detail", LocalDateTime.now(), new FriendsGroup(1l, ""));
    }
}
