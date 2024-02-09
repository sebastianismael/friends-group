package ar.edu.unlam.tallerweb1.delivery.api

import ar.edu.unlam.tallerweb1.delivery.Controller
import ar.edu.unlam.tallerweb1.domain.SharedExpensesRepository
import ar.edu.unlam.tallerweb1.domain.model.SharedExpent
import java.time.Duration.between
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import javax.servlet.http.HttpServletRequest

class ApiListSharedExpenses(private val sharedExpensesRepository: SharedExpensesRepository) : Controller {
    override fun invoke(request: HttpServletRequest): Any {
        val user = request.getParameter("user")
        return sharedExpensesRepository.findBy(user).map { SharedExpentDto(it) }
    }

    private inner class SharedExpentDto(expent: SharedExpent) {
        val payer: String = expent.payer
        val detail: String = expent.detail
        val minuteAgo: String
        val amount: String

        init {
            this.minuteAgo = formatMinutes(expent.date)
            this.amount = expent.amount.toString()
        }

        private fun formatMinutes(date: LocalDateTime) = between(date, now()).toMinutes().toString()
    }
}
