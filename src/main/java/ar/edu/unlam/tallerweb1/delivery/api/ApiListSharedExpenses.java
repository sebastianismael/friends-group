package ar.edu.unlam.tallerweb1.delivery.api;

import ar.edu.unlam.tallerweb1.delivery.Controller;
import ar.edu.unlam.tallerweb1.domain.SharedExpensesRepository;
import ar.edu.unlam.tallerweb1.domain.model.SharedExpent;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;

import static java.lang.String.valueOf;
import static java.time.Duration.between;
import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.toList;

public class ApiListSharedExpenses implements Controller {

    private SharedExpensesRepository sharedExpensesRepository;
    public ApiListSharedExpenses(SharedExpensesRepository sharedExpensesRepository){
        this.sharedExpensesRepository = sharedExpensesRepository;
    }

    @Override
    public Object invoke(HttpServletRequest request) {
        String user = request.getParameter("user");
        return sharedExpensesRepository.findBy(user).stream().map(SharedExpentDto::new).collect(toList());
    }

    private class SharedExpentDto{
        private String payer;
        private String detail;
        private String minuteAgo;
        private String amount;

        public SharedExpentDto(SharedExpent expent){
            this.detail = expent.getDetail();
            this.payer = expent.getPayer();
            this.minuteAgo = formatMinutes(expent.getDate());
            this.amount = expent.getAmount().toString();
        }

        public String getPayer() {
            return payer;
        }

        public String getDetail() {
            return detail;
        }

        public String getMinuteAgo() {
            return minuteAgo;
        }

        public String getAmount() {
            return amount;
        }

        private String formatMinutes(LocalDateTime date) {
            return valueOf(between(date, now()).toMinutes());
        }

    }

}
