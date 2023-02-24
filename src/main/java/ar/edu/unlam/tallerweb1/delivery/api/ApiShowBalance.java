package ar.edu.unlam.tallerweb1.delivery.api;

import ar.edu.unlam.tallerweb1.delivery.Controller;
import ar.edu.unlam.tallerweb1.domain.FriendsGroupService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ApiShowBalance implements Controller {
    private FriendsGroupService friendsGroupService;

    public ApiShowBalance(FriendsGroupService friendsGroupService) {
        this.friendsGroupService = friendsGroupService;
    }

    @Override
    public Map<String, Double> invoke(HttpServletRequest request) {
        String user = request.getParameter("user");
        return friendsGroupService.getBalance(user);
    }
}
