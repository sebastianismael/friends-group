package ar.edu.unlam.tallerweb1.delivery.controllers;

import ar.edu.unlam.tallerweb1.delivery.Controller;
import ar.edu.unlam.tallerweb1.domain.FriendsGroupService;

import javax.servlet.http.HttpServletRequest;

import static ar.edu.unlam.tallerweb1.delivery.HtmlStrings.FRIEND_ADDED;

public class AddFriend implements Controller {
    private final FriendsGroupService friendsGroupService;

    public AddFriend(FriendsGroupService friendsGroupService) {
        this.friendsGroupService = friendsGroupService;
    }

    @Override
    public String invoke(HttpServletRequest request) {
        String user = request.getParameter("user");
        String friend = request.getParameter("friend");
        friendsGroupService.addFriendsToGroup(user, friend);
        return FRIEND_ADDED;
    }
}
