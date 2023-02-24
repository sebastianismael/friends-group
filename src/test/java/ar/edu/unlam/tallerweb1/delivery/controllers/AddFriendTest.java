package ar.edu.unlam.tallerweb1.delivery.controllers;

import ar.edu.unlam.tallerweb1.domain.FriendsGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.*;

public class AddFriendTest {

    private static final String USER = "user_1";
    private static final String FRIEND = "friend";
    private HttpServletRequest request;
    private AddFriend controller;
    private FriendsGroupService friendsGroupService;
    @BeforeEach
    public void init() {
        request = mock(HttpServletRequest.class);
        when(request.getParameter("user")).thenReturn(USER);
        when(request.getParameter("friend")).thenReturn(FRIEND);
        friendsGroupService = mock(FriendsGroupService.class);

        controller = new AddFriend(friendsGroupService);
    }
    @Test
    public void addFriendToUserFriendGroup(){
        whenAddFriendToFriendsGroupOf(USER, FRIEND);
        thenAddFriendToGroup(FRIEND);
    }

    private void whenAddFriendToFriendsGroupOf(String user, String friend) {
        controller.invoke(request);
    }

    private void thenAddFriendToGroup(String friend) {
        verify(friendsGroupService, times(1)).addFriendsToGroup(USER, friend);
    }
}
