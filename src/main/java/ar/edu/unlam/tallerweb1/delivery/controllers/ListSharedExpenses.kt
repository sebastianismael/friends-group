package ar.edu.unlam.tallerweb1.delivery.controllers

import ar.edu.unlam.tallerweb1.delivery.Controller
import ar.edu.unlam.tallerweb1.delivery.HtmlStrings.EURO
import ar.edu.unlam.tallerweb1.delivery.HtmlStrings.EXPEND_SINCE_DAYS
import ar.edu.unlam.tallerweb1.delivery.HtmlStrings.EXPEND_SINCE_MINUTES
import ar.edu.unlam.tallerweb1.delivery.HtmlStrings.EXPENSES_HEADER
import ar.edu.unlam.tallerweb1.delivery.HtmlStrings.NEW_LINE
import ar.edu.unlam.tallerweb1.delivery.HtmlStrings.NO_EXPENSES
import ar.edu.unlam.tallerweb1.delivery.HtmlStrings.TAB
import ar.edu.unlam.tallerweb1.domain.SharedExpensesRepository
import ar.edu.unlam.tallerweb1.domain.model.SharedExpent
import java.time.Duration.between
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import javax.servlet.http.HttpServletRequest

class ListSharedExpenses(private val sharedExpensesRepository: SharedExpensesRepository) : Controller {
    override fun invoke(request: HttpServletRequest): String {
        val user = request.getParameter("user")
        val sharedExpenses = sharedExpensesRepository.findBy(user)
        return toHtml(sharedExpenses, user)
    }

    private fun toHtml(results: List<SharedExpent>, user: String): String {
        if (results.isEmpty()) return NO_EXPENSES
        return formatResults(results, user)
    }

    private fun formatResults(results: List<SharedExpent>, user: String): String {
        val buffer = StringBuilder(header(user))
        results.forEach { formatExpent(buffer, it) }
        return buffer.toString()
    }

    private fun formatExpent(sb: StringBuilder, each: SharedExpent) {
        sb.append(each.owner.name).append(TAB).append(each.amount).append(EURO).append(NEW_LINE)
        sb.append(each.detail).append(TAB).append(calculateDate(each.date)).append(NEW_LINE).append(NEW_LINE)
    }

    private fun calculateDate(date: LocalDateTime): String {
        val days = daysBeforeToday(date)
        if (days > 0) return formatDays(days)
        return formatMinutes(date)
    }

    private fun daysBeforeToday(date: LocalDateTime) = between(date, now()).toDays()
    private fun formatDays(days: Long) = String.format(EXPEND_SINCE_DAYS, days)
    private fun formatMinutes(date: LocalDateTime) = String.format(EXPEND_SINCE_MINUTES, between(date, now()).toMinutes())
    private fun header(user: String) = String.format(EXPENSES_HEADER, user, user, user)
}
