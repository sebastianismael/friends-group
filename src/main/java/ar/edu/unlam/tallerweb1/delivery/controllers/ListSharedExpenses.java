package ar.edu.unlam.tallerweb1.delivery.controllers;

import ar.edu.unlam.tallerweb1.delivery.Controller;
import ar.edu.unlam.tallerweb1.domain.SharedExpensesRepository;
import ar.edu.unlam.tallerweb1.domain.model.SharedExpent;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static ar.edu.unlam.tallerweb1.delivery.HtmlStrings.*;
import static java.lang.String.format;
import static java.time.Duration.between;
import static java.time.LocalDateTime.now;

public class ListSharedExpenses implements Controller {

    private SharedExpensesRepository sharedExpensesRepository;
    public ListSharedExpenses(SharedExpensesRepository sharedExpensesRepository){
        this.sharedExpensesRepository = sharedExpensesRepository;
    }

    @Override
    public String invoke(HttpServletRequest request) {
        String user = request.getParameter("user");
        final List<SharedExpent> sharedExpenses = sharedExpensesRepository.findBy(user);
        return toHtml(sharedExpenses, user);
    }

    private String toHtml(List<SharedExpent> results, String user){
        if(results.isEmpty()) return NO_EXPENSES;
        return formatResults(results, user);
    }

    private String formatResults(List<SharedExpent> results, String user) {
        StringBuilder buffer = new StringBuilder(header(user));
        results.forEach(expent -> formatExpent(buffer, expent));
        return buffer.toString();
    }

    private static String header(String user) {
        return format(EXPENSES_HEADER, user, user, user);
    }

    private void formatExpent(StringBuilder sb, SharedExpent each) {
        sb.append(each.getOwner().getName()).append(TAB).append(each.getAmount()).append(EURO).append(NEW_LINE);
        sb.append(each.getDetail()).append(TAB).append(calculateDate(each.getDate())).append(NEW_LINE).append(NEW_LINE);
    }

    private String calculateDate(LocalDateTime date) {
        final long days = daysBeforeToday(date);
        if(days > 0) return formatDays(days);
        return formatMinutes(date);
    }

    private long daysBeforeToday(LocalDateTime date) {
        return between(date, now()).toDays();
    }

    private String formatDays(long days) {
        return format(EXPEND_SINCE_DAYS, days);
    }

    private String formatMinutes(LocalDateTime date) {
        return format(EXPEND_SINCE_MINUTES, between(date, now()).toMinutes());
    }
}
