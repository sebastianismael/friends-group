package ar.edu.unlam.tallerweb1.delivery.controllers

import ar.edu.unlam.tallerweb1.delivery.Controller
import ar.edu.unlam.tallerweb1.delivery.HtmlStrings.Companion.ADD_EXPENT_NO_AMOUNT
import ar.edu.unlam.tallerweb1.delivery.HtmlStrings.Companion.ADD_EXPENT_NO_DETAIL
import ar.edu.unlam.tallerweb1.delivery.HtmlStrings.Companion.ADD_EXPENT_SUCCESS
import ar.edu.unlam.tallerweb1.domain.FriendsGroupService
import ar.edu.unlam.tallerweb1.domain.FriendsGroupServiceImpl
import org.apache.logging.log4j.util.Strings.isBlank
import org.apache.logging.log4j.util.Strings.isEmpty
import javax.servlet.http.HttpServletRequest

class AddExpent(private val friendsGroupService: FriendsGroupService) : Controller {
    override operator fun invoke(request: HttpServletRequest): String {
        val user = request.getParameter("user")

        val detail = request.getParameter("detail")
        if (isNotValid(detail)) return ADD_EXPENT_NO_DETAIL

        val amount = request.getParameter("amount")
        if (isNotValid(amount)) return ADD_EXPENT_NO_AMOUNT

        friendsGroupService.addExpentToGroup(user, detail, amount.toDouble())
        return ADD_EXPENT_SUCCESS
    }

    companion object {
        private fun isNotValid(amountParam: String) = isEmpty(amountParam) || isBlank(amountParam)
    }
}
