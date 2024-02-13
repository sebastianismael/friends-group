package ar.edu.unlam.tallerweb1.delivery.controllers

import ar.edu.unlam.tallerweb1.delivery.Controller
import ar.edu.unlam.tallerweb1.delivery.HtmlStrings.BALANCE_HEADER
import ar.edu.unlam.tallerweb1.delivery.HtmlStrings.CLOSE_DIV
import ar.edu.unlam.tallerweb1.delivery.HtmlStrings.EURO
import ar.edu.unlam.tallerweb1.delivery.HtmlStrings.GREEN
import ar.edu.unlam.tallerweb1.delivery.HtmlStrings.NO_EXPENSES
import ar.edu.unlam.tallerweb1.delivery.HtmlStrings.RED
import ar.edu.unlam.tallerweb1.delivery.HtmlStrings.TAB
import ar.edu.unlam.tallerweb1.domain.FriendsGroupService
import javax.servlet.http.HttpServletRequest

class ShowBalance(private val friendsGroupService: FriendsGroupService) : Controller {
    override fun invoke(request: HttpServletRequest): String {
        val user = request.getParameter("user")
        val balance = friendsGroupService.getBalance(user)
        return toHtml(balance)
    }

    private fun toHtml(balance: Map<String, Double>): String {
        if (balance.isEmpty()) return NO_EXPENSES
        return formatResults(balance)
    }

    private fun formatResults(balance: Map<String, Double>): String {
        val buffer = StringBuilder()
        buffer.append(BALANCE_HEADER)
        balance.entries.forEach { formatBalanceLine(buffer, it) }
        buffer.append(CLOSE_DIV)
        return buffer.toString()
    }

    private fun formatBalanceLine(sb: StringBuilder, entry: Map.Entry<String, Double>) {
        sb.append("<p>")
        sb.append(entry.key.trim())
        sb.append(TAB).append(TAB)
        sb.append(entry.value.format())
        sb.append("</p>")
    }

    private fun Double.format(): String {
        val str = if (this >= 0) GREEN else RED
        return "$str$this$EURO</span>"
    }
}
