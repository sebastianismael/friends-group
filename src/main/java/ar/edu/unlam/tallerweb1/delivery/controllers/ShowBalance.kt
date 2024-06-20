package ar.edu.unlam.tallerweb1.delivery.controllers

import ar.edu.unlam.tallerweb1.delivery.Controller
import ar.edu.unlam.tallerweb1.delivery.HtmlStrings.BALANCE_HEADER
import ar.edu.unlam.tallerweb1.delivery.HtmlStrings.CLOSE_DIV
import ar.edu.unlam.tallerweb1.delivery.HtmlStrings.EURO
import ar.edu.unlam.tallerweb1.delivery.HtmlStrings.GREEN
import ar.edu.unlam.tallerweb1.delivery.HtmlStrings.NO_EXPENSES
import ar.edu.unlam.tallerweb1.delivery.HtmlStrings.RED
import ar.edu.unlam.tallerweb1.delivery.HtmlStrings.TAB
import ar.edu.unlam.tallerweb1.domain.usecases.GetBalance
import javax.servlet.http.HttpServletRequest
import kotlin.collections.Map.Entry

class ShowBalance(private val getBalance: GetBalance) : Controller {
    override fun invoke(request: HttpServletRequest): String {
        val user = request.getParameter("user")
        val balance = getBalance(user)
        return toHtml(balance)
    }

    private fun toHtml(balance: Map<String, Double>): String {
        if (balance.isEmpty()) return NO_EXPENSES
        return formatResults(balance)
    }

    private fun formatResults(balance: Map<String, Double>): String {
        val buffer = StringBuilder()
        buffer.append(BALANCE_HEADER)
        balance.entries.forEach { buffer.formatBalanceLine(it) }
        buffer.append(CLOSE_DIV)
        return buffer.toString()
    }
}

private fun StringBuilder.formatBalanceLine(entry: Entry<String, Double>) {
    this.append("<p>")
    this.append(entry.key.trim())
    this.append(TAB).append(TAB)
    this.append(entry.value.format())
    this.append("</p>")

}

private fun Double.format(): String {
    val str = if (this >= 0) GREEN else RED
    return "$str$this$EURO</span>"
}
