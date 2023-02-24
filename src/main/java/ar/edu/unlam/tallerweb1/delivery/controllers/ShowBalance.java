package ar.edu.unlam.tallerweb1.delivery.controllers;

import ar.edu.unlam.tallerweb1.delivery.Controller;
import ar.edu.unlam.tallerweb1.domain.FriendsGroupService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Map.Entry;

import static ar.edu.unlam.tallerweb1.delivery.HtmlStrings.*;

public class ShowBalance implements Controller {
    private FriendsGroupService friendsGroupService;

    public ShowBalance(FriendsGroupService friendsGroupService) {
        this.friendsGroupService = friendsGroupService;
    }

    @Override
    public String invoke(HttpServletRequest request) {
        String user = request.getParameter("user");
        final Map<String, Double> balance = friendsGroupService.getBalance(user);
        return toHtml(balance);
    }

    private String toHtml(Map<String, Double> balance){
        if(balance.isEmpty()) return NO_EXPENSES;
        return formatResults(balance);
    }

    private String formatResults(Map<String, Double> balance) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(BALANCE_HEADER);
        balance.entrySet().forEach(user -> formatBalanceLine(buffer, user));
        buffer.append(CLOSE_DIV);
        return buffer.toString();
    }

    private void formatBalanceLine(StringBuilder sb, Entry<String, Double> entry) {
        sb.append("<p>").append(entry.getKey().trim()).append(TAB).append(TAB);
        final Double value = entry.getValue();
        if(value >= 0)
            sb.append("<span style=\"color: green\">");
        else
            sb.append("<span style=\"color: red\">");
        sb.append(value).append(EURO).append("</span></p>");
    }

}
