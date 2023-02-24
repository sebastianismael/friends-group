package ar.edu.unlam.tallerweb1.delivery.controllers;

import ar.edu.unlam.tallerweb1.domain.FriendsGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import static ar.edu.unlam.tallerweb1.delivery.HtmlStrings.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AddExpentTest {

    private static final String USER = "user_1";
    private static final String DETAIL = "Cena";

    private static final String AMOUNT = "100.50";
    private HttpServletRequest request;
    private AddExpent controller;
    private FriendsGroupService friendsGroupService;
    @BeforeEach
    public void init() {
        request = mock(HttpServletRequest.class);
        when(request.getParameter("user")).thenReturn(USER);
        when(request.getParameter("amount")).thenReturn(AMOUNT);
        when(request.getParameter("detail")).thenReturn(DETAIL);
        friendsGroupService = mock(FriendsGroupService.class);

        controller = new AddExpent(friendsGroupService);
    }
    @Test
    public void addExpentToUserFriendGroup(){
        whenAddAnExpentToFriendsGroupOf(USER);
        thenAddExpentToGroup(DETAIL, AMOUNT);
    }

    @Test
    public void shouldReturnErrorIfAmountIsNotPresent(){
        givenAmountIsNotPresent();
        String response = whenAddAnExpentToFriendsGroupOf(USER);
        thenGetErrorMessage(response, ADD_EXPENT_NO_AMOUNT);
    }

    @Test
    public void shouldReturnErrorIfDetailIsNotPresent(){
        givenDetailIsNotPresent();
        String response = whenAddAnExpentToFriendsGroupOf(USER);
        thenGetErrorMessage(response, ADD_EXPENT_NO_DETAIL);
    }

    private void givenDetailIsNotPresent() {
        when(request.getParameter("detail")).thenReturn("");
    }

    private void givenAmountIsNotPresent() {
        when(request.getParameter("amount")).thenReturn("");
    }

    private String whenAddAnExpentToFriendsGroupOf(String user) {
        return controller.invoke(request).toString();
    }

    private void thenAddExpentToGroup(String detail, String amount) {
        verify(friendsGroupService, times(1)).addExpentToGroup(USER, detail, Double.valueOf(amount));
    }

    private void thenGetErrorMessage(String actual, String expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
