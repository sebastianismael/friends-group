package ar.edu.unlam.tallerweb1.delivery.controllers;


import ar.edu.unlam.tallerweb1.domain.SharedExpensesRepository;
import ar.edu.unlam.tallerweb1.domain.model.FriendsGroup;
import ar.edu.unlam.tallerweb1.domain.model.SharedExpent;
import ar.edu.unlam.tallerweb1.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

import static ar.edu.unlam.tallerweb1.delivery.HtmlStrings.NO_EXPENSES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ListSharedExpensesTest {

    private static final String USER = "user_1";
    private ListSharedExpenses controller;
    private SharedExpensesRepository repository;
    private HttpServletRequest request;

    private FriendsGroup group = new FriendsGroup(1l, "a");

    @BeforeEach
    public void init() {
        request = mock(HttpServletRequest.class);
        when(request.getParameter("user")).thenReturn(USER);

        repository = mock(SharedExpensesRepository.class);

        controller = new ListSharedExpenses(repository);
    }

    @Test
    public void returnAnEmtyListWhenThereIsNotExpenses() {
        givenThereIsNotExpensesForGroupOf(USER);
        String expenses = whenGetSharedExpensesOfGroupWithUser(USER);
        thenThereIsNotExpenses(expenses);
    }

    @Test
    public void returnAnExpensesListWhenUserGroupHasSeveral(){
        givenExpensesForGroupOf(USER);
        String expenses = whenGetSharedExpensesOfGroupWithUser(USER);
        thenThereAreExpenses(expenses);
    }

    private void givenExpensesForGroupOf(String user) {
        List<SharedExpent> list = new LinkedList<>();

        list.add(new SharedExpent(
                1L,
                new User(1L, "Francisco Buyo"),
                100.0,
                "Cena",
                LocalDateTime.now().plus(-5, ChronoUnit.DAYS),
                group));

        list.add(new SharedExpent(
                2L,
                new User(2L, "Alfonso Perez"),
                53.40,
                "Taxi",
                LocalDateTime.now().plus(-12, ChronoUnit.DAYS),
                group));

        when(repository.findBy(user)).thenReturn(list);
    }

    private void givenThereIsNotExpensesForGroupOf(String user) {
        when(repository.findBy(user)).thenReturn(new LinkedList<>());
    }

    private String whenGetSharedExpensesOfGroupWithUser(String user) {
        return controller.invoke(request);
    }

    private void thenThereAreExpenses(String expenses) {
        assertThat(expenses).isNotEqualTo(NO_EXPENSES);
    }

    private void thenThereIsNotExpenses(String expenses) {
        assertThat(expenses).isEqualTo(NO_EXPENSES);
    }

}
