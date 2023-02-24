package ar.edu.unlam.tallerweb1.delivery.controllers;

import ar.edu.unlam.tallerweb1.delivery.Controller;
import ar.edu.unlam.tallerweb1.domain.FriendsGroupService;

import javax.servlet.http.HttpServletRequest;

import static ar.edu.unlam.tallerweb1.delivery.HtmlStrings.*;
import static org.apache.logging.log4j.util.Strings.isBlank;
import static org.apache.logging.log4j.util.Strings.isEmpty;

public class AddExpent implements Controller {

    private final FriendsGroupService friendsGroupService;

    public AddExpent(FriendsGroupService friendsGroupService) {
        this.friendsGroupService = friendsGroupService;
    }
    @Override
    public Object invoke(HttpServletRequest request) {
        String user = request.getParameter("user");

        String detail = request.getParameter("detail");
        if(isNotValid(detail)) return ADD_EXPENT_NO_DETAIL;

        final String amount = request.getParameter("amount");
        if(isNotValid(amount)) return ADD_EXPENT_NO_AMOUNT;

        friendsGroupService.addExpentToGroup(user, detail, Double.valueOf(amount));
        return ADD_EXPENT_SUCCESS;
    }

    private static boolean isNotValid(String amountParam) {
        return isEmpty(amountParam) || isBlank(amountParam);
    }
}
