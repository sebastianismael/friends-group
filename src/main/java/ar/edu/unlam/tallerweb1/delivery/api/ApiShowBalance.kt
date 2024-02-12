package ar.edu.unlam.tallerweb1.delivery.api

import ar.edu.unlam.tallerweb1.delivery.Controller
import ar.edu.unlam.tallerweb1.domain.FriendsGroupService
import javax.servlet.http.HttpServletRequest

class ApiShowBalance(private val friendsGroupService: FriendsGroupService) : Controller {
    override fun invoke(request: HttpServletRequest) = friendsGroupService.getBalance(request.getParameter("user"))
}
